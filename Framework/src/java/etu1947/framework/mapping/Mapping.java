/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.mapping;

/**
 *
 * @author layah
 */
public class Mapping {
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
    
    public Mapping(String nom, String method){
        this.setclassName(nom);
        this.setMethod(method);
    }
}