package com.example.connect_a_britadmin.Activity.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.connect_a_britadmin.Adaptor.FacultyAdaptor;
import com.example.connect_a_britadmin.Database.FacultyData;
import com.example.connect_a_britadmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {

    FloatingActionButton fabAdd;
    private RecyclerView itDepartment,managementDepartment,hotelDepartment;
    private LinearLayout itNoData,managementNoData,hotelNoData;
    private List<FacultyData> itList,mangList,hotelList;
    private FacultyAdaptor adapter;
    private DatabaseReference databaseReference,dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        fabAdd=findViewById(R.id.fabAdd);
        itDepartment=findViewById(R.id.itDepartment);
        managementDepartment=findViewById(R.id.managementDepartment);
        hotelDepartment=findViewById(R.id.hotelDepartment);
        itNoData=findViewById(R.id.itNoData);
        managementNoData=findViewById(R.id.managementNoData);
        hotelNoData=findViewById(R.id.hotelNoData);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Faculty");


        itDepartment();
        managementDepartment();
        hotelDepartment();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateFaculty.this,AddFaculty.class));

            }
        });
    }

    private void itDepartment() {
        dbRef=databaseReference.child("IT");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itList= new ArrayList<>();
                if(!snapshot.exists()){
                    itNoData.setVisibility(View.VISIBLE);
                    itDepartment.setVisibility(View.GONE);

                }
                else
                {
                    itNoData.setVisibility(View.GONE);
                    itDepartment.setVisibility(View.VISIBLE);
                    for( DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        FacultyData data = dataSnapshot.getValue(FacultyData.class);
                        itList.add(data);
                    }
                    itDepartment.setHasFixedSize(true);
                    itDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=  new FacultyAdaptor(itList,UpdateFaculty.this,"IT");
                    itDepartment.setAdapter(adapter);
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void managementDepartment() {
        dbRef=databaseReference.child("Management");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mangList= new ArrayList<>();
                if(!snapshot.exists()){
                    managementNoData.setVisibility(View.VISIBLE);
                    managementDepartment.setVisibility(View.GONE);

                }
                else
                {
                    managementNoData.setVisibility(View.GONE);
                    managementDepartment.setVisibility(View.VISIBLE);
                    for( DataSnapshot dsnapshot: snapshot.getChildren())
                    {
                        FacultyData data = dsnapshot.getValue(FacultyData.class);
                        mangList.add(data);
                    }
                    managementDepartment.setHasFixedSize(true);
                    managementDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=  new FacultyAdaptor(mangList,UpdateFaculty.this,"Management");
                    managementDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void hotelDepartment() {
        dbRef=databaseReference.child("Hotel Management");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hotelList= new ArrayList<>();
                if(!snapshot.exists()){
                    hotelNoData.setVisibility(View.VISIBLE);
                    hotelDepartment.setVisibility(View.GONE);

                }
                else
                {
                    hotelNoData.setVisibility(View.GONE);
                    hotelDepartment.setVisibility(View.VISIBLE);
                    for( DataSnapshot dsnapshot: snapshot.getChildren())
                    {
                        FacultyData data = dsnapshot.getValue(FacultyData.class);
                        hotelList.add(data);
                    }
                    hotelDepartment.setHasFixedSize(true);
                    hotelDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=  new FacultyAdaptor(hotelList,UpdateFaculty.this,"Hotel Management");
                    hotelDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}