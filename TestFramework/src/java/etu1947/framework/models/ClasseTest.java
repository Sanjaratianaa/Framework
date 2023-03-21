/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.models;

import etu1947.framework.annotations.ModelTable;
import etu1947.framework.annotations.Url;

/**
 *
 * @author layah
 */
@ModelTable
public class ClasseTest {
    
    @Url(value = "getThis")
    public String getMot(){
        return "Coucouu";
    }
}