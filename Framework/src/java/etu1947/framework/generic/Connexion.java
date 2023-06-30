package etu1947.framework.generic;

import java.sql.*;

public class Connexion {
    String Username;
    String Password;
    String DatabaseName;

    public String getUsername(){return this.Username;}
    public String getPassword(){return this.Password;}
    public String getDatabaseName(){return this.DatabaseName;}

    public void setUsername(String name){
        this.Username=name;
    }
    public void setPassword(String pwd){
        this.Password=pwd;
    }
    public void setDatabaseName(String database){
        this.DatabaseName=database;
    }

    public Connexion(String name, String mdp, String databaseName){
        this.setUsername(name);
        this.setPassword(mdp);
        this.setDatabaseName(databaseName);
    }

    public Connection EtablirConnexion(){
        Connection con=null;
        try{
            Class.forName("org.postgresql.Driver");    
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+this.getDatabaseName(),this.getUsername(),this.getPassword());
        }catch(Exception e){
            System.out.println(e);
        }
        return con;
    }

}
