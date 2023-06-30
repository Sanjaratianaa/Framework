package etu1947.framework.utile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelView {
    String vue;
    HashMap<String,Object> data = new HashMap<>();
    HashMap<String,Object> sessions = new HashMap<>();
    List<Annotation> annotations;
    Boolean invalidateSession = false;
    List<String> removeSession;
    Boolean isJson;

    public void setVue(String v){
        this.vue = v;
    }
    public void setData(HashMap<String,Object> datas){
        this.data = datas;
    }
    public void setRemoveSession(List<String> removeSession){
        this.removeSession = removeSession;
    }
    public void setIsJson(Boolean value){
        this.isJson = value;
    }

    public String getVue(){
        return vue;
    }
    public HashMap<String,Object> getData(){
        return data;
    }
    public HashMap<String,Object> getSessions(){
        return sessions;
    }
    public List<Annotation> getAnnotations(){
        return annotations;
    }
    public List<String> getRemoveSession(){
        return this.removeSession;
    }
    public Boolean getIsJson(){
        return this.isJson;
    }

    public ModelView(String vue){
        this.setVue(vue);
        this.annotations = new ArrayList<>();
    }

    public ModelView(){
        
    }

    public void addItem(String nom, Object objet){
        this.getData().put(nom, objet);
    }

    public void addSession(String nom, Object objet){
        this.getSessions().put(nom, objet);
    }

    public ModelView getAnnotatedModelView(Object obj, String methodName) {
        try {
            Method method = obj.getClass().getMethod(methodName);
            Annotation[] annotationss = method.getDeclaredAnnotations();
            ModelView model = (ModelView) method.invoke(obj);
            for (Annotation annotation : annotationss) {
                model.addAnnotation(annotation);
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void addAnnotation(Annotation annotation) {
        System.out.println(annotation.toString());
        this.annotations.add(annotation);
    }
}
