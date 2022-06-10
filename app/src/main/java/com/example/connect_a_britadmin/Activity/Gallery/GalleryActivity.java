package com.example.connect_a_britadmin.Activity.Gallery;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GalleryActivity extends AppCompatActivity {

    private CardView addGalleryImage;
    private Spinner imageCategory;
    private Button uploadGalleryBtn;
    private ImageView galleryImageView;
    private String category;
    private final int REQ=1;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    String downloadUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        addGalleryImage=findViewById(R.id.addGalleryImage);
        imageCategory=findViewById(R.id.imageCategory);
        uploadGalleryBtn=findViewById(R.id.uploadGalleryBtn);
        galleryImageView=findViewById(R.id.galleryImageView);
        progressDialog=new ProgressDialog(this);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Gallery");
        storageReference= FirebaseStorage.getInstance().getReference();


        String[] items = new String[]{"Select Category","New Admission","Social Events","International Mobility Program"};
        imageCategory.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));


        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category=imageCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        uploadGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap==null)
                {
                    Toast.makeText(GalleryActivity.this, "Please Select a Image", Toast.LENGTH_SHORT).show();
                }
                else if(category.equals("Select Category"))
                {
                    Toast.makeText(GalleryActivity.this, "Please Select a Category", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();
                    uploadGalleryImage();
                }
            }
        });

    }

    private void uploadGalleryImage() {

        ByteArrayOutputStream bytes= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,bytes);
        byte[] finalimg=bytes.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child("Gallery").child(finalimg+"jpg");
        final UploadTask uploadtask=filepath.putBytes(finalimg);
        uploadtask.addOnCompleteListener(GalleryActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(GalleryActivity.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
        databaseReference=databaseReference.child(category);
        final String uniqueKey=databaseReference.push().getKey();


        databaseReference.child(uniqueKey).setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(GalleryActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(GalleryActivity.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
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
            galleryImageView.setImageBitmap(bitmap);
        }
    }
}