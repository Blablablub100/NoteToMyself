package com.example.notetomyself;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notetomyself.Storage.Memo;
import com.example.notetomyself.Storage.StorageManagement;

public class EditMemoDialog {

    private Activity activity;
    private CategoryAdapter adapter;
    private View view;
    private AlertDialog dialog;
    Memo memo;

    EditMemoDialog(Memo memo, final Activity activity) {

        this.memo = memo;
        this.activity = activity;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_edit_memo, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Memo memo = adapter.getMemo();
                EditText editText = view.findViewById(R.id.editText_dialog_memo_name);
                memo.setName(editText.getText().toString());
                StorageManagement.getInstance().addMemo(memo);
                if (activity instanceof RecordActivity) {
                    activity.finish();
                }

                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
        initUI();
    }

    private void initUI() {
        initRecycler();
        initAddCatButton();
        initFavButton();
        initDate();
    }

    private void initDate() {
        TextView date = view.findViewById(R.id.textView_dialog_memo_date);
        date.setText(Util.getFormattedTime(activity));
    }

    private void initFavButton() {
        final ImageView star = view.findViewById(R.id.imageView_dialog_favourite);
        if (memo.isFavorite()) {
            star.setColorFilter(
                    star.getContext().getColor(R.color.colorGold)
            );
        }
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memo.isFavorite()) {
                    memo.setFavorite(false);
                    star.setColorFilter(
                            star.getContext().getColor(R.color.colorLiteAccentGray)
                    );
                } else {
                    memo.setFavorite(true);
                    star.setColorFilter(
                            star.getContext().getColor(R.color.colorGold)
                    );
                }
            }
        });
    }


    private void initAddCatButton() {
        view.findViewById(R.id.imageView_dialog_add_category)
                .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showNewCategoryDialog();
            }
        });
    }


    private void showNewCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.title_add_category);

        LayoutInflater inflater = activity.getLayoutInflater();
        final View addCatView = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(addCatView);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText tmp = addCatView.findViewById(R.id.editText_new_cat_name);
                adapter.addCategory(tmp.getText().toString(), true);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();
    }


    private void initRecycler() {
        RecyclerView mainRecycler = view.findViewById(R.id.recyclerView_dialog_categorie_list);
        mainRecycler.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mainRecycler.setHasFixedSize(true); // always size matching constraint

        // fill the adapter with data
        adapter = new CategoryAdapter(memo);
        mainRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
