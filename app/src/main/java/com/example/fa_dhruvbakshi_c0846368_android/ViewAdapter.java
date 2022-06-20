package com.example.fa_dhruvbakshi_c0846368_android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    ArrayList<String> id,name, latitude, longitude;
    private MyViewHolder holder;

    ViewAdapter(Activity activity, Context context, ArrayList id, ArrayList name, ArrayList latitude, ArrayList longitude){
        this.activity=activity;
        this.context=context;
        this.id=id;
        this.name=name;
        this.latitude = latitude;
        this.longitude = longitude;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.data_main,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.idtxt.setText(String.valueOf(id.get(position)));
        holder.nametxt.setText(String.valueOf(name.get(position)));
        holder.latitxt.setText(String.valueOf(latitude.get(position)));
        holder.longtxt.setText(String.valueOf(longitude.get(position)));
        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, LocationActivity.class);
                intent.putExtra("id",String.valueOf(id.get(position)));
                intent.putExtra("name",String.valueOf(name.get(position)));
                intent.putExtra("lati",String.valueOf(latitude.get(position)));
                intent.putExtra("lng",String.valueOf(longitude.get(position)));

                activity.startActivityForResult(intent,1);
            }
        });
    }


    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idtxt,nametxt,longtxt,latitxt;
        LinearLayout mainlayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idtxt= itemView.findViewById(R.id.dispId);
            nametxt= itemView.findViewById(R.id.dispName);
            latitxt= itemView.findViewById(R.id.dispLat);
            longtxt= itemView.findViewById(R.id.dispLng);
            mainlayout = itemView.findViewById(R.id.mainlayout);
        }
    }


}
