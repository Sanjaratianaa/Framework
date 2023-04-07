/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.models;

import javax.print.attribute.standard.MediaPrintableArea;

import etu1947.framework.annotations.ModelTable;
import etu1947.framework.annotations.Url;
import etu1947.framework.utile.ModelView;

/**
 *
 * @author layah
 */
@ModelTable
public class ClasseTest {
    
    @Url(value = "getThis")
    public ModelView getMot(){
        ModelView model = new ModelView("test.jsp");
        String[] listes = new String[3];
        listes[0] = "Poire";
        listes[1] = "Pomme";
        listes[2] = "Orange";
        model.addItem("fruits", listes);
        return model;
    }
}