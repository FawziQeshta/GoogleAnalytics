package com.fawzi.googleanalytics.ui.products;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fawzi.googleanalytics.models.Category;
import com.fawzi.googleanalytics.models.Product;
import com.fawzi.googleanalytics.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductPresenter {

    private Context context;
    private ProductListener listener;
    private FirebaseFirestore db;

    private static final String TAG = "ProductPresenter";

    public ProductPresenter(Context context, ProductListener listener) {
        this.context = context;
        this.listener = listener;
        db = FirebaseFirestore.getInstance();
    }

    public void loadProducts(String productCollection) {

        db.collection(productCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Product> productsList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = new Product();
                                product.setId(document.getId());
                                product.setName(String.valueOf(document.getData().get(Constants.PRODUCT_NAME)));
                                product.setTitle(String.valueOf(document.getData().get(Constants.PRODUCT_TITLE)));
                                product.setImage(String.valueOf(document.getData().get(Constants.PRODUCT_IMAGE)));
                                product.setDescription(String.valueOf(document.getData().get(Constants.PRODUCT_DESCRIPTION)));
                                productsList.add(product);
                            }

                            listener.getProducts(productsList);

                        } else {
                            Toast.makeText(context, "An error occurred while reading the data", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public interface ProductListener {
        void getProducts(List<Product> products);
    }

}
