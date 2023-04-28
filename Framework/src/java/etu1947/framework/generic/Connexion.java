package etu1947.framework.generic;

import java.sql.*;

public class Connexion {
    String Username;
    String Password;

    public String getUsername(){return this.Username;}
    public String getPassword(){return this.Password;}

    public void setUsername(String name){
        this.Username=name;
    }
    public void setPassword(String pwd){
        this.Password=pwd;
    }

    public Connexion(String name, String mdp){
        this.setUsername(name);
        this.setPassword(mdp);
    }

    public Connection EtablirConnexion(){
        Connection con=null;
        try{
            Class.forName("org.postgresql.Driver");    
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/framework",this.getUsername(),this.getPassword());
        }catch(Exception e){
            System.out.println(e);
        }
        return con;
    }

}
