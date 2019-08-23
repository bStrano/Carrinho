package br.com.stralom.compras.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import androidx.databinding.ObservableArrayList;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.Cart;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.ItemCart;
import br.com.stralom.compras.entities.ItemStock;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.listerners.FirebaseGetDataListener;
import br.com.stralom.compras.listerners.FirebasePostDataListener;

/**
 * Created by Bruno Strano on 30/12/2017.
 */

public class ProductDAO extends GenericDAO{
    private static final String TAG = "ProductDAO";

    private final DBHelper dbHelper;
    private FirebaseFirestore dbFirebase;
    private Context context;

    public ProductDAO(Context context) {
        super(context,DBHelper.TABLE_PRODUCT);
        dbHelper = new DBHelper(context);
        this.context = context;
        dbFirebase = FirebaseFirestore.getInstance();
    }



    public void add(final Product product, final FirebasePostDataListener listener) {
        Log.d(TAG, "Add Product");
        Map<String, Object> productTest = product.toJson();

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferences_profiles), Context.MODE_PRIVATE);
        String profileIdentifier = sharedPreferences.getString(context.getString(R.string.sharedPreferences_selectedProfile), "");
        dbFirebase.collection("profiles").document(profileIdentifier).collection("products")
                .document(product.getName())
                .set(productTest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

//    public void add(String name, double price, Category category, String profileIdentifier, FirebasePostDataListener listener){
//        Product product = new Product(name,name,price,category, new ItemCart(1));
//        add(product,profileIdentifier, listener);
//    }


    public void remove(String productName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferences_profiles), Context.MODE_PRIVATE);
        String profileIdentifier = sharedPreferences.getString(context.getString(R.string.sharedPreferences_selectedProfile), "");
        if(profileIdentifier != null){
            dbFirebase.collection("profiles").document(profileIdentifier).collection("products")
                    .document(productName);
        }

    }


    public void getAllOrderedByName( String profileIdentifier, final FirebaseGetDataListener listener){
        Log.d(TAG, "GET ALL ORDER BY NAME");

        dbFirebase.collection("profiles").document(profileIdentifier).collection("products")
                .orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Product> products = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                HashMap itemCartHash = (HashMap) document.get("itemcart");

                                HashMap itemStockHash = (HashMap) document.get("itemstock");
                                ItemCart itemCart = new ItemCart(Double.parseDouble(itemCartHash.get("amount").toString()));
                                ItemStock itemStock = new ItemStock( (double) itemStockHash.get("actualAmount"), (double) itemStockHash.get("maxAmount"));
                                Product product = new Product(document.getId(),document.getString("name"), document.getDouble("price"), new Category(document.getString("category")), itemCart,itemStock);
                                products.add(product);
                            }
                            listener.handleListData(products);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    public void update(Product product) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferences_profiles), Context.MODE_PRIVATE);
        String profileIdentifier = sharedPreferences.getString(context.getString(R.string.sharedPreferences_selectedProfile), "");
        if(profileIdentifier != null) {
            this.add(product,null);
        }
    }
}
