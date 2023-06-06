/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.utile;

import etu1947.framework.annotations.Url;
import etu1947.framework.annotations.ParameterNames;
import etu1947.framework.mapping.Mapping;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
//            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    if(child.isFile()){
                        String className = packageName + "." + child.getName().split("\\.")[0];
                        classes.add(Class.forName(className));
                        System.out.println(className);
                        getMethods(Class.forName(className), urlMapping);
                    }
                    classes.addAll(getClasses(packageName + "." + child.getName().split("\\.")[0], urlMapping));
//                }
            } 
        }
        return classes;
    }
    
    
    /**
     * also this function will be called above in order to : 
     * scan the classes and in the meantime scan the methods in it 
     * @param klass is the class that will have the methods to retrieve
     * @param urlMapping is the HashMap object that will be filled as requested by Mr Naina
     * @param out just a printer object in order to test
     */
    public static void getMethods(Class<?> klass, HashMap<String, Mapping> urlMapping){
        for(Method method : klass.getDeclaredMethods()){
            if(method.isAnnotationPresent(Url.class)){
                Url annotation = method.getAnnotation(Url.class);
                String url  = annotation.value();
                Mapping map = new Mapping(klass.getName(), method.getName());
                urlMapping.put(url, map);
            }
        }        
    }

    public Object[] resultatMethode(Object objet, Method methodePrincipale,Object[] valeurs){

        System.out.println("isany valeur :"+valeurs.length);

        Object[] arguments = null;
        try {
            Method[] methods = objet.getClass().getDeclaredMethods();

            for (Method method : methods) {

                if(method.getName().equals(methodePrincipale.getName())){

                    Class<?>[] parameterTypes = method.getParameterTypes();

                    if (parameterTypes.length > 0) {
                        arguments = new Object[parameterTypes.length];
                        System.out.println(method.getName());

                        for (int i = 0; i < parameterTypes.length; i++) {

                            arguments[i] = this.castenation(parameterTypes[i], valeurs[i]);

                        }

                    }
                }
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arguments;
    }

    public Class<?>[] lesParameterTypes(Object objet, Method methodePrincipale){
        Class<?>[] parameterTypes = null;
        try {
            Method[] methods = objet.getClass().getDeclaredMethods();

            for (Method method : methods) {

                if(method.getName().equals(methodePrincipale.getName())){
                    System.out.println("methodePrincipale: "+methodePrincipale.getName());
                    System.out.println("parameterTypes: "+method.getParameterTypes().length);
                    parameterTypes = method.getParameterTypes();
                }
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameterTypes;
    }

    public List<String> lesAnnotations(Object objet, Method methodePrincipale){
        List<String> arguments = new ArrayList<>();
        try {
            Method[] methods = objet.getClass().getDeclaredMethods();

            for (Method method : methods) {

                if(method.getName().equals(methodePrincipale.getName())){
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Annotation[][] annotations = method.getParameterAnnotations();

                    if (parameterTypes.length > 0) {

                        for (int i = 0; i < parameterTypes.length; i++) {
                            Annotation[] parametersAnnot = annotations[i];
                            for(Annotation annotation : parametersAnnot){
                                String[] names = ((ParameterNames)annotation).value();
                                arguments.addAll(Arrays.asList(names));
                            }
                        }
                    }
                }
                
            }

        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return arguments;
    }

    public Object castenation(Class<?> classe, Object objet) throws Exception {
        Object result = null;
        if(classe.getName().contains("[")){

            String[] values = (String[])objet;

            // si double[]
            if(classe.getName().equals("[D")){
                double[] resultats = new double[values.length];
                for (int l=0; l<values.length; l++) {
                    resultats[l] = Double.parseDouble(values[l]);
                }

                result = resultats;
            
            // si int[]
            }else if(classe.getName().equals("[I")){

                int[] resultats = new int[values.length];
                for (int l=0; l<values.length; l++) {
                    resultats[l] = Integer.parseInt(values[l]);
                }

                result = resultats;
            
            // si Double[]
            }else if(classe.getName().equals("[Ljava.lang.Double")){

                Double[] resultats = new Double[values.length];
                for (int l=0; l<values.length; l++) {
                    resultats[l] = Double.parseDouble(values[l]);
                }

                result = resultats;

            // si String[]
            }else if(classe.getName().equals("[Ljava.lang.String")){
                result = values;
            
            // si Integer[]
            }else if(classe.getName().equals("[Ljava.lang.Integer")){
                Integer[] resultats = new Integer[values.length];
                for (int l=0; l<values.length; l++) {
                    resultats[l] = Integer.parseInt(values[l]);
                }

                result = resultats;
            }

        }else{

            if(classe.getName().equals("int")){
                result =  Integer.parseInt(objet.toString());
            }else if(classe.getName().equals("double")){
                result = Double.parseDouble(objet.toString());
            }else if(classe.getName().equals("String")){
                result = objet.toString();
            }

        }
        
        return result;
    }
}