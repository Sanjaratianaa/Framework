/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.models;

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
        return new ModelView("test.jsp");
    }
}