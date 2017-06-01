package com.example.master.iovadmin;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VehicleViewholder extends RecyclerView.ViewHolder{
    public ImageView vehicle_img;
    public TextView vehicle_id, vehicle_num, name;
    public VehicleViewholder(View itemView) {
        super(itemView);
        vehicle_img = (ImageView) itemView.findViewById(R.id.vehicleimg);
        vehicle_id = (TextView) itemView.findViewById(R.id.vehicle_id);
        vehicle_num = (TextView) itemView.findViewById(R.id.vehicle_num);
        name = (TextView) itemView.findViewById(R.id.name);
    }
}
