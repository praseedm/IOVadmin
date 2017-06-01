package com.example.master.iovadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recycler_list;
    FirebaseRecyclerAdapter<>

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

    }
}
