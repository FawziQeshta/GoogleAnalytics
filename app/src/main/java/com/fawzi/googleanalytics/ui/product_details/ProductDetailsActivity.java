package com.fawzi.googleanalytics.ui.product_details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fawzi.googleanalytics.R;
import com.fawzi.googleanalytics.databinding.ActivityProductDetailsBinding;
import com.fawzi.googleanalytics.models.Product;
import com.fawzi.googleanalytics.utils.GoogleAnalytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductDetailsActivity extends AppCompatActivity {

    private ActivityProductDetailsBinding binding;

    private static final String TAG = "ProductDetailsActivity";

    private long startTime;
    private long elapsedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDataIntent();

        GoogleAnalytics.sendScreenTrackToFirebase(this,"ProductDetailsScreen","ProductDetailsActivity");

        startTime = System.currentTimeMillis();
        elapsedTime = 0;

        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getDataIntent() {
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("Product");
        openImage(product.getImage());
        binding.tvName.setText(product.getName());
        binding.tvTitle.setText(product.getTitle());
        binding.tvDescription.setText(product.getDescription());
    }

    private void openImage(String image) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(image);

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(binding.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "onFailure: " + exception.getMessage());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        elapsedTime = elapsedTime + (System.currentTimeMillis() - startTime);
        int seconds = (int) ((elapsedTime / 1000) % 60);
        int minutes = (int) ((elapsedTime / 1000) / 60);
        GoogleAnalytics.sendUserTrackedScreenTime(minutes + ":" + seconds, "ProductDetailsActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
    }

}