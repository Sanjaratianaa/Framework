package etu1947.framework.utile;

import java.util.HashMap;

public class ModelView {
    String vue;
    HashMap<String,Object> data = new HashMap<>();

    public void setVue(String v){
        this.vue = v;
    }
    public void setData(HashMap<String,Object> datas){
        this.data = datas;
    }

    public String getVue(){
        return vue;
    }
    public HashMap<String,Object> getData(){
        return data;
    }

    public ModelView(String vue){
        this.setVue(vue);
    }

    public void addItem(String nom, Object objet){
        this.getData().put(nom, objet);
    }
}
