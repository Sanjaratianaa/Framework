package etu1947.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.*;

import etu1947.framework.mapping.Mapping;
import etu1947.framework.utile.Fichiers;
import etu1947.framework.utile.FonctionsUtile;
import etu1947.framework.utile.ModelView;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.annotation.MultipartConfig;

/**
 *
 * @author Layah
 */
// @WebServlet(name = "FrontServlet", urlPatterns = {"/FrontServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 100
)

public class FrontServlet extends HttpServlet {
    HashMap<String,Mapping> urls = new HashMap<>();
    ArrayList<Class<?>> classes; 

    // setters
    public void setUrls(HashMap<String,Mapping> utilitaire){
        this.urls = utilitaire;
    }
    public void setClasses(ArrayList<Class<?>> utilitaires){
        this.classes = utilitaires;
    }

    // getters
    public HashMap<String,Mapping> getUrls(){
        return this.urls;
    }
    public ArrayList<Class<?>> getClasses(){
        return this.classes;
    }

    @Override
    public void init() throws ServletException {
        String packageName = "etu1947.framework.models";
        try{
            ArrayList<Class<?>> classes =  FonctionsUtile.getClasses(packageName, this.getUrls());
            this.setClasses(classes);
            System.out.println(classes);
            System.out.println(this.getUrls());


        }catch(IOException | ClassNotFoundException | URISyntaxException e){
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try{
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h3>URL : " + request.getRequestURL() +"</h3>");
            out.println("<h3>URI : " + request.getRequestURI() + "</h3>");
            out.println("<p>Info: " + request.getPathInfo() +"?"+request.getQueryString()+ "</p>");

            
            if(request.getQueryString()!= null){
                String mot= request.getQueryString();
                String[] mots = mot.split("&&");
                out.println("<ul>");
                for (String mot1 : mots) {
                    out.println("<li>" + mot1 + "</li>");
                }
                out.println("</ul>");
            }
            
            if(this.getClasses().isEmpty() == false){
                out.println("These are the classes for sprint 3:");
                out.println("<ul>");
                for (Class<?> classe : this.getClasses()) {
                    out.println("<li> this class has the url  >>  " + classe.getName() + "</li>");
                }
                out.println("</ul>");
            }

                String[] all = request.getRequestURI().split("/");
                out.print("<p>"+this.getUrls().get(all[all.length-1]).getMethod()+"</p>");
                
                Dispatcher(request,response, out);
                
            out.println("</body>");
            out.println("</html>");
        }catch(Exception e){
            e.printStackTrace(out);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }

    public void Dispatcher(HttpServletRequest request,HttpServletResponse response, PrintWriter out) throws ServletException, Exception, IOException{
        String[] all = request.getRequestURI().split("/");
        if(this.getUrls().containsKey(all[all.length-1]) == true){
            try {
                Mapping mapping = this.getUrls().get(all[all.length-1]);

                // out.println("<p>"+mapping.getclassName()+" et "+mapping.getMethod()+"</p>");

                Class classe = Class.forName(mapping.getclassName());
                Object o = classe.newInstance();
                // out.println("<p> Non "+o+"</p>");
                Method[] methodes = o.getClass().getDeclaredMethods();
                for(Method methode: methodes){

                    if(methode.getReturnType().toString().contains("ModelView") && methode.getName().equals(mapping.getMethod())){
                        System.out.println("ModelViewwww: "+methode.getName()+" and "+mapping.getMethod());
                        // out.println("ModelViewwww: "+methode.getName()+" and "+mapping.getMethod());

                        insertion(o, request,out);

                        FonctionsUtile utiles = new FonctionsUtile();
                        Object[] valeurs = this.prendreArgumentFonction(o,utiles,methode,request);
                        Object[] resultMethod = utiles.resultatMethode(o, methode,valeurs);
                        Class<?>[] parameterTypes = utiles.lesParameterTypes(o, methode);
                        System.out.println("eto aki eeeee: "+parameterTypes.length);

                        Method methodeTest = o.getClass().getDeclaredMethod(mapping.getMethod(),parameterTypes);
                        // System.out.println("eto aki eeeee: "+methodeTest.invoke(o,resultMethod));
                        ModelView objet = (ModelView)methodeTest.invoke(o,resultMethod);
                        
                        PourLesSetAttributes(request, objet);
                        // HashMap<String, Object> images = this.forImage(request, response, out);
                        // out.println("Vue: "+objet.getVue());
                        
                        request.getRequestDispatcher(objet.getVue()).forward(request,response);
                    }else{
                        out.println("<p>Nonnnnn tsy mety</p>");
                    }
                }
                
            } catch (Exception e) {
                throw e;
            }    
        }
    }

    public void PourLesSetAttributes(HttpServletRequest request, ModelView view) throws ServletException, IOException {
        try {
            HashMap<String, Object> hashs = view.getData();
            for(Map.Entry mEntry: hashs.entrySet()){
                request.setAttribute(mEntry.getKey().toString(),mEntry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<Boolean> PourLesFormulaires(HttpServletRequest request) throws Exception {
        Vector<Boolean> all = new Vector<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            if(paramValue != null){
                all.add(true);
            }else{
                all.add(false);
            }
        }
        return all;
    }

    public void insertion(Object objet, HttpServletRequest request, PrintWriter out) throws Exception {
        out.println("miditra insertion");
        try {
            Field[] fields = objet.getClass().getDeclaredFields();
            Fichiers fichier = new Fichiers();

            for (int i = 0; i < fields.length; i++) {
                System.out.println(i+" = "+fields[i].getType().getName());
                fields[i].setAccessible(true);
                if(fields[i].getType().getName().equals("int")){
                    fields[i].set(objet, Integer.parseInt(request.getParameter(fields[i].getName())));
                }else if(fields[i].getType().getName().equals("double")){
                    fields[i].set(objet, Double.parseDouble(request.getParameter(fields[i].getName())));
                }else if(fields[i].getType().getName().contains("Fichiers")){
                    System.out.println("Coucouuu Layahhhhhhhh ato aaaaaa");
                    fichier = fichier.forImage(request, fields[i].getName());
                    fields[i].set(objet, fichier);
                }else{
                    fields[i].set(objet, request.getParameter(fields[i].getName()));
                }
            }

            for (int i = 0; i < fields.length; i++) {
                out.println(fields[i].getName()+" : "+objet.getClass().getDeclaredMethod("get"+fields[i].getName()).invoke(objet));
            }

        } catch (Exception e) {
            out.println(e.getLocalizedMessage());
        }
        
    }

    // Get all argument of a function and call ArgumentTableau function
    public Object[] prendreArgumentFonction(Object objet,FonctionsUtile utile,Method methodePrincipale, HttpServletRequest request) throws Exception {
        Vector<Object> objets = new Vector<Object>();
        try {
            List<String> annotationsList = utile.lesAnnotations(objet, methodePrincipale);

            Enumeration<String> allparameterServices = request.getParameterNames();

            for(int i=0; i<annotationsList.size(); i++){

                if(annotationsList.get(i).contains("[]") == false){

                    objets.add(request.getParameter(annotationsList.get(i)));

                }else if(annotationsList.get(i).contains("[]")){
                    String[] allParam = request.getParameterValues(annotationsList.get(i));
                    Object temp = Array.newInstance(allParam[0].getClass() , allParam.length);

                    for(int j = 0 ; j < allParam.length ; j++){
                        Array.set( temp , j , allParam[j]);
                    }
                    objets.add(temp);
                }
            }

        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        return objets.toArray();
    }

}

