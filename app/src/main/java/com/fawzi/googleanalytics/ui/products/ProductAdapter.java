package com.fawzi.googleanalytics.ui.products;

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
import com.fawzi.googleanalytics.databinding.ItemProductBinding;
import com.fawzi.googleanalytics.models.Product;
import com.fawzi.googleanalytics.ui.product_details.ProductDetailsActivity;
import com.fawzi.googleanalytics.utils.GoogleAnalytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{

    private Context context;
    private List<Product> data;
    private StorageReference storageRef;

    private static final String TAG = "ProductAdapter";

    public ProductAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(ItemProductBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = data.get(position);

        holder.binding.productName.setText(product.getName());
        holder.binding.productTitle.setText(product.getTitle());

        StorageReference pathReference = storageRef.child(product.getImage());

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context.getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(holder.binding.productImage);
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
                GoogleAnalytics.sendClickedEventToFirebase(context, "products", "clicked_product_" + product.getName());
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("Product", product);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ProductHolder extends RecyclerView.ViewHolder {
        private ItemProductBinding binding;

        public ProductHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
