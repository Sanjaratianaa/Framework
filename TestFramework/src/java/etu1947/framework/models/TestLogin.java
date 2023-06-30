/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.models;

import java.lang.reflect.Field;
import java.util.Vector;

import etu1947.framework.annotations.Auth;
import etu1947.framework.annotations.ModelTable;
import etu1947.framework.annotations.ParameterNames;
import etu1947.framework.annotations.Url;
import etu1947.framework.utile.Fichiers;
import etu1947.framework.utile.ModelView;

/**
 *
 * @author layah
 */
@ModelTable
public class TestLogin{

    @Url(value = "checkLogin")
    public ModelView checkLogin(@ParameterNames({"nom","mdp"}) String nom,String mdp){
        // ClasseTest test = new ClasseTest();
        // System.out.println("mot de passe: "+mdp);
        // if(mdp.equals("1234")){
        //     ModelView model = new ModelView("test.jsp");
        //     model = model.getAnnotatedModelView(test, "getMot");
        //     model.addSession("isConnected", true);
        //     model.addSession("profil", "");
        //     return model;
        // }else if(mdp.equals("0000")){
        //     ModelView model = new ModelView("test.jsp");
        //     model = model.getAnnotatedModelView(test, "getMot");
        //     model.addSession("isConnected", true);
        //     model.addSession("profil", "admin");
        //     return model;
        // }else{
        ModelView model = new ModelView("affichage.jsp");
        model.addSession("isConnected", true);
        model.addSession("profil", "");
        return model;
        // }
    }
    
}