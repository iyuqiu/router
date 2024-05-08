package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *  ElementType.TYPE  接口、类、美剧、注解
 *  ElementType.FIELD  属性、枚举的常量
 *  ELementType.METHOD  方法
 *  ELementType.PARAMETER  方法参数
 *  ELementType.PACKAGE  包
 *
 *
 *  生命周期： SOURCE < CLASS < RUNTIME
 *  1、如果需要在运行时去动态获取注解信息，用RUNTIME注解
 *  2、要在编译时进行一些预处理操作，如ButterKnife，用CLASS注解。 注解会在class文件中存在，但是在运行时会被丢弃
 *  3、做一些检查行的操作，如@Override，用SOURCE源码注解，注解仅存在源码级别，在编译时丢掉该注解
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Router {

    String path();
    String group() default "";
}

