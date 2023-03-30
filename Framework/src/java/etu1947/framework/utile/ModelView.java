/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1947.framework.utile;

/**
 *
 * @author layah
 */
public class ModelView {
    String vue;
    
    // getters
    public String getVue(){
        return this.vue;
    }
    
    // setters
    public void setVue(String vue){
        this.vue = vue;
    }
    
    public ModelView(String view){
        this.setVue(view);
    }
    
}
