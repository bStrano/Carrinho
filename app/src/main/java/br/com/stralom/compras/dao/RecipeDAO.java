package br.com.stralom.compras.dao;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.ItemRecipe;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.entities.Recipe;
import br.com.stralom.compras.listerners.FirebaseGetDataListener;

/**
 * Created by Bruno Strano on 17/01/2018.
 */

public class RecipeDAO extends GenericDAO {
    private static final String TAG = "RecipeDAO";
    private FirebaseFirestore dbFirebase;
    private Context context;
//    private final ItemRecipeDAO itemRecipeDAO;

    public RecipeDAO(Context context) {
        super(context, DBHelper.TABLE_RECIPE);
        this.context = context;
        this.dbFirebase = FirebaseFirestore.getInstance();
        //        itemRecipeDAO = new ItemRecipeDAO(context);
    }

    public void add(Recipe recipe) {
        Log.d(TAG, "Add Recipe");

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferences_profiles), Context.MODE_PRIVATE);
        String profileIdentifier = sharedPreferences.getString(context.getString(R.string.sharedPreferences_selectedProfile), "");

        Map<String, Object> productTest = recipe.toJson(profileIdentifier, dbFirebase);


        dbFirebase.collection("profiles").document(profileIdentifier).collection("recipes")
                .document(recipe.getName())
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



    public void remove( String id)  {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferences_profiles), Context.MODE_PRIVATE);
        String profileIdentifier = sharedPreferences.getString(context.getString(R.string.sharedPreferences_selectedProfile), "");
        if(profileIdentifier != null){
            dbFirebase.collection("profiles").document(profileIdentifier).collection("recipes")
                    .document(id).delete();
        }
        //        itemRecipeDAO.deleteAllFromRecipe(id);
        //super.remove(id);
    }
    public void getAll(final FirebaseGetDataListener listener, final ArrayList<Product> products){
        Log.d(TAG, "GET ALL ORDER BY NAME");

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferences_profiles), Context.MODE_PRIVATE);
        String profileIdentifier = sharedPreferences.getString(context.getString(R.string.sharedPreferences_selectedProfile), "");

        dbFirebase.collection("profiles").document(profileIdentifier).collection("recipes")
                .orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Recipe> recipes = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String recipeName = document.getString("name");
                                ArrayList<HashMap<String,Object>> ingredientsDocument = (ArrayList<HashMap<String,Object>>) document.get("ingredients");
                                Log.d("Bruno",ingredientsDocument.toString());
                                ArrayList<ItemRecipe> ingredients = new ArrayList<>();
                                  for(HashMap<String,Object> ingredient : ingredientsDocument){
                                    double amount = (double) ingredient.get("amount");
                                    String productName = (String) ingredient.get("productName");
                                    Product product = null;
                                    for(Product item : products){
                                        Log.d("Bruno", item.getName() + "/" + productName);
                                        if(item.getName().equals(productName)){
                                            Log.d("Bruno", "true");
                                            product = item;
                                            break;
                                        }
                                    }
                                    if(product != null){
                                        Log.d("Bruno", "Ingredient add");
                                        ItemRecipe itemRecipe = new ItemRecipe(amount,product);
                                        ingredients.add(itemRecipe);
                                    }

                                }
                                Log.d("Bruno", ingredients.toString());
                                Log.d("Bruno", String.valueOf(ingredients.size()));
                                Recipe recipe = new Recipe(document.getId(), recipeName, (List<ItemRecipe>) ingredients.clone());
                                Log.d("Bruno", recipe.toString());
                                  recipes.add(recipe);
                            }
                            listener.handleListData(recipes);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }




}
