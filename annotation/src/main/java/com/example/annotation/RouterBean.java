package com.example.annotation;

import javax.lang.model.element.Element;

public class RouterBean {

    private RouterBean(Type type, Class<?> clazz, String path, String group) {
        this.type = type;
        this.clazz = clazz;
        this.path = path;
        this.group = group;
    }

    public enum Type {
        ACTIVITY
    }

    private RouterBean(Builder builder) {
        this.element = builder.element;
        this.path = builder.path;
        this.group = builder.group;
    }

    public static RouterBean create(Type type, Class<?> clazz, String path, String group) {
        return new RouterBean(type, clazz, path, group);
    }

    //枚举类型
    private Type type;
    // 类节点
    private Element element;
    // 被@Router注解的类对象
    private Class<?> clazz;
    // 路由的组名
    private  String group;
    // 路由的地址
    private  String path;

    public Element getElement() {
        return element;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getGroup() {
        return group;
    }

    public Type getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public final static class Builder {
        //类节点
        private Element element;
        // 路由的组名
        private String group;
        // 路由的地址
        private String path;

        public Builder setElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public RouterBean build() {
            if (path == null || path.length() == 0) {
                throw new IllegalArgumentException("path必填项为空，如：/app/MainActivity");
            }
            return new RouterBean(this);
        }
    }
}
