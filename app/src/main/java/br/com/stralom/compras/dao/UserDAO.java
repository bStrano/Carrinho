package br.com.stralom.compras.dao;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.ItemCart;
import br.com.stralom.compras.entities.ItemStock;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.entities.User;
import br.com.stralom.compras.listerners.FirebaseGetDataListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {
    private FirebaseFirestore dbFirebase;
    private static final String TAG = "UserDAO" ;


    public UserDAO() {
        this.dbFirebase = FirebaseFirestore.getInstance();
    }

    public void getAll( FirebaseUser firebaseUser, final FirebaseGetDataListener listener){
        dbFirebase.collection("users").document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            User user = new User(document.getString("uid"), document.getString("shareCode"), document.getString("email"), document.getString("phone"), document.getString("displayName"));
                            if (document.exists()) {
                                ArrayList userInfo = new ArrayList<>();
                                userInfo.add(user);
                                listener.handleListData(userInfo);
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void createAditionalInfo(FirebaseUser firebaseUser){
        Log.d(TAG, "Create AdditionalInfo");

        User userInfo = new User(firebaseUser);
        Map<String, Object> userJson = userInfo.toJson();
        dbFirebase.collection("users").document(firebaseUser.getUid())
                .set(userJson, SetOptions.mergeFields(new ArrayList<String>()))
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
}
