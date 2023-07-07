/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.models;

import java.lang.reflect.Field;
import java.util.Vector;

import etu1947.framework.annotations.ModelTable;
import etu1947.framework.annotations.ParameterNames;
import etu1947.framework.annotations.Url;
import etu1947.framework.generic.Connexion;
import etu1947.framework.generic.GenericDao;
import etu1947.framework.utile.Fichiers;
import etu1947.framework.utile.ModelView;

/**
 *
 * @author layah
 */
@ModelTable
public class Personne{
    int IdPersonne;
    String Nom;
    String Prenom;
    double Age;

    public void setIdPersonne(int id){
        this.IdPersonne = id;
    }
    public void setNom(String nom){
        this.Nom = nom;
    }
    public void setPrenom(String prenom){
        this.Prenom = prenom;
    }
    public void setAge(double age){
        this.Age = age;
    }

    public int getIdPersonne(){
        return this.IdPersonne;
    }
    public String getNom(){
        return this.Nom;
    }
    public String getPrenom(){
        return this.Prenom;
    }
    public double getAge(){
        return this.Age;
    }

    public Personne(){

    }

    @Url(value="InsertionPersonne.do")
    public ModelView Insertion(@ParameterNames({"un","deux","numbers[]"}) double un,double deux, int[] numbers){
        ModelView model = new ModelView("test1.jsp");
        Vector<String> datas = new Vector<>();
        GenericDao dao = new GenericDao();
        Connexion connexion = new Connexion("layah","layah2003","framework");

        try {

            dao.save(connexion.EtablirConnexion(), this);
            Field[] fields = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                datas.add(this.getClass().getDeclaredMethod("get"+fields[i].getName()).invoke(this).toString());
            }
            double res = un+deux;
            datas.add(""+res);
            model.addItem("donnees", datas);
            model.addItem("numbers", numbers);

        } catch (Exception e) {
            // model.addItem("exeption", e);
            System.out.println(e.getLocalizedMessage());
        }
        
        return model;
    }

    @Url(value = "getImage.do")
    public ModelView affichageImage() {
        ModelView model = new ModelView("affichageTest.jsp");
        return model;
    }
    
}