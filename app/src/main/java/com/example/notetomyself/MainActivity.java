package com.example.notetomyself;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.notetomyself.Storage.StorageManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycler();
        initNewRecordingButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bar_action_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showFilterDialog();
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        final String[] categories = StorageManagement.getInstance().getAllCategories().toArray(new String[0]);
        final List<String> selectedItems = new ArrayList<>();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle(R.string.filter_category_title);
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
        builder.setMultiChoiceItems(categories, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    selectedItems.add(categories[which]);
                                } else if (selectedItems.contains(categories[which])) {
                                    // Else, if the item is already in the array, remove it
                                    selectedItems.remove(categories[which]);
                                }
                            }
                        });
                // Set the action buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        setFilter(selectedItems);
                    }
                });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create().show();
    }

    private void setFilter(List<String> categories) {
        adapter.updateMemo(StorageManagement.getInstance().getMembersOf(categories));
        adapter.notifyDataSetChanged();
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
