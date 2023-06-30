package etu1947.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.*;
import etu1947.framework.annotations.Auth;
import etu1947.framework.mapping.Mapping;
import etu1947.framework.utile.Fichiers;
import etu1947.framework.utile.FonctionsUtile;
import etu1947.framework.utile.ModelView;
import jakarta.servlet.http.Part;
import java.io.File; 
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
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

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
    HashMap<String,Object> singletons = new HashMap<>();
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
        try {
            ArrayList<Class<?>> classes = FonctionsUtile.getClasses(packageName, this.getUrls(), this.singletons);
            this.setClasses(classes);
        } catch(IOException | ClassNotFoundException | URISyntaxException e) {
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

                Class classe = Class.forName(mapping.getclassName());
                Object o = this.verifieInstance(classe);

                Method[] methodes = o.getClass().getDeclaredMethods();
                for(Method methode: methodes){

                    if(methode.getReturnType().toString().contains("ModelView") && methode.getName().equals(mapping.getMethod())){
                        System.out.println("ModelViewwww: "+methode.getName()+" and "+mapping.getMethod());

                        insertion(o, request,out);

                        FonctionsUtile utiles = new FonctionsUtile();
                        Object[] valeurs = this.prendreArgumentFonction(o,utiles,methode,request);
                        Object[] resultMethod = utiles.resultatMethode(o, methode,valeurs);
                        Class<?>[] parameterTypes = utiles.lesParameterTypes(o, methode);

                        try {

                            Method methodeTest = o.getClass().getDeclaredMethod(mapping.getMethod(),parameterTypes);
                            System.out.println("bg: "+methodeTest.getAnnotation(Auth.class));
                            System.out.println("eto aki eeeee 22: "+methodeTest+" and "+resultMethod);

                            ModelView objet = (ModelView)methodeTest.invoke(o,resultMethod);
                            PourLesSetAttributes(request, objet);

                            HashMap<String, Object> hashs = objet.getSessions();

                            if(!hashs.isEmpty()){
                                for(Map.Entry mEntry: hashs.entrySet()){
                                    (request.getSession(true)).setAttribute(mEntry.getKey().toString(),mEntry.getValue());
                                }
                            }
                            
                            if(utiles.checkIf(objet,methodeTest)){  
                                System.out.println("whyyy");                              
                                System.out.println("Coucouu kosa e: "+this.isAuthentifier(request, objet));
                                if(this.isAuthentifier(request, objet) && this.getProfil(request, methodeTest)){
                                    System.out.println("I'm authentified");
                                    request.getRequestDispatcher(objet.getVue()).forward(request,response);
                                    System.out.println("anaty methode:" + methodeTest.getAnnotation(Auth.class).Profil());
                                    
                                }else{
                                    System.out.println("You're not authentified");
                                }
                            }else{
                                System.out.println("I enter");
                                request.getRequestDispatcher(objet.getVue()).forward(request,response);
                            }

                        } catch (Exception e) {
                            out.println(e.getCause());
                        }
                        
                        
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
            if(fields.length > 0){

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

    public Object verifieInstance(Class classe) throws Exception{
        Object objet = null;
        if(this.singletons.get(classe.getName()) == null){
            objet = classe.newInstance();
            this.singletons.put(classe.getName(), objet);
        }else if(this.singletons.get(classe.getName()) != null){
            objet = this.singletons.get(classe.getName());
            Field[] fields = objet.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if(fields[i].getType().getName().equals("int") || fields[i].getType().getName().equals("double")){
                    fields[i].set(objet, 0);
                }else{
                    fields[i].set(objet, null);
                }
            }
        }
        return objet;
    }


    public Boolean isAuthentifier(HttpServletRequest request, ModelView model) throws ClassNotFoundException, IOException, URISyntaxException  {
        HttpSession session = request.getSession(true);

            String value = getInitParameter("Session-Connexion");
            System.out.println("value: "+value);
            System.out.println(session.getAttribute(value));
            if(session.getAttribute(value).toString().equals("true")){
                System.out.println("okeeee c'est trueee");
                return true;
            }

        return false;
    }


    public Boolean getProfil(HttpServletRequest request, Method method) throws ClassNotFoundException, IOException, URISyntaxException  {
        HttpSession session = request.getSession(true);

            String value = getInitParameter("Session-Profil");
            if(session.getAttribute(value) != null && session.getAttribute(value).toString().equals(method.getAnnotation(Auth.class).Profil())){
                return true;    
            }

        return false;
    }

}

