package com.fawzi.googleanalytics.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.fawzi.googleanalytics.databinding.ActivityMainBinding;
import com.fawzi.googleanalytics.models.Category;
import com.fawzi.googleanalytics.utils.Utilities;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainPresenter.MainListener {

    private ActivityMainBinding binding;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainPresenter presenter = new MainPresenter(this, this);

        presenter.loadCategories();

        Utilities.sendScreenTrackToFirebase(this,"CategoriesScreen","MainActivity");

    }

    @Override
    public void getCategories(List<Category> categories) {
        binding.progressMain.setVisibility(View.GONE);

        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        binding.rvMainCategories.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMainCategories.setHasFixedSize(true);
        binding.rvMainCategories.setAdapter(adapter);
    }

}