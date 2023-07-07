/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.models;

import etu1947.framework.annotations.ModelTable;
import etu1947.framework.annotations.RestAPI;
import etu1947.framework.annotations.Session;
import etu1947.framework.annotations.Url;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import etu1947.framework.annotations.Auth;
import etu1947.framework.utile.ModelView;

/**
 *
 * @author layah
 */
@ModelTable
public class ClasseTest {

    HashMap<String, Object> SessionsAffichage;

    public void setSessionsAffichage(HashMap<String, Object> sessions){
        this.SessionsAffichage = sessions;
    }
    public HashMap<String, Object> getSessionsAffichage(){
        return this.SessionsAffichage;
    }

    
    @Url(value = "getThis.do")
    @Auth( Profil = "")
    public ModelView getMot(){
        ModelView model = new ModelView("test.jsp");
        String[] listes = new String[3];
        listes[0] = "Poire";
        listes[1] = "Pomme";
        listes[2] = "Orange";
        model.addItem("fruits", listes);
        
        List<String> removableSession = new ArrayList<>();
        removableSession.add("personne");
        model.setRemoveSession(removableSession);

        return model;
    }

    @Url(value = "insertImage.do")
    @Auth( Profil = "admin")
    public ModelView insertImage(){
        ModelView model = new ModelView("imageTest.jsp");
        return model;
    }

    @Url(value = "showLogin.do")
    public ModelView showLogin(){
        ModelView model = new ModelView("loginPage.jsp");
        return model;
    }

    @Url(value = "testJson.do")
    public ModelView testJson(){
        ModelView model = new ModelView("affichage.jsp");
        String[] listes = new String[3];
        listes[0] = "Poire";
        listes[1] = "Pomme";
        listes[2] = "Orange";
        model.addItem("fruits", listes);
        model.addItem("personne", "Layah");
        model.addItem("etu", "1947");
        model.setIsJson(true);
        return model;
    }

    @Url(value = "testJson2.do")
    @RestAPI
    public String testJson2(){
        return "test Json 2";
    }

    @Url(value = "testInvalidate.do")
    public ModelView testInvalidateSession(){
        ModelView model = new ModelView("affichage.jsp");
        model.setInvalidateSession(true);
        return model;
    }

    @Url(value = "getSessionForMe.do")
    @Session
    @Auth( Profil = "")
    public ModelView testGetSessionForMe(){
        ModelView model = new ModelView("affichage.jsp");
        for (String key : this.getSessionsAffichage().keySet()) {
            Object value = this.getSessionsAffichage().get(key);
            System.out.println(key + " : "+value);
        }
        return model;
    }

    // @Url(value = "printSession.do")
    // @Auth( Profil = "")
    // public void printSession(){
    //     for(Entry<String, Object> mEntry: this.getSessionsAffichage().entrySet()){
    //         System.out.println("affichage ty: "+ mEntry.getValue());
    //     } 
    // }

}