/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.utile;

import etu1947.framework.annotations.Url;
import etu1947.framework.mapping.Mapping;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author layah
 */
public class FonctionsUtile {
    
    public static ArrayList<Class<?>> getClasses(String packageName, HashMap<String, Mapping> urlMapping) throws ClassNotFoundException, IOException, URISyntaxException {
        
        ArrayList<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        path = path.replace("%20", " ");
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.toURI());
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    if(child.isFile()){
                        String className = packageName + "." + child.getName().split("\\.")[0];
                        classes.add(Class.forName(className));
                        System.out.println("coUCOUUUUU : "+className);
                        getMethods(Class.forName(className), urlMapping);
                    }
                    classes.addAll(getClasses(packageName + "." + child.getName().split("\\.")[0], urlMapping));
                }
            } 
        }
        return classes;
    }
    
    
    /**
     * also this function will be called above in order to : 
     * scan the classes and in the meantime scan the methods in it 
     * @param klass is the class that will have the methods to retrieve
     * @param urlMapping is the HashMap object that will be filled as requested by Mr Naina
     */
    public static void getMethods(Class<?> klass, HashMap<String, Mapping> urlMapping){
        for(Method method : klass.getDeclaredMethods()){
            if(method.isAnnotationPresent(Url.class)){
                Url annotation = method.getAnnotation(Url.class);
                String url  = annotation.value();
                Mapping map = new Mapping(klass.getName(), method.getName());
                System.out.println("methodeeee: "+method.getName());
                urlMapping.put(url, map);
            }
        }        
    }
    
    /**
     * also this function will be called above in order to : 
     * cast to an object
     * @param klass is the class that will have the methods to retrieve
     * @param urlMapping is the HashMap object that will be filled as requested by Mr Naina
     */
//    public static Object castClasseToObjetc(HashMap<String, Mapping> urlMapping){
//        Class classe = 
//        return objet;
//    }
}