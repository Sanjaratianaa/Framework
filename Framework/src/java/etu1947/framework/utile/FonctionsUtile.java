/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.utile;

import etu1947.framework.annotations.Url;
import etu1947.framework.annotations.Auth;
import etu1947.framework.annotations.ParameterNames;
import etu1947.framework.annotations.RestAPI;
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
import java.util.Map;
import java.util.Vector;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 *
 * @author layah
 */
public class FonctionsUtile {
    public static ArrayList<Class<?>> getClasses(String packageName, HashMap<String, Mapping> urlMapping, HashMap<String, Object> listSingletons) throws ClassNotFoundException, IOException, URISyntaxException {
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
                        firstInsertInstance(Class.forName(className), listSingletons);

                    }
                    classes.addAll(getClasses(packageName + "." + child.getName().split("\\.")[0], urlMapping,listSingletons));
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

    public static void firstInsertInstance(Class<?> classe, HashMap<String, Object> singletons){
        singletons.put(classe.getName(), null);
    }

    public Object getInstance(HashMap<String, Object> singletons, String className){
        return singletons.get(className);
    }

    public Object[] resultatMethode(Object objet, Method methodePrincipale,Object[] valeurs, PrintWriter out){

        System.out.println("isany valeur :"+valeurs.length);

        Object[] arguments = null;
        try {
            Method[] methods = objet.getClass().getDeclaredMethods();

            for (Method method : methods) {
                out.println(">> Methode >>");
                if(method.getName().equals(methodePrincipale.getName()) && !method.getName().contains("ession")){
                    out.println(">> Methode Petit>>");

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    out.println("param: "+parameterTypes.length);

                    if (parameterTypes.length > 0) {
                        arguments = new Object[parameterTypes.length];
                        System.out.println(method.getName());

                        for (int i = 0; i < parameterTypes.length; i++) {

                            arguments[i] = this.castenation(parameterTypes[i], valeurs[i]);
                            System.out.println(arguments[i]);

                        }

                    }
                }
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(arguments != null){
            for (int i = 0; i < arguments.length; i++) {
                System.out.println("argument: "+arguments[i]);
            }
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
            System.out.println("coucouuuu");

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
            }else if(classe.getName().contains("String")){
            System.out.println("coucouuuu bb");
                result = objet.toString();
            }

        }
        
        return result;
    }

    public Boolean checkIf(ModelView model, Method method){
        if(method.isAnnotationPresent(Auth.class)){
            return true;
        }else{
            List<Annotation> annotations = model.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().getCanonicalName().equals("etu1947.framework.annotations.Auth")) {
                    return true;
                }
            }
        }
        return false;
    }

    public String changeDataToJson(Object objet,HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        ModelView model = (ModelView)objet;
        String json = "";
        if(model.getIsJson()){
            Gson gson = new Gson();
            json = gson.toJson(model.getData());
            System.out.println("Json: "+json);
        }
        return json;
    }

    public void changeObjectToJson(Object objet,HttpServletResponse response) throws IOException{
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String json = gson.toJson(objet);
            System.out.println("Json Object: "+json);
            out.println("Json Object: "+json);
    }
}