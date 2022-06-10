package com.example.connect_a_britadmin.Adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect_a_britadmin.Database.NoticeData;
import com.example.connect_a_britadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class NoticeAdaptor extends RecyclerView.Adapter <NoticeAdaptor.NoticeViewAdaptor> {
    private Context context;
    private  ArrayList<NoticeData> list;

    public NoticeAdaptor(Context context, ArrayList<NoticeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoticeViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_layout,parent,false);
        return new NoticeViewAdaptor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewAdaptor holder, int position) {


        NoticeData data = list.get(position);
        holder.deleteNoticeTitle.setText(data.getTitle());
        try {
            if(data.getImage()!= null)
            Picasso.get().load(data.getImage()).into(holder.deleteNoticeView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.deleteNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Are you Sure ?");
                builder.setCancelable(true);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Newsfeed");
                        databaseReference.child(data.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Deleted!!", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog=null;
                try {
                     dialog=builder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(dialog!=null)
                dialog.show();


            }
        });
 
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoticeViewAdaptor extends RecyclerView.ViewHolder {

        private Button deleteNoticeBtn;
        private TextView deleteNoticeTitle;
         private ImageView deleteNoticeView;

        public NoticeViewAdaptor(@NonNull View itemView) {
            super(itemView);

            deleteNoticeBtn=itemView.findViewById(R.id.deleteNoticeBtn);
            deleteNoticeTitle=itemView.findViewById(R.id.deleteNoticeTitle);
            deleteNoticeView=itemView.findViewById(R.id.deleteNoticeView);
        }
    }
}
