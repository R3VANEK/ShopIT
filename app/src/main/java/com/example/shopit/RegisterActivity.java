package com.example.shopit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity {

    EditText loginView, passwordView, emailView;
    Button registerView;

    CustomerModel customerModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginView = findViewById(R.id.loginSet);
        passwordView = findViewById(R.id.passwordSet);
        emailView = findViewById(R.id.emailSet);
        registerView = findViewById(R.id.registerButton);

        customerModel = new CustomerModel(getApplicationContext());



    }


    public void registerUser(View view) throws NoSuchAlgorithmException {

        String enteredLogin = loginView.getText().toString();
        String enteredPassword = passwordView.getText().toString();
        String enteredEmail = emailView.getText().toString();

        if(enteredLogin.isEmpty() || enteredPassword.isEmpty() || enteredEmail.isEmpty())
            Toast.makeText(getApplicationContext(), "Proszę uzupełnić wszystkie pola", Toast.LENGTH_LONG).show();

        customerModel.setLogin(enteredLogin);
        customerModel.setPasswordHash(enteredPassword);
        customerModel.setEmail(enteredEmail);

        try{
            customerModel.insertToDB();
            Toast.makeText(getApplicationContext(), "zarejestrowano użytkownika!", Toast.LENGTH_LONG).show();
            customerModel.savePreferences();
            startActivity(new Intent(this, LoginActivity.class));
//            customerModel.sendRegisterMail(getApplicationContext(), enteredEmail);
        }
        catch(SQLException e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }


}