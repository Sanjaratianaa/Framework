package etu1947.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.*;
import etu1947.framework.annotations.Auth;
import etu1947.framework.annotations.RestAPI;
import etu1947.framework.annotations.Session;
import etu1947.framework.mapping.Mapping;
import etu1947.framework.utile.Fichiers;
import etu1947.framework.utile.FonctionsUtile;
import etu1947.framework.utile.ModelView;
import jakarta.servlet.http.Part;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionContext;

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
    FonctionsUtile utiles = new FonctionsUtile();

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
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try{

            String[] all = request.getRequestURI().split("/");
            if(this.getUrls().containsKey(all[all.length-1]) == true){
                Mapping mapping = this.getUrls().get(all[all.length-1]);

                Object o = this.verifieInstance(Class.forName(mapping.getclassName()));

                Method[] methodes = o.getClass().getDeclaredMethods();
                for(Method methode: methodes){

                    if(methode.getName().equals(mapping.getMethod())){

                        insertion(o, request,out);

                        Object[] resultMethod = utiles.resultatMethode(o, methode,this.prendreArgumentFonction(o,utiles,methode,request),out);
                        Class<?>[] parameterTypes = utiles.lesParameterTypes(o, methode);

                        Method methodeTest = o.getClass().getDeclaredMethod(mapping.getMethod(),parameterTypes);

                        out.println("methode: "+methodeTest.getName()+"\n");
                        out.println();
                        out.println();
                        GetAllSessionForUser(request,o,methodeTest);
                        System.out.println("je suuis laaaaaaaaaa");

                        if(methodeTest.isAnnotationPresent(RestAPI.class)){
                            Object objet = methodeTest.invoke(o);
                            PourLesSetSessions(request, objet);
                            removeAllSessions(request, objet);
                            utiles.changeObjectToJson(objet,response);
                        }else{
                            Object objet = (ModelView)methodeTest.invoke(o,resultMethod);
                            PourLesSetAttributes(request, objet);
                            PourLesSetSessions(request, objet);
                            removeAllSessions(request, objet);

                            out.println(utiles.changeDataToJson(objet,response));

                            if(utiles.checkIf((ModelView)objet,methodeTest) && isAuthenticated(request, methodeTest, (ModelView)objet)){  

                                System.out.println("I'm authentified");
                                request.getRequestDispatcher(((ModelView)objet).getVue()).forward(request,response);
                                System.out.println("anaty methode:" + methodeTest.getAnnotation(Auth.class).Profil());
                                
                            }else if(!utiles.checkIf((ModelView)objet,methodeTest)){
                                System.out.println("I enter");
                                request.getRequestDispatcher(((ModelView)objet).getVue()).forward(request,response);
                            }

                        }
                    }
                }
            }
                
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

    public void PourLesSetAttributes(HttpServletRequest request, Object objet) throws Exception {
        try {
            Method getDataMethod = objet.getClass().getMethod("getData");
            Object objetData = getDataMethod.invoke(objet);

            if (objetData instanceof HashMap) {
                HashMap<String, Object> hashs = (HashMap<String, Object>) objetData;
                hashs.forEach(request::setAttribute);
            }
        } catch (Exception e) {
            throw new Exception("getData() not found");
        }
    }
    
    public void removeAllSessions(HttpServletRequest request, Object objet) throws Exception {
        try {
            HttpSession sessionContext = request.getSession(false);

            Method getInvalidateSession = objet.getClass().getMethod("getInvalidateSession");
            boolean invalidateSession = (boolean) getInvalidateSession.invoke(objet);

            Method getremoveSession = objet.getClass().getMethod("getRemoveSession");
            Object objetRemove = getremoveSession.invoke(objet);
    
            if (invalidateSession) {
                System.out.println("I enter heree 'cause i need to remove all");
                if (sessionContext != null) {
                    Enumeration<String> sessionIds = sessionContext.getAttributeNames();
                    while (sessionIds.hasMoreElements()) {
                        String sessionId = sessionIds.nextElement();
                        System.out.println(sessionId);
                        sessionContext.removeAttribute(sessionId);
                    }
                }
            }else if((objetRemove instanceof List) && !((List<String>) objetRemove).isEmpty()){
                List<String> removableObject = (List<String>)objetRemove;
                for (String removable : removableObject) {
                    sessionContext.removeAttribute(removable);
                }
            }

        } catch (Exception e) {
            throw new Exception("InvalidateSession is false babyy!");
        }
    }    

    public void PourLesSetSessions(HttpServletRequest request, Object objet) throws Exception {
        try {
            Method getSessionMethod = objet.getClass().getMethod("getSessions");
            Object objetSession = getSessionMethod.invoke(objet);

            if (objetSession instanceof HashMap) {
                HashMap<String, Object> hashs = (HashMap<String, Object>) objetSession;
                hashs.forEach((key, value) -> request.getSession(true).setAttribute(key, value));
            }
        } catch (Exception e) {
            throw new Exception("getSessions() not found");
        }
    }

    public void GetAllSessionForUser(HttpServletRequest request, Object objet, Method methode) throws Exception {
        try {
            System.out.println("De aonaaaaaaaa");
            HttpSession sessionContext = request.getSession(false);
            
            System.out.println("De aonaaaaaaaa");
            if(methode.isAnnotationPresent(Session.class)){

                HashMap<String, Object> sessions = new HashMap<>();
                System.out.println("I'm hereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                Enumeration<String> sessionIds = sessionContext.getAttributeNames();
                while (sessionIds.hasMoreElements()) {
                    String sessionId = sessionIds.nextElement();
                    System.out.println(sessionId);
                    sessions.put(sessionId, sessionContext.getAttribute(sessionId));
                }

                Field[] fields = objet.getClass().getDeclaredFields();
                for (Field field : fields) {
                    System.out.println("field: "+field.getName());
                    field.setAccessible(true);
                    Method setter = objet.getClass().getDeclaredMethod("setSessionsAffichage", field.getType());
                    setter.invoke(objet,sessions);
                    break;
                }
            }
            
        } catch (Exception e) {
            throw new Exception("Ohhh, an error is present when you try to get all the session for the user!\n");
        }
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

    public boolean isAuthenticated(HttpServletRequest request, Method method, ModelView model) {
        HttpSession session = request.getSession(true);
    
        String connexionValue = getInitParameter("Session-Connexion");
        String profilValue = getInitParameter("Session-Profil");
    
        

        Object connexionAttribute = session.getAttribute(connexionValue);
        Object profilAttribute = session.getAttribute(profilValue);

        System.out.println("connexion: "+connexionAttribute);
        System.out.println("profil: "+profilAttribute);
    
        if (connexionAttribute != null && profilAttribute != null) {
            System.out.println("It's true!");
            if (connexionAttribute.toString().equals("true") && profilAttribute.toString().equals(method.getAnnotation(Auth.class).Profil())) {
                return true;
            }
        }
    
        return false;
    }
    

}

