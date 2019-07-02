package com.example.notetomyself;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.notetomyself.Storage.Memo;
import com.example.notetomyself.Storage.StorageManagement;

import java.util.Collections;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Memo> memos;
    private Activity activity;

    MemoAdapter(List<Memo> memos, Activity activity) {
        this.memos = memos;
        this.activity = activity;
    }

    public void updateMemo(List<Memo> memos) {
        this.memos = memos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layout = R.layout.cardview_memo;
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(layout, viewGroup, false);
        return new ViewHolderMemo(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolderMemo) viewHolder).fill(memos.get(i));
    }

    @Override
    public int getItemCount() {
        return memos.size();
    }

    public class ViewHolderMemo extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private SeekBar seekBarProgress;
        private ImageView btnDelete;
        private ImageView btnAddCategory;
        private ImageView btnEdit;
        private ImageView btnFavourite;

        public ViewHolderMemo(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView_memo_name);
            btnDelete = itemView.findViewById(R.id.imageView_delete_memo);
            btnFavourite = itemView.findViewById(R.id.imageView_toggle_favourite);
            btnEdit = itemView.findViewById(R.id.imageView_edit_memo);
        }

        void fill(final Memo memo) {
            textViewName.setText(memo.getName());
            if (memo.isFavorite()) {
                btnFavourite.setColorFilter(
                        btnFavourite.getContext().getColor(R.color.colorGold));
            }

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tmp = memos.indexOf(memo);
                    memos.remove(memo);
                    StorageManagement.getInstance().removeMemo(memo);
                    MemoAdapter.this.notifyItemRemoved(tmp);
                }
            });


            btnFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int oldPos = memos.indexOf(memo);
                    if (memo.isFavorite()) {
                        memo.setFavorite(false);
                        btnFavourite.setColorFilter(
                                btnFavourite.getContext().getColor(R.color.colorLiteAccentGray));
                    } else {
                        memo.setFavorite(true);
                        btnFavourite.setColorFilter(
                                btnFavourite.getContext().getColor(R.color.colorGold));
                    }
                    Collections.sort(memos);
                    int newPos = memos.indexOf(memo);
                    MemoAdapter.this.notifyItemMoved(oldPos, newPos);
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initEditButton(memo);
                }
            });
        }


        private void initEditButton(final Memo memo) {
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditMemoDialog dialog = new EditMemoDialog(memo, MemoAdapter.this.activity);
                }
            });
        }
    }
}
