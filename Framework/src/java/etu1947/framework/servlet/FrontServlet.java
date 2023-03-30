package etu1947.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;

import etu1947.framework.mapping.Mapping;
import etu1947.framework.utile.FonctionsUtile;
import etu1947.framework.utile.ModelView;

import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Layah
 */
// @WebServlet(name = "FrontServlet", urlPatterns = {"/FrontServlet"})
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
//        System.out.println(packageName);
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
        try ( PrintWriter out = response.getWriter()) {
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

            if(this.getUrls().containsKey(request.getPathInfo()) == true){
                try {
                    Mapping mapping = this.getUrls().get(request.getPathInfo());
                    Object o = Class.forName(mapping.getclassName());
                    ModelView objet = (ModelView)o.getClass().getDeclaredMethod(mapping.getMethod()).invoke(o,(Object[])null);

                    RequestDispatcher dispat = request.getRequestDispatcher(objet.getVue());
                    dispat.forward(request,response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            
            out.println("</body>");
            out.println("</html>");
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


}
