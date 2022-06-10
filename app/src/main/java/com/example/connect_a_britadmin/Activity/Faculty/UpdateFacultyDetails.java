package com.example.connect_a_britadmin.Activity.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateFacultyDetails extends AppCompatActivity {

    private ImageView updateFacultyImage;
    private EditText updatefacultyName,updatefacultyEmail,updatefacultyPost;
    private Button  updateFacultyBtn,deleteFacultyBtn;
    private String email,name,post,image;
    private final int REQ=1;
    private Bitmap bitmap=null;
    private DatabaseReference databaseReference,dbRef;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private String downloadUrl  ;
    String uniqueKey;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty_details);


        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        post=getIntent().getStringExtra("post");
        image=getIntent().getStringExtra("image");

         uniqueKey=getIntent().getStringExtra("key");
         category=getIntent().getStringExtra("category");


        updateFacultyImage=findViewById(R.id.updateFacultyImage);
        updatefacultyName=findViewById(R.id.updatefacultyName);
        updatefacultyEmail=findViewById(R.id.updatefacultyEmail);
        updatefacultyPost=findViewById(R.id.updatefacultyPost);
        updateFacultyBtn=findViewById(R.id.updateFacultyBtn);
        deleteFacultyBtn=findViewById(R.id.deleteFacultyBtn);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Faculty");
        storageReference= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(this);


        try {

            Picasso.get().load(image).into(updateFacultyImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updatefacultyName.setText(name);
        updatefacultyEmail.setText(email);
        updatefacultyPost.setText(post);
        updateFacultyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        updateFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=updatefacultyName.getText().toString();
                email=updatefacultyEmail.getText().toString();
                post=updatefacultyPost.getText().toString();
                checkValidation();
            }
        });
        deleteFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });


    }

    private void deleteData() {

        databaseReference.child(category).child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UpdateFacultyDetails.this, "Faculty Detail Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateFacultyDetails.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateFacultyDetails.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValidation() {
        if(name.isEmpty())
        {
            updatefacultyName.setError("Empty");
            updatefacultyName.requestFocus();
        }
        else if(email.isEmpty())
        {
            updatefacultyEmail.setError("Empty");
            updatefacultyEmail.requestFocus();
        }
        else if(post.isEmpty())
        {
            updatefacultyPost.setError("Empty");
            updatefacultyPost.requestFocus();
        }
        else if(bitmap== null)
        {
            updateData(image);
        }
        else
        {
            uploadImage();
        }
        
    }

    private void updateData(String s) {
        HashMap hp = new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("post",post);
        hp.put("image",s);


        databaseReference.child(category).child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateFacultyDetails.this, "Faculty Detail Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateFacultyDetails.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateFacultyDetails.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadImage() {


            ByteArrayOutputStream bytes= new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,bytes);
            byte[] finalimg=bytes.toByteArray();
            final StorageReference filepath;
            filepath=storageReference.child("Faculty").child(finalimg+"jpg");
            final UploadTask uploadtask=filepath.putBytes(finalimg);
            uploadtask.addOnCompleteListener(UpdateFacultyDetails.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                        updateData(downloadUrl);
                                    }
                                });
                            };
                        });
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateFacultyDetails.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
                    }
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
            updateFacultyImage.setImageBitmap(bitmap);
        }
    }
}