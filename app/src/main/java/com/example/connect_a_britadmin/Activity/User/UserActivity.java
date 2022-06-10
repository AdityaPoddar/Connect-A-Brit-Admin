package com.example.connect_a_britadmin.Activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.connect_a_britadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private RecyclerView userRecycler;

    private List<UserData> userList;
    private UserAdaptor adapter;
    private DatabaseReference databaseReference,dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        userRecycler=findViewById(R.id.userRecycler);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");



        getAllUser();
    }

    private void getAllUser() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList= new ArrayList<>();


                for( DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    UserData data = dataSnapshot.getValue(UserData.class);
                    userList.add(data);
                }
                userRecycler.setHasFixedSize(true);
                userRecycler.setLayoutManager(new LinearLayoutManager(UserActivity.this));
                adapter=  new UserAdaptor(userList,UserActivity.this);
                userRecycler.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}