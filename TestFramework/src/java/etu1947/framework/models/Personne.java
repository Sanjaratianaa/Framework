/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.models;

import java.lang.reflect.Field;
import java.util.Vector;

import etu1947.framework.annotations.ModelTable;
import etu1947.framework.annotations.Url;
import etu1947.framework.utile.ModelView;

/**
 *
 * @author layah
 */
@ModelTable
public class Personne{
    int idPersonne;
    String Nom;
    String Prenom;
    double Age;

    public void setidPersonne(int id){
        this.idPersonne = id;
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

    public int getidPersonne(){
        return this.idPersonne;
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

    @Url(value="InsertionPersonne")
    public ModelView Insertion(){
        ModelView model = new ModelView("test1.jsp");
        Vector<String> datas = new Vector<>();

        try {

            Field[] fields = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                datas.add(this.getClass().getDeclaredMethod("get"+fields[i].getName()).invoke(this).toString());
            }
            model.addItem("donnees", datas);

        } catch (Exception e) {
            model.addItem("exeption", e);
        }
        
        return model;
    }
    
}