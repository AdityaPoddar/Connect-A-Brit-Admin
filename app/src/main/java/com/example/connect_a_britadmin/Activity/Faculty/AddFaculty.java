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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.connect_a_britadmin.Database.FacultyData;
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

public class AddFaculty extends AppCompatActivity {

    private ImageView addFacultyImage;
    private EditText facultyName,facultyEmail,facultyPost;
    private Spinner departmentCategory;
    private Button addFacultyBtn;
    private final int REQ=1;
    private Bitmap bitmap=null;
    private String category;
    private String name,email,post,downloadUrl="";
    private DatabaseReference databaseReference,dbRef;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        addFacultyImage=findViewById(R.id.addFacultyImage);
        facultyName=findViewById(R.id.facultyName);
        facultyEmail=findViewById(R.id.facultyEmail);
        facultyPost=findViewById(R.id.facultyPost);
        departmentCategory=findViewById(R.id.departmentCategory);
        addFacultyBtn=findViewById(R.id.addFacultyBtn);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Faculty");
        storageReference= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(this);

        addFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });



        String[] items = new String[]{"Select Category","IT","Management","Hotel Management"};
        departmentCategory.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));


        departmentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category=departmentCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addFacultyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

    }

    private void checkValidation() {
        name=facultyName.getText().toString();
        email=facultyEmail.getText().toString();
        post=facultyPost.getText().toString();


        if(name.isEmpty())
        {
            facultyName.setError("Empty");
            facultyName.requestFocus();
        }
        else if(email.isEmpty())
        {
            facultyEmail.setError("Empty");
            facultyEmail.requestFocus();
        }
        else if(post.isEmpty())
        {
            facultyPost.setError("Empty");
            facultyPost.requestFocus();
        }
        else if(category.equals("Select Category"))
        {
            Toast.makeText(this, "Please provide Category", Toast.LENGTH_SHORT).show();
        }
        else if(bitmap==null)
        {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            insertFacultyData();
        }
        else
        {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            insertFacultyImage();
        }
    }

    private void insertFacultyData() {



            dbRef=databaseReference.child(category);
            final String uniqueKey=databaseReference.push().getKey();
            String name=facultyName.getText().toString();
            String email=facultyEmail.getText().toString();
            String post=facultyPost.getText().toString();





            FacultyData facultyData = new FacultyData(name,email,post,downloadUrl,uniqueKey);



        dbRef.child(uniqueKey).setValue(facultyData).addOnSuccessListener(new OnSuccessListener<Void>() {

                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    Toast.makeText(AddFaculty.this, "Faculty Added!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddFaculty.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
                }
            });




        }


    private void insertFacultyImage() {

        ByteArrayOutputStream bytes= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,bytes);
        byte[] finalimg=bytes.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child("Faculty").child(finalimg+"jpg");
        final UploadTask uploadtask=filepath.putBytes(finalimg);
        uploadtask.addOnCompleteListener(AddFaculty.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    insertFacultyData();
                                }
                            });
                        };
                    });
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(AddFaculty.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
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
            addFacultyImage.setImageBitmap(bitmap);
        }
    }
}