package etu1947.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import etu1947.framework.mapping.Mapping;
import etu1947.framework.utile.FonctionsUtile;
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
    HashMap<String,Mapping> urls;

    // setters
    public void setUrls(HashMap<String,Mapping> utilitaire){
        this.urls = utilitaire;
    }

    // getters
    public HashMap<String,Mapping> getUrls(){
        return this.urls;
    }

    @Override
    public void init() throws ServletException {
        String packageName = "models";
        
        try{
            FonctionsUtile.getClasses(packageName, this.getUrls());
            
//            Mapping map =  getUrlMapping().get(url);
//            out.println(map.getMethod());
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
            String mot= request.getQueryString();
            String[] mots = mot.split("&&");
            out.println("<ul>");
            for (String mot1 : mots) {
                out.println("<li>" + mot1 + "</li>");
            }
            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");
        }
        
        PrintWriter outt = response.getWriter();
        Mapping map = getUrls().get("getThis");
        outt.println("Coucouuuuuu");
        outt.println("<p> Heree: "+map.getclassName()+"</p>");
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
