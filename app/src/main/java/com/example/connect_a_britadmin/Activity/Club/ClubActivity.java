package com.example.connect_a_britadmin.Activity.Club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.connect_a_britadmin.Activity.Faculty.UpdateFaculty;
import com.example.connect_a_britadmin.Adaptor.ClubAdaptor;
import com.example.connect_a_britadmin.Adaptor.FacultyAdaptor;
import com.example.connect_a_britadmin.Database.ClubData;
import com.example.connect_a_britadmin.Database.FacultyData;
import com.example.connect_a_britadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubActivity extends AppCompatActivity {
    private RecyclerView leadership,business,drama,sports;
    private List<ClubData> llist,blist,dlist,slist;
    private ClubAdaptor adapter;
    private DatabaseReference databaseReference,dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        leadership=findViewById(R.id.leadership);
        business=findViewById(R.id.business);
        drama=findViewById(R.id.drama);
        sports=findViewById(R.id.sports);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Clubs");

        leadershipclub();
        dramaclub();
        buisnessclub();
        sportsclub();

    }

    private void leadershipclub() {
        dbRef=databaseReference.child("LeaderShip");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                llist= new ArrayList<>();

                    for( DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        ClubData data = dataSnapshot.getValue(ClubData.class);
                        llist.add(data);
                    }
                    leadership.setHasFixedSize(true);
                    leadership.setLayoutManager(new LinearLayoutManager(ClubActivity.this));
                    adapter=  new ClubAdaptor(llist,ClubActivity.this,"LeaderShip");


                    leadership.setAdapter(adapter);
                }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClubActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void dramaclub() {
        dbRef=databaseReference.child("Drama");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dlist= new ArrayList<>();
                if(!snapshot.exists()){


                }
                else
                {

                    for( DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        ClubData data = dataSnapshot.getValue(ClubData.class);
                        dlist.add(data);
                    }
                    drama.setHasFixedSize(true);
                    drama.setLayoutManager(new LinearLayoutManager(ClubActivity.this));
                    adapter=  new ClubAdaptor(dlist,ClubActivity.this,"Drama");
                    drama.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClubActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void buisnessclub() {
        dbRef=databaseReference.child("Business");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                blist= new ArrayList<>();
                if(!snapshot.exists()){


                }
                else
                {

                    for( DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        ClubData data = dataSnapshot.getValue(ClubData.class);
                        blist.add(data);
                    }
                    business.setHasFixedSize(true);
                    business.setLayoutManager(new LinearLayoutManager(ClubActivity.this));
                    adapter=  new ClubAdaptor(blist,ClubActivity.this,"Business");
                    business.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClubActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sportsclub() {
        dbRef=databaseReference.child("Sports");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slist= new ArrayList<>();
                if(!snapshot.exists()){


                }
                else
                {

                    for( DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        ClubData data = dataSnapshot.getValue(ClubData.class);
                        slist.add(data);
                    }
                    sports.setHasFixedSize(true);
                    sports.setLayoutManager(new LinearLayoutManager(ClubActivity.this));
                    adapter=  new ClubAdaptor(slist,ClubActivity.this,"Sports");
                    sports.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClubActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}