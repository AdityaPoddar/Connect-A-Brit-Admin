package com.example.connect_a_britadmin.Activity.Notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.connect_a_britadmin.Adaptor.NoticeAdaptor;
import com.example.connect_a_britadmin.Database.NoticeData;
import com.example.connect_a_britadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoticeDeleteActivity extends AppCompatActivity {


    private RecyclerView deleteNoticeRecycler;
    private ProgressBar progressBar;
    private ArrayList<NoticeData> list;
    private NoticeAdaptor adaptor;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_delete);



        deleteNoticeRecycler=(RecyclerView) findViewById(R.id.deleteNoticeRecycler);
        progressBar=findViewById(R.id.progressBar);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Newsfeed");


        deleteNoticeRecycler.setLayoutManager(new LinearLayoutManager(this));
        deleteNoticeRecycler.setHasFixedSize(true);
        getNotice();
    }

    private void getNotice() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list= new ArrayList<>();
                for(DataSnapshot d: snapshot.getChildren()){
                    NoticeData data=d.getValue(NoticeData.class);
                    list.add(data);

                }

                adaptor = new NoticeAdaptor(NoticeDeleteActivity.this,list);
                adaptor.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                deleteNoticeRecycler.setAdapter(adaptor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NoticeDeleteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}