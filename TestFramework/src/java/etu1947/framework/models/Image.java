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
import etu1947.framework.utile.Fichiers;
import etu1947.framework.utile.ModelView;
// import jakarta.servlet.annotation.MultipartConfig;

/**
 *
 * @author layah
 */
@ModelTable
// @MultipartConfig
public class Image{
    Fichiers Fichier;

    public void setFichier(Fichiers fichier){
        this.Fichier = fichier;
    }

    public Fichiers getFichier(){
        return this.Fichier;
    }

    public Image(){

    }

    @Url(value = "getImage.do")
    public ModelView affichageImage() {
        ModelView model = new ModelView("affichageTest.jsp");
        Vector<Object> datas = new Vector<>();

        try {

            Field[] fields = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                datas.add(this.getClass().getDeclaredMethod("get"+fields[i].getName()).invoke(this));
            }

            model.addItem("images", datas);

        } catch (Exception e) {
            model.addItem("exeption", e);
        }

        return model;
    }
    
}