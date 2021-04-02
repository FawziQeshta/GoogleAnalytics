package com.fawzi.googleanalytics.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fawzi.googleanalytics.R;
import com.fawzi.googleanalytics.databinding.ItemCategoryBinding;
import com.fawzi.googleanalytics.models.Category;
import com.fawzi.googleanalytics.ui.products.ProductsActivity;
import com.fawzi.googleanalytics.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MainCategoryHolder> {

    private Context context;
    private List<Category> data;
    private StorageReference storageRef;

    private static final String TAG = "MainCategoryAdapter";

    public CategoryAdapter(Context context, List<Category> data) {
        this.context = context;
        this.data = data;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @NonNull
    @Override
    public MainCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainCategoryHolder(ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainCategoryHolder holder, int position) {
        Category category = data.get(position);

        holder.binding.categoryName.setText(category.getName());

        StorageReference pathReference = storageRef.child(category.getImage());

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context.getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .into(holder.binding.categoryImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "onFailure: " + exception.getMessage());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductsActivity.class);
                intent.putExtra("productCollection", checkCategory(category));
                context.startActivity(intent);
            }
        });

    }

    private String checkCategory(Category category) {
        if (category.getName().equals("Food")) {
            return Constants.CATEGORY_FOOD_KEY;
        } else if (category.getName().equals("Electronics")) {
            return Constants.CATEGORY_ELECTRONICS_KEY;
        } else {
            return Constants.CATEGORY_CLOTHES_KEY;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MainCategoryHolder extends RecyclerView.ViewHolder {
        private ItemCategoryBinding binding;

        public MainCategoryHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
