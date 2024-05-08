package com.example.router;

import com.example.annotation.TestAPT;

@TestAPT(path = "/app/TestDoc")
public class Doc {
    /**
     *
     *  JavaPoet 8个常用类
     *
     *  MethodSpec  代表一个构造函数或方法声明
     *  TypeSpec     代表一个类、接口或者枚举声明
     *  FieldSpec     代表一个成员变量，一个字段声明
     *  JavaFile        包含一个顶级类的Java文件
     *  ParameterSpec       用来创建参数
     *  AnnotationSpec        用来创建注解
     *  ClassName      用来包装一个类
     *  TypeName      类型，如在添加返回值类型时使用TypeName.VOID
     *
     *
     *  JavaPoet字符串格式化规则
     *
     *  $L  字面量，如 “int value = $L”,10
     *  $S   字符串，如 $S,"hello"
     *  $T   类、接口，如 $T,MainActivity
     *  $N   变量 ，如：user.$N,name
     */
}
