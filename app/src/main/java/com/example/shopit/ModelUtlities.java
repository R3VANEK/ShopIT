package com.example.shopit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface ModelUtlities {



    static String createSchema(String TABLE_NAME, LinkedHashMap<String, String> COLUMN_NAMES) {
        String tableName;
        LinkedHashMap<String, String> columnNames;

        String output = "CREATE TABLE " + TABLE_NAME + "(";
        for (Map.Entry<String, String> set : COLUMN_NAMES.entrySet()) {
            output += set.getKey() + " " + set.getValue();
        }
        output = output.substring(0,output.length()-1);
        output+=")";
        return output;
    }



    static String dropSchema(String TABLE_NAME){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    static String test(Class<? extends Model> c1) throws NoSuchFieldException, IllegalAccessException {
      return (String) c1.getDeclaredField("TABLE_NAME").get(null);
    }

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

    void insertToDB();


}
