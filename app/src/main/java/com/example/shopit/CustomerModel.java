package com.example.shopit;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Patterns;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomerModel extends Model implements EmailUtilities{


    public static final String TABLE = "customers";
    public static final LinkedHashMap<String, String> COLUMNS = new LinkedHashMap<String, String>() {{
        put("id", "INTEGER PRIMARY KEY NOT NULL,");
        put("login", "VARCHAR(50) NOT NULL UNIQUE,");
        put("email", "TEXT NOT NULL UNIQUE,");
        put("password", "VARCHAR(11) NOT NULL UNIQUE,");
    }};

    public static final String MyPREFERENCES = "MyPrefs";

    private String login;
    private String email;
    private String password;
    private String passwordHash;

    private Context context;



    public CustomerModel(Context context){
        this.context = context;
    }

    public CustomerModel(Context context, String name, String email, String password) throws NoSuchAlgorithmException {

        this.context = context;

        if(name.isEmpty() || email.isEmpty() || password.isEmpty())
            throw new Error("Nie podano któregoś z parametrów");

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            throw new Error("Wpisano niepoprawny adres e-mail");

        if(password.length() < 8)
            throw new Error("Podane hasło jest za krótkie");


        passwordHash = HashCreator.createMD5Hash(password);

    }





    public boolean loginDB(String login, String passwordHash){

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT count(*) FROM "+TABLE + " WHERE login = ? AND password = ?", new String[] {login, passwordHash});
        String count = null;
        if (c.moveToFirst()){
            do {
                count = c.getString(0);
            } while(c.moveToNext());
        }
        c.close();
        db.close();


        if(!count.isEmpty() && count.contains("1"))
            return true;
        else
            return false;
    }

    // uzywany przy rejestracji
    @Override
    void insertToDB() throws java.sql.SQLException {

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("login", this.login);
        cv.put("email", this.email);
        cv.put("password", passwordHash);

        long result = db.insert(TABLE, null, cv);
        db.close();


        if(result == -1)
            throw new java.sql.SQLException("Nie udało się zarejestrować użytkownika");

    }




   public void setPasswordHash(String password) throws NoSuchAlgorithmException {
       this.passwordHash = HashCreator.createMD5Hash(password);
   }


    public void savePreferences(){
        SharedPreferences sp = this.context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
        editor.putString("login", this.login);
        editor.putString("passwordHash", this.passwordHash);
        editor.apply();

    }

    public String getPreferedLogin(){
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString("login","");
    }

    public boolean validatePreferences(){
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String preferedLogin = sp.getString("login","");
        String preferedPasswordHash = sp.getString("passwordHash", "");

        if(preferedLogin.isEmpty() || preferedPasswordHash.isEmpty())
            return false;

        boolean status = loginDB(preferedLogin, preferedPasswordHash);

        if(!status)
            deletePreferences();

        return status;

    }



    public void deletePreferences(){
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }







    public void setLogin(String login) {
        this.login = login;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }


}
