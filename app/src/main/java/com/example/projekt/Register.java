package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private EditText email, password;
    TextView toLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        Button register = findViewById(R.id.registerButton);
        mAuth = FirebaseAuth.getInstance();
        toLogin=findViewById(R.id.textToLogin);

        register.setOnClickListener(v -> {
            String txtEmail = email.getText().toString();
            String txtPassword = password.getText().toString();

            if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
                Toast.makeText(Register.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
            } else if (txtPassword.length() < 6){
                Toast.makeText(Register.this, "Password too short!", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(txtEmail , txtPassword);
            }
        });
        toLogin.setOnClickListener(view -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });
    }

    private void registerUser( final String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(Register.this,"Register successful",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
            else{
                Toast.makeText(Register.this,"Register no successful",Toast.LENGTH_SHORT).show();
            }
        });
    }
}