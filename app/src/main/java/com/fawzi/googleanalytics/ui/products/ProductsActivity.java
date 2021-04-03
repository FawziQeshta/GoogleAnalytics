package com.fawzi.googleanalytics.ui.products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.fawzi.googleanalytics.R;
import com.fawzi.googleanalytics.databinding.ActivityProductsBinding;
import com.fawzi.googleanalytics.models.Product;
import com.fawzi.googleanalytics.utils.GoogleAnalytics;

import java.util.List;

public class ProductsActivity extends AppCompatActivity implements ProductPresenter.ProductListener {

    private ActivityProductsBinding binding;

    private long startTime;
    private long elapsedTime = 0;

    private ProductPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new ProductPresenter(this, this);

        presenter.loadProducts(getProductCollection());

        GoogleAnalytics.sendScreenTrackToFirebase(this,"ProductsScreen","ProductsActivity");

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

    private String getProductCollection() {
        return getIntent().getStringExtra("productCollection");
    }

    @Override
    public void getProducts(List<Product> products) {
        binding.progressProducts.setVisibility(View.GONE);

        ProductAdapter adapter = new ProductAdapter(this, products);
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProducts.setHasFixedSize(true);
        binding.rvProducts.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        elapsedTime = elapsedTime + (System.currentTimeMillis() - startTime);
        int seconds = (int) ((elapsedTime / 1000) % 60);
        int minutes = (int) ((elapsedTime / 1000) / 60);
        GoogleAnalytics.sendUserTrackedScreenTime(minutes + ":" + seconds, "ProductsActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
    }

}