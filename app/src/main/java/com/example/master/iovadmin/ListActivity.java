package com.example.master.iovadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.UserObj;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recycler_list;
    FirebaseRecyclerAdapter<UserObj,VehicleViewholder> mAdapter;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference(), vehicleRef = database.child("vehicles");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recycler_list = (RecyclerView) findViewById(R.id.list_view);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Vehicles");
        }
        setUpListView();
    }

    private void setUpListView() {
        recycler_list.setHasFixedSize(false);
        recycler_list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FirebaseRecyclerAdapter<UserObj, VehicleViewholder>(
                UserObj.class,
                R.layout.vehicle_item,
                VehicleViewholder.class,
                vehicleRef
        ) {
            @Override
            protected void populateViewHolder(VehicleViewholder viewHolder, UserObj model, int position) {
                viewHolder.vehicle_num.setText(model.getvNumber());
                viewHolder.vehicle_id.setText(model.getId());
                viewHolder.name.setText(model.getName());
                Glide.with(ListActivity.this)
                        .load(model.getPhotoUri())
                        .centerCrop()
                        .into(viewHolder.vehicle_img);
            }
        };
        recycler_list.setAdapter(mAdapter);
    }

}
