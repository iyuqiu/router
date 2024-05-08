package com.example.compiler;

import com.example.annotation.Router;
import com.example.annotation.TestAPT;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

//@AutoService(Processor.class)
public class TestProcessor extends AbstractProcessor {

    private Elements elementUtils;// 操作Element工具类
    private Types typeUtils; //type(类信息)工具类
    private Filer filer; //文件生成器
    private Messager messager; //用来输出警告、错误等信息

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(TestAPT.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    /**
     * @param set              使用了支持处理注解的节点集合(类上面写了注解)
     * @param roundEnvironment
     * @return true表示后续处理器不会再处理(已经处理完成)
     * 相关内容可以参考Javapoet
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()) return false;
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Router.class);
        for (Element element : elements) {
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            String className = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "被注解的类" + packageName + "/" + className);

            MethodSpec main = MethodSpec.methodBuilder("main").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(void.class).addParameter(String[].class, "args").addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!").build();

            TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld").addModifiers(Modifier.PUBLIC, Modifier.FINAL).addMethod(main).build();

            JavaFile javaFile = JavaFile.builder(packageName, helloWorld).build();

            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
