package com.example.connect_a_britadmin.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect_a_britadmin.Activity.Faculty.UpdateFacultyDetails;
import com.example.connect_a_britadmin.Database.FacultyData;
import com.example.connect_a_britadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FacultyAdaptor extends RecyclerView.Adapter<FacultyAdaptor.FacultyViewAdaptor> {

    private List<FacultyData> list;
    private Context context;
    private  String category;

    public FacultyAdaptor(List<FacultyData> list, Context context, String category) {
        this.list = list;
        this.context = context;
        this.category=category;
    }

    @NonNull
    @Override
    public FacultyViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.faculty_card_layout,parent,false);
        return new FacultyViewAdaptor(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyViewAdaptor holder, int position) {

        FacultyData facultyData = list.get(position);
        holder.facultyname.setText(facultyData.getName());
        holder.facultyemail.setText(facultyData.getEmail());
        holder.facultypost.setText(facultyData.getPost());
        try {
            Picasso.get().load(facultyData.getImage()).into(holder.facultyImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.facultyUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, UpdateFacultyDetails.class);
                intent.putExtra("name",facultyData.getName());
                intent.putExtra("email",facultyData.getEmail());
                intent.putExtra("post",facultyData.getPost());
                intent.putExtra("image",facultyData.getImage());
                intent.putExtra("key",facultyData.getKey());
                intent.putExtra("category",category);
                context.startActivity(intent);
            }
        });

    }

    public class FacultyViewAdaptor extends RecyclerView.ViewHolder {

        private TextView facultyname,facultyemail,facultypost;
        private Button facultyUpdateBtn;
        private ImageView facultyImage;

        public FacultyViewAdaptor(@NonNull View itemView) {
            super(itemView);
            facultyname=itemView.findViewById(R.id.facultyname);
            facultyemail=itemView.findViewById(R.id.facultyemail);
            facultypost=itemView.findViewById(R.id.facultypost);
            facultyUpdateBtn=itemView.findViewById(R.id.facultyUpdateBtn);
            facultyImage=itemView.findViewById(R.id.facultyImage);

        }
    }
}
