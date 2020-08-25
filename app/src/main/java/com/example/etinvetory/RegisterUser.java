package com.example.etinvetory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.etinvetory.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    Button btnRegisterUser;
    EditText etName, etEmail, etPassword;
    RadioButton rdBtnTeacher, rdBtnAdmin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    String userID;
    String role, fullName, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        getSupportActionBar().hide();

        btnRegisterUser = findViewById(R.id.btnRegister);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        rdBtnAdmin = findViewById(R.id.rdBtnAdmin);
        rdBtnTeacher = findViewById(R.id.rdbtnTeacher);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);

        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 fullName = etName.getText().toString().trim();
                 email = etEmail.getText().toString().trim();
                 password = etPassword.getText().toString().trim();

                

                // User info Validation
                if(TextUtils.isEmpty(email))
                {
                    etEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    etPassword.setError("Password is Required");
                    return;
                }
                if(password.length() < 6)
                {
                    etPassword.setError("Password Must be more then 6 characters");
                    return;
                }
                if(!rdBtnAdmin.isChecked() && !rdBtnTeacher.isChecked())
                {
                    Toast.makeText(RegisterUser.this, "Choose a Role - TEACHER OR ADMIN", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(rdBtnTeacher.isChecked())
                {
                    role = rdBtnTeacher.getText().toString();
                } else { role = rdBtnAdmin.getText().toString();}
                progressBar.setVisibility(View.VISIBLE);

                // register user
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(RegisterUser.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference mDbRef = mDatabase.getReference();

                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", fullName);
                            user.put("email",email);
                            user.put("role",role);

                            mDbRef.child("users").child(userID).setValue(user);
                            startActivity(new Intent(getApplicationContext(),InventoryDashboard.class));

                        } else
                        {
                            Toast.makeText(RegisterUser.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }
}