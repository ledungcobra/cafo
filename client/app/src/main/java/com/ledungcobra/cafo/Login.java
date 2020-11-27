package com.ledungcobra.cafo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    MaterialButton buttonSignIn;
    MaterialButton buttonCreateAccount;
    TextInputEditText textFieldFullName;
    TextInputEditText textFieldUsername;
    TextInputEditText textFieldEmail;
    TextInputEditText textFieldPassword;
    TextInputEditText textFieldConfirmPassword;
    RadioGroup radioGroupLoginAs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initial
        bindView();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,
                        radioGroupLoginAs.getCheckedRadioButtonId() == R.id.radioCustomer?
                                MainActivity.class:DriverScreen.class));
            }
        });

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this,
                        radioGroupLoginAs.getCheckedRadioButtonId() == R.id.radioCustomer?
                                MainActivity.class:DriverScreen.class));

            }
        });



    }
    private void bindView(){
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        textFieldFullName = findViewById(R.id.textFieldFullName);
        textFieldConfirmPassword = findViewById(R.id.textFieldConfirmPassword);
        textFieldUsername = findViewById(R.id.textFieldUsername);
        textFieldEmail = findViewById(R.id.textFieldEmail);
        textFieldPassword = findViewById(R.id.textFieldPassword);
        radioGroupLoginAs = findViewById(R.id.radioGroupLoginAs);
    }

}