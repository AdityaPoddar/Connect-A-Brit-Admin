package com.example.connect_a_britadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.connect_a_britadmin.Activity.Club.ClubActivity;
import com.example.connect_a_britadmin.Activity.Faculty.UpdateFaculty;
import com.example.connect_a_britadmin.Activity.Gallery.GalleryActivity;
import com.example.connect_a_britadmin.Activity.Notice.NoticeActivity;
import com.example.connect_a_britadmin.Activity.Notice.NoticeDeleteActivity;
import com.example.connect_a_britadmin.Activity.User.UserActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView uploadNotice,addGallery,addFaculty,deleteNotice,addStudent,clubsMember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadNotice = (CardView) findViewById(R.id.addNotices);
        addGallery = (CardView) findViewById(R.id.addGallery);
        addFaculty = (CardView) findViewById(R.id.addFaculty);
        deleteNotice = (CardView) findViewById(R.id.deleteNotice);
        addStudent = (CardView) findViewById(R.id.addStudent);
        clubsMember = (CardView) findViewById(R.id.clubsMember);

        uploadNotice.setOnClickListener(MainActivity.this);
        addGallery.setOnClickListener(MainActivity.this);
        addFaculty.setOnClickListener(MainActivity.this);
        deleteNotice.setOnClickListener(MainActivity.this);
        addStudent.setOnClickListener(MainActivity.this);
        clubsMember.setOnClickListener(MainActivity.this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.addNotices:
                 intent = new Intent(MainActivity.this, NoticeActivity.class);
                startActivity(intent);
                break;

            case R.id.addGallery:
                 intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
                break;

                case R.id.addFaculty:
                 intent = new Intent(MainActivity.this, UpdateFaculty.class);
                startActivity(intent);
                break;
                case R.id.deleteNotice:
                 intent = new Intent(MainActivity.this, NoticeDeleteActivity.class);
                startActivity(intent);
                break;
                case R.id.addStudent:
                 intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
                break;
            case R.id.clubsMember:
                 intent = new Intent(MainActivity.this, ClubActivity.class);
                startActivity(intent);
                break;
        }
    }
}
