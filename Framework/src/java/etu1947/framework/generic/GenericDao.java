package etu1947.framework.generic;

import java.sql.*;
import java.util.ArrayList;
import java.lang.reflect.*;

public class GenericDao{

//METHODS
    public void save(Connection con,Object obj) throws Exception{
        String query = "INSERT INTO " + DaoHelper.getTableName(obj.getClass().getName()) + " VALUES(default,";
        String[] typeName = DaoHelper.getTypeName(obj); 
        Method[] getters = DaoHelper.getGetters(obj);

        for (int i = 1; i < typeName.length; i++) {
            if (typeName[i].equals("java.lang.String")) {
                query += "'" + getters[i].invoke(obj,(Object[]) null) + "'";
            } else if (typeName[i].equals("java.util.Date") || typeName[i].equals("java.sql.Date")) {
                query += "TO_DATE('" + getters[i].invoke(obj, (Object[]) null) + "','YYYY-MM-DD')";
            } else if (typeName[i].equals("double")|| typeName[i].equals("int")) {
                query += "" + getters[i].invoke(obj, (Object[]) null);
            }else if (typeName[i].equals("java.time.LocalTime")) {
                query += "'" + getters[i].invoke(obj, (Object[]) null)+"'";
            }
            if(i < typeName.length-1){
                query += ","; 
            }
        }
        query += ")";
        Statement stmt =  con.createStatement();
        stmt.executeUpdate(query);
    }

}
