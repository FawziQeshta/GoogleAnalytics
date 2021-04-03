package com.fawzi.googleanalytics.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.fawzi.googleanalytics.databinding.ActivityMainBinding;
import com.fawzi.googleanalytics.models.Category;
import com.fawzi.googleanalytics.utils.GoogleAnalytics;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainPresenter.MainListener {

    private ActivityMainBinding binding;

    private static final String TAG = "MainActivity";

    private long startTime;
    private long elapsedTime = 0;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new MainPresenter(this, this);

        presenter.loadCategories();

        GoogleAnalytics.sendScreenTrackToFirebase(this, "CategoriesScreen", "MainActivity");

        startTime = System.currentTimeMillis();
        elapsedTime = 0;

    }

    @Override
    public void getCategories(List<Category> categories) {
        binding.progressMain.setVisibility(View.GONE);

        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        binding.rvMainCategories.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMainCategories.setHasFixedSize(true);
        binding.rvMainCategories.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        elapsedTime = elapsedTime + (System.currentTimeMillis() - startTime);
        int seconds = (int) ((elapsedTime / 1000) % 60);
        int minutes = (int) ((elapsedTime / 1000) / 60);
        GoogleAnalytics.sendUserTrackedScreenTime(minutes + ":" + seconds, "MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
    }


}