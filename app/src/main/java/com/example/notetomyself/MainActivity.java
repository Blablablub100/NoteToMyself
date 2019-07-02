package com.example.notetomyself;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.example.notetomyself.Storage.StorageManagement;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycler();
        initNewRecordingButton();
    }

    private void initNewRecordingButton() {
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.updateMemo(StorageManagement.getInstance().getAllMemos());
            adapter.notifyDataSetChanged();
        }
    }

    private void initRecycler() {
        RecyclerView mainRecycler = findViewById(R.id.reyclerView_main);
        mainRecycler.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mainRecycler.setHasFixedSize(true); // always size matching constraint

        // fill the adapter with data
        adapter = new MemoAdapter(StorageManagement.getInstance().getAllMemos(), this);
        mainRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
