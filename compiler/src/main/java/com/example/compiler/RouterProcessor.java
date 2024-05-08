package com.example.compiler;

import com.example.annotation.Router;
import com.example.annotation.RouterBean;
import com.example.compiler.utils.Constants;
import com.example.compiler.utils.EmptyUtils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@SupportedOptions({Constants.MODULE_NAME, Constants.APT_PACKAGE})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({Constants.ROUTER_ANNATATION_TYPES})
@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

    private Elements elementUtils;// 操作Element工具类
    private Types typeUtils; //type(类信息)工具类
    private Filer filer; //文件生成器
    private Messager messager; //用来输出警告、错误等信息
    private String moduleName;
    private String packageNameForAPT;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        Map<String, String> options = processingEnvironment.getOptions();
        if (!EmptyUtils.isEmpty(options)) {
            moduleName = processingEnv.getOptions().get("moduleName");
            packageNameForAPT = processingEnv.getOptions().get("packageNameForAPT");
            messager.printMessage(Diagnostic.Kind.NOTE, moduleName); //内容时红色
            messager.printMessage(Diagnostic.Kind.NOTE, packageNameForAPT);
        }
        if (EmptyUtils.isEmpty(moduleName) || EmptyUtils.isEmpty(packageNameForAPT)) {
            throw new RuntimeException("注解处理器需要的参数moduleName或者packageName为空，请在对应build.gradle配置参数");
        }
    }

    /**
     * @param set              使用了支持处理注解的节点集合(类上面写了注解)
     * @param roundEnvironment
     * @return true表示后续处理器不会再处理(已经处理完成)
     * 相关内容可以参考 EventBus
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!EmptyUtils.isEmpty(set)) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Router.class);
            if (!EmptyUtils.isEmpty(elements)) {
                parseElements(elements);
            }

            return true;
        }

        return false;
    }

    /**
     * 临时map存储，用来存放路由组Group对应的详细Path类对象，生成路由路径类文件时遍历，
     * key是组名app，value是组名的路由路径 Router$$Path$$app.class
     */

    private final Map<String, List<RouterBean>> tempPathMap = new HashMap<>();

    //临时map存储，用来存放路由Group信息，生成路由组类文件遍历， key组名app，value= Grouter$$Path$app.class
    private Map<String,String> tempGroupmap = new HashMap<>();

    //解析元素
    private void parseElements(Set<? extends Element> elements) {

        TypeElement activityType = elementUtils.getTypeElement(Constants.ACTIVITY);
        TypeMirror activityMirror = activityType.asType();

        for (Element element : elements) {
            //获取每个元素的类信息
            TypeMirror elementMirror = element.asType();
            messager.printMessage(Diagnostic.Kind.NOTE, "遍历的元素信息为：" + elementMirror.toString());
            Router router = element.getAnnotation(Router.class);
            RouterBean routerBean = new RouterBean.Builder().setGroup(router.group()).setPath(router.path()).setElement(element).build();
            if (typeUtils.isSubtype(elementMirror, activityMirror)) {
                routerBean.setType(RouterBean.Type.ACTIVITY);
            } else {
                throw new RuntimeException("@Router注解目前仅限用于Activity之上");
            }

            valueOfPathMap(routerBean);
        }

        TypeElement groupLoadType = elementUtils.getTypeElement(Constants.ROUTER_GROUP);
        TypeElement pathLoadType = elementUtils.getTypeElement(Constants.ROUTER_PATH);
        /**
         * 1、生成路由的详细Path类文件；如Router$$Path$$app
         */
        createPathFile(pathLoadType);
        /**
         * 2、生成路由组Group类文件(没有Path类文件，取不到)，如Router$$Group$$app
         */
        createGroupFile(groupLoadType, pathLoadType);
    }

    private void createGroupFile(TypeElement groupLoadType, TypeElement pathLoadType) {
        if (EmptyUtils.isEmpty(tempGroupmap) || EmptyUtils.isEmpty(tempPathMap)) return;
        //方法的返回值 Map<String, Class<? extends RouterLoadPath>>
        TypeName methodReturns = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathLoadType))));

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.GROUP_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(methodReturns);

        methodBuilder.addStatement("$T<$T,$T> $N = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathLoadType))),
                Constants.GROUP_PARAMETER_NAME,
                HashMap.class);

        // 遍历分组，每个分组创建一个路径类文件，如Router$$Group$$order
        for (Map.Entry<String, String> entry : tempGroupmap.entrySet()) {
            // Map<String, RouterBean> pathMap = new HashMap<>();

            /**
             *    groupMap.put("order",Router$$Path$$order.class);
             */
            methodBuilder.addStatement("$N.put($S,$T.class)",
                    Constants.GROUP_PARAMETER_NAME,
                    entry.getKey(),
                    ClassName.get(packageNameForAPT, entry.getValue()));
        }


            methodBuilder.addStatement("return $N",Constants.GROUP_PARAMETER_NAME);
            //生成类文件，如：Router$$Group$$order
            String finalClassName = Constants.GROUP_FILE_NAME+moduleName;
            messager.printMessage(Diagnostic.Kind.NOTE,"APT生成路由组Group类文件为"+packageNameForAPT+"."+finalClassName);

            try {
                JavaFile.builder(packageNameForAPT, TypeSpec.classBuilder(finalClassName)
                                .addSuperinterface(ClassName.get(groupLoadType))
                                .addModifiers(Modifier.PUBLIC)
                                .addMethod(methodBuilder.build())
                                .build())
                        .build()
                        .writeTo(filer);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    private void createPathFile(TypeElement pathLoadType) {
        if (EmptyUtils.isEmpty(tempPathMap)) return;
        //方法的返回值 Map<String, RouterBean>
        TypeName methodReturns = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(RouterBean.class));
        // 遍历分组，每个分组创建一个路径类文件，如Router$$Path$$order
        for (Map.Entry<String, List<RouterBean>> entry : tempPathMap.entrySet()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.PATH_METHOD_NAME).addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(methodReturns);

            // Map<String, RouterBean> pathMap = new HashMap<>();
            methodBuilder.addStatement("$T<$T,$T> $N = new $T<>()",
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ClassName.get(RouterBean.class),
                    Constants.PATH_PARAMETER_NAME,
                    HashMap.class);
            List<RouterBean> pathList = entry.getValue();
            for (RouterBean bean : pathList) {
                /**
                 *     pathMap.put("/order/OrderActivity",
                 *     RouterBean.create(RouterBean.Type.ACTIVITY,
                 *     OrderActivity.class, "/order/OrderActivity", "order"));
                 *
                 */
                methodBuilder.addStatement("$N.put($S,$T.create($T.$L,$T.class,$S,$S))",
                        Constants.PATH_PARAMETER_NAME,
                        bean.getPath(),
                        ClassName.get(RouterBean.class),
                        ClassName.get(RouterBean.Type.class),
                        bean.getType(),
                        ClassName.get((TypeElement) bean.getElement()),
                        bean.getPath(),
                        bean.getGroup());
            }


                methodBuilder.addStatement("return $N",Constants.PATH_PARAMETER_NAME);
                //生成类文件，如：Router$$Path$$order
                String finalClassName = Constants.PATH_FILE_NAME+entry.getKey();
                messager.printMessage(Diagnostic.Kind.NOTE,"APT生成路由Path类文件为"+packageNameForAPT+"."+finalClassName);

                try {
                    JavaFile.builder(packageNameForAPT, TypeSpec.classBuilder(finalClassName)
                                .addSuperinterface(ClassName.get(pathLoadType))
                                .addModifiers(Modifier.PUBLIC)
                                .addMethod(methodBuilder.build())
                                .build())
                            .build()
                            .writeTo(filer);

                    tempGroupmap.put(entry.getKey(),finalClassName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }


    private void valueOfPathMap(RouterBean bean) {
        if (checkRouterpath(bean)) {
            List<RouterBean> routerBeans = tempPathMap.get(bean.getGroup());
            if (EmptyUtils.isEmpty(routerBeans)) {
                routerBeans = new ArrayList<>();
                routerBeans.add(bean);
                tempPathMap.put(bean.getGroup(), routerBeans);
            } else {
                routerBeans.add(bean);
            }
        } else {
            messager.printMessage(Diagnostic.Kind.ERROR, "@Router注解未按规范，如：/app/MainActivity");
        }
    }

    private boolean checkRouterpath(RouterBean routerBean) {
        String group = routerBean.getGroup();
        String path = routerBean.getPath();
        if (EmptyUtils.isEmpty(path) || !path.startsWith("/")) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@Router注解未按规范，如：/app/MainActivity");
            return false;
        }

        if (EmptyUtils.isEmpty(group)) {
            try {
                String defaultGroup = path.substring(1, path.indexOf("/", 1));
                if (EmptyUtils.isEmpty(defaultGroup)) {
                    return false;
                }
                routerBean.setGroup(defaultGroup);
                return true;
            } catch (Exception e) {
                messager.printMessage(Diagnostic.Kind.NOTE, "获取group失败" + e.getMessage());
                return false;
            }
        }
        return true;
    }
}
