package com.example.etinvetory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    Button btnRegisterUser;
    public static View view;
    TextView tvHomeTitle;
    String role;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        btnRegisterUser = view.findViewById(R.id.btnRegisterUser);
        tvHomeTitle = view.findViewById(R.id.tvHomeTitle);


        dbRef = dbRef.child("users");





        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tvHomeTitle.append(user.getEmail());


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot userInfo : snapshot.getChildren())
                    {
                        role = userInfo.child("role").getValue(String.class);
                        if(role.equals("Admin"))
                        {
                            btnRegisterUser.setVisibility(View.VISIBLE);

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),RegisterUser.class));
            }
        });
        return view;
    }
}
