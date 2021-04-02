package com.fawzi.googleanalytics.ui.main;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fawzi.googleanalytics.utils.Constants;
import com.fawzi.googleanalytics.models.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter {

    private Context context;
    private MainListener listener;
    private FirebaseFirestore db;

    private static final String TAG = "MainPresenter";

    public MainPresenter(Context context, MainListener listener) {
        this.context = context;
        this.listener = listener;
        db = FirebaseFirestore.getInstance();
    }

    public void loadCategories() {

        db.collection(Constants.CATEGORIES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Category> categoryList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category category = new Category();
                                category.setId(document.getId());
                                category.setName(String.valueOf(document.getData().get(Constants.CATEGORY_NAME)));
                                category.setImage(String.valueOf(document.getData().get(Constants.CATEGORY_IMAGE)));
                                categoryList.add(category);
                            }

                            listener.getCategories(categoryList);

                        } else {
                            Toast.makeText(context, "An error occurred while reading the data", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public interface MainListener {
        void getCategories(List<Category> categories);
    }

}
