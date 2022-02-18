package com.example.shopit;


import android.content.ContentValues;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Model {



    static String createSchema(Class<? extends Model> c1) throws NoSuchFieldException, IllegalAccessException {

        String tableName = (String) c1.getDeclaredField("TABLE").get(null);
        LinkedHashMap<String, String> columns = (LinkedHashMap<String, String>) c1.getDeclaredField("COLUMNS").get(null);

        String output = "CREATE TABLE " + tableName + "(";
        for (Map.Entry<String, String> set : columns.entrySet()) {
            output += set.getKey() + " " + set.getValue();
        }
        output = output.substring(0,output.length()-1);
        output+=")";
        return output;

    }

    static String dropSchema(Class<? extends Model> c1) throws NoSuchFieldException, IllegalAccessException {
        String tableName = (String) c1.getDeclaredField("TABLE").get(null);
        return "DROP TABLE IF EXISTS " + tableName;
    }




    abstract void insertToDB() throws SQLException;















    //    void insertToDB(T obj) throws InvocationTargetException, IllegalAccessException {
//            for(Method m : obj.getClass().getDeclaredMethods()) {
//                if(m.getName().contains("get"))
//                    m.invoke(obj);
//            }
//
//    }
//    static String insertToDB(Class<? extends Model> c1) throws InstantiationException, IllegalAccessException {
//        ContentValues cv = new ContentValues();
//        ArrayList<String> test = new ArrayList<>();
//
//
//        for(Field field : c1.getDeclaredFields()){
//
//
//            if(java.lang.reflect.Modifier.isStatic(field.getModifiers()))
//                continue;
//
//            field.setAccessible(true);
//            String fieldType = field.getType().getName();
//
//
//            if(field.getType().getName().contains("String"))
//                cv.put(field.getName(), (String) field.get(c1.newInstance()));
//            else if(field.getType().isPrimitive())
//                cv.put(field.getName(), (fieldType.equals("int") ? (int) field.get(c1.newInstance()) : (double) field.get(c1.newInstance())));
//
//
//
//        }
//
//        return cv.keySet().toString();
//        //do sprawdzenia cv
//
//    };
}
