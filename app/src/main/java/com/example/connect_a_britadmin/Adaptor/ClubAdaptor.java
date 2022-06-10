package com.example.connect_a_britadmin.Adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect_a_britadmin.Database.ClubData;
import com.example.connect_a_britadmin.Database.FacultyData;
import com.example.connect_a_britadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ClubAdaptor extends RecyclerView.Adapter<ClubAdaptor.ClubViewAdaptor> {


    private List<ClubData> list;
    private Context context;
    private  String category;

    public ClubAdaptor(List<ClubData> list, Context context, String category) {
        this.list = list;
        this.context = context;
        this.category=category;
    }

    @NonNull
    @Override
    public ClubViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.club_layout,parent,false);
        return new ClubAdaptor.ClubViewAdaptor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubViewAdaptor holder, int position) {

        ClubData data = list.get(position);
        holder.userclubemail.setText(data.getEmail());
        holder.userclubgender.setText(data.getGender());

        holder.clubAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Are you Sure ?");
                builder.setCancelable(true);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {






                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Clubs");
                        databaseReference.child("Accepted").setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Accepted!!", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();



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

    public class ClubViewAdaptor extends RecyclerView.ViewHolder {
        TextView userclubemail,userclubgender;
        Button clubAcceptBtn;

        public ClubViewAdaptor(@NonNull View itemView) {
            super(itemView);
            clubAcceptBtn=itemView.findViewById(R.id.clubAcceptBtn);
            userclubemail=itemView.findViewById(R.id.userclubemail);
            userclubgender=itemView.findViewById(R.id.userclubgender);
        }
    }
}
