package com.example.connect_a_britadmin.Activity.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect_a_britadmin.R;

import java.util.List;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.UserViewAdaptor> {

    private List<UserData> list;
    private Context context;

    public UserAdaptor(List<UserData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_card_layout,parent,false);
        return new UserAdaptor.UserViewAdaptor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewAdaptor holder, int position) {

        UserData data = list.get(position);
        holder.username.setText(data.getName());
        holder.useremail.setText(data.getEmail());
        holder.userdepartment.setText(data.getDepartment());
        holder.useryear.setText(data.getYear());
//        try {
//            Picasso.get().load(data.getImage()).placeholder(R.drawable.avatar).into(holder.userimage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserViewAdaptor extends RecyclerView.ViewHolder {
        private TextView username,useremail,userdepartment,useryear;

        private ImageView userimage;

        public UserViewAdaptor(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            useremail=itemView.findViewById(R.id.useremail);
            userdepartment=itemView.findViewById(R.id.userdepartment);
            useryear=itemView.findViewById(R.id.useryear);

            userimage=itemView.findViewById(R.id.userimage);

        }
    }
}
