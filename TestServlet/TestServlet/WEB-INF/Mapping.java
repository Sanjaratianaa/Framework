package etu1947.framework;

public class Mapping{
    String className;
    String Method;

    // setters
    public void setclassName(String name){
        this.className = name;
    }
    public void setMethod(String method){
        this.Method = method;
    }

    // getters
    public String getclassName(){
        return className;
    }
    public String getMethod(){
        return Method;
    }
}