package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

//import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText email, password;
    TextView register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        Button login = findViewById(R.id.loginButton);
        register=findViewById(R.id.textRegister);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(view -> {
            String txt_email=email.getText().toString();
            String txt_password=password.getText().toString();
            loginUser(txt_email,txt_password);
        });
        register.setOnClickListener(view -> startActivity(new Intent(Login.this, Register.class)));

    }

    private void loginUser(String txt_email, String txt_password) {
        mAuth.signInWithEmailAndPassword(txt_email,txt_password).addOnSuccessListener(authResult -> {
            Toast.makeText(Login.this,"Success Login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        });
    }
}