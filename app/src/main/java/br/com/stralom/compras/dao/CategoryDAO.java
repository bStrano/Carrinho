package br.com.stralom.compras.dao;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.listerners.FirebaseGetDataListener;

public class CategoryDAO extends GenericDAO {
    private static final String TAG = "CategoryDAO";
    private FirebaseFirestore dbFirebase;

    public CategoryDAO(Context context) {
        super(context, DBHelper.TABLE_CATEGORY);
        dbFirebase =  FirebaseFirestore.getInstance();
    }

    public void getAll(final FirebaseGetDataListener<Category> listener){
        final ArrayList<Category> categories = new ArrayList<>();

        dbFirebase.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category category = new Category(document.getId());
                                categories.add(category);
                            }
                            listener.handleListData(categories);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
