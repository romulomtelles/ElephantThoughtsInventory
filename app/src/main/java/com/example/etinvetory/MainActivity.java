package com.example.etinvetory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button btnSignIn;
    private EditText etEmail, etPassword;
    private ProgressBar mainProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        btnSignIn = findViewById(R.id.btnSignIn);
        etEmail = findViewById(R.id.etEmailMain);
        etPassword = findViewById(R.id.etPassword);
        mainProgressBar = findViewById(R.id.mainProgressBar);
        mainProgressBar.setVisibility(View.INVISIBLE);

//
//        startActivity(new Intent(MainActivity.this,RegisterUser.class));

        mAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String username = etEmail.getText().toString().trim();
//                String password = etPassword.getText().toString().trim();
                String username = "romulo@elephantthoughts.com";
                String password = "1206Romulo";


                if(TextUtils.isEmpty(username))
                {
                    etEmail.setError("Email is Empty");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    etPassword.setError("Password is Empty");
                    return;
                }
                mainProgressBar.setVisibility(View.VISIBLE);


               mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                           startActivity(new Intent(getApplicationContext(),InventoryDashboard.class));

                       } else
                       {
                           Toast.makeText(MainActivity.this, " Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }
        });

    }
}