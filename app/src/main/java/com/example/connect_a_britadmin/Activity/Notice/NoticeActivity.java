package com.example.connect_a_britadmin.Activity.Notice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.connect_a_britadmin.Database.NoticeData;
import com.example.connect_a_britadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoticeActivity extends AppCompatActivity {


    CardView addImage;
    private final int REQ=1;
    private Bitmap bitmap;
    private ImageView noticeImageView;
    private EditText noticeTitle;
    private Button uploadNoticeBtn;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl="";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);


        noticeImageView=findViewById(R.id.noticeImageView);
        noticeTitle=findViewById(R.id.noticeTitle);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Newsfeed");
        storageReference= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(this);



        uploadNoticeBtn=findViewById(R.id.uploadNoticeBtn);
        uploadNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noticeTitle.getText().toString().isEmpty())
                {
                    noticeTitle.setError("Empty");
                }
                else if(bitmap==null)
                {
                    uploadData();
                }
                else
                {
                    uploadImage();
                }
            }
        });



        addImage=findViewById(R.id.addImage);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }


        });
    }

    private void uploadImage() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        ByteArrayOutputStream bytes= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,bytes);
        byte[] finalimg=bytes.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child("Notice").child(finalimg+"jpg");
        final UploadTask uploadtask=filepath.putBytes(finalimg);
        uploadtask.addOnCompleteListener(NoticeActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    uploadtask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadUrl=String.valueOf(uri);
                                        uploadData();
                                    }
                                    });
                        };
                    });
                }
                else
                {
                    progressDialog.dismiss();
                    uploadData();
                    Toast.makeText(NoticeActivity.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {


        final String uniqueKey=databaseReference.push().getKey();
        String title=noticeTitle.getText().toString();


        Calendar calForDate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("dd-MM-yy");
        String date =  currentDate.format(calForDate.getTime());



        Calendar calForTime= Calendar.getInstance();
        SimpleDateFormat currentTime= new SimpleDateFormat("hh:mm a");
        String time =  currentTime.format(calForTime.getTime());


        NoticeData noticeData = new NoticeData(title,downloadUrl,date,time,uniqueKey);



        databaseReference.child(uniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(NoticeActivity.this, "Notice Uploaded!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(NoticeActivity.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void openGallery() {

            Intent chooseImage= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(chooseImage,REQ);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            noticeImageView.setImageBitmap(bitmap);
        }
    }
}
