package com.example.notetomyself;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.notetomyself.Storage.Memo;
import com.example.notetomyself.Storage.StorageManagement;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> categories;
    private Memo memo;

    CategoryAdapter(Memo memo) {
        this.categories = StorageManagement.getInstance().getAllCategories();
        this.memo = memo;
    }

    void addCategory(String category) {
        if (!memo.addCategory(category)) return;
        categories.add(category);
        notifyItemInserted(categories.size() - 1);
    }

    Memo getMemo() {
        return memo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layout = R.layout.cardview_category;
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(layout, viewGroup, false);
        return new CategoryAdapter.ViewHolderCategory(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        String cat = categories.get(i);
        ((ViewHolderCategory) viewHolder).fill(cat, memo.isMemberOf(cat));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolderCategory extends RecyclerView.ViewHolder {

        private TextView textViewCatName;
        private CheckBox checkBoxCat;
        private String cat;

        public ViewHolderCategory(@NonNull View itemView) {
            super(itemView);
            this.textViewCatName = itemView.findViewById(R.id.textView_dialog_recycler_category);
            this.checkBoxCat = itemView.findViewById(R.id.checkBox_dialog_recycler);
        }

        void fill(String cat, boolean checked) {
            textViewCatName.setText(cat);
            checkBoxCat.setChecked(checked);
            checkBoxCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBoxCat.isChecked()) {
                        memo.addCategory(textViewCatName.getText().toString());
                    } else {
                        memo.removeCategory(textViewCatName.getText().toString());
                    }
                }
            });
        }
    }
}
