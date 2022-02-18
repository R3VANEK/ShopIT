package com.example.shopit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    String savedLogin = null;
    String savedPasswordHash = null;
    String savedPreferencesDate = null;

    EditText loginView;
    EditText passwordView;

    CustomerModel customerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        customerModel = new CustomerModel(getApplicationContext());

        boolean isLogged = customerModel.validatePreferences();

        if(isLogged)
            startActivity(new Intent(this, MainActivity.class));


        loginView = findViewById(R.id.login);
        passwordView = findViewById(R.id.password);

    }


    public void loginToApplication(View view) throws NoSuchAlgorithmException {

        String enteredlogin = loginView.getText().toString();
        String enteredPassword = passwordView.getText().toString();

        if(enteredlogin.isEmpty() || enteredPassword.isEmpty()){
            Toast.makeText(getApplicationContext(), "Nie podano loginu lub hasła", Toast.LENGTH_LONG).show();
            return;
        }

        customerModel.setPasswordHash(enteredPassword);
        customerModel.setLogin(enteredlogin);
        boolean isLogged = customerModel.loginDB(customerModel.getLogin(), customerModel.getPasswordHash());

        if(isLogged){
            //savePreferences
            customerModel.savePreferences();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else{
            Toast.makeText(getApplicationContext(), "Podano błędny login lub hasło", Toast.LENGTH_LONG).show();
        }

    }




    public void goToRegister(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}