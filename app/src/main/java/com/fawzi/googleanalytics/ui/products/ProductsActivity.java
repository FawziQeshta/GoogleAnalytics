package com.fawzi.googleanalytics.ui.products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.fawzi.googleanalytics.R;
import com.fawzi.googleanalytics.databinding.ActivityProductsBinding;
import com.fawzi.googleanalytics.models.Product;
import com.fawzi.googleanalytics.ui.main.CategoryAdapter;
import com.fawzi.googleanalytics.ui.main.MainPresenter;

import java.util.List;

public class ProductsActivity extends AppCompatActivity implements ProductPresenter.ProductListener {

    private ActivityProductsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ProductPresenter presenter = new ProductPresenter(this, this);

        presenter.loadProducts(getProductCollection());


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

}