package br.com.stralom.compras.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import br.com.stralom.compras.R;
import br.com.stralom.compras.adapters.ItemCartRegistrationAdapter;
import br.com.stralom.compras.adapters.ItemCartRegistrationProductAdapter;
import br.com.stralom.compras.adapters.ItemCartRegistrationRecipeAdapter;
import br.com.stralom.compras.dao.DBHelper;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.dao.RecipeDAO;
import br.com.stralom.compras.entities.Cart;
import br.com.stralom.compras.entities.ItemCart;
import br.com.stralom.compras.entities.ItemRecipe;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.entities.Recipe;
import br.com.stralom.compras.listerners.FirebasePostDataListener;


public class ItemCartRegistration extends AppCompatActivity {
    private static final String TAG = "ItemCartRegistration";
    private ProductDAO productDAO;
    private RecipeDAO recipeDAO;

    private ArrayList<Product> products;
    private ArrayList<Recipe> recipes;

    private Cart cart;

    private SearchView searchView;
    private RecyclerView listView;
    private Button btnCancel;
    private Button btnConfirm;
    private RadioButton rbtnProducts;
    private RadioButton rbtnRecipes;

    private ItemCartRegistrationProductAdapter productAdapter;
    private ItemCartRegistrationRecipeAdapter recipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cart_registration);

        Log.d("DEBUG", "ONCREATE");
        // ****************** Temporary ********************
        cart = new Cart((long) 1);

        productDAO = new ProductDAO(this);
        recipeDAO = new RecipeDAO(this);


        Intent itemCartMainIntent = getIntent();
        // products = (ArrayList<Product>) productDAO.getAll();

        products = itemCartMainIntent.getParcelableArrayListExtra("products");
        recipes = itemCartMainIntent.getParcelableArrayListExtra("recipes");

        Log.e("DEBUG", products.toString());



        searchView = findViewById(R.id.search_itemCartRegistration);
        listView = findViewById(R.id.registration_itemCart_list);
        btnCancel = findViewById(R.id.registration_itemCart_cancel);
        btnConfirm = findViewById(R.id.registration_itemCart_confirm);
        rbtnProducts = findViewById(R.id.registration_itemCart_rbtnProducts);
        rbtnRecipes = findViewById(R.id.registration_itemCart_rbtnRecipes);


        productAdapter = new ItemCartRegistrationProductAdapter(products, this);
        recipeAdapter = new ItemCartRegistrationRecipeAdapter(recipes, this);


        listView.setAdapter(productAdapter);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (rbtnProducts.isChecked()) {
                    productAdapter.ifNecessaryCreateOrUpdateSimpleProduct(s, cart);
                    productAdapter.getFilter().filter(s);
                } else if (rbtnRecipes.isChecked()) {
                    recipeAdapter.getFilter().filter(s);
                }

                return false;
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Map.Entry<Recipe, Double> entry : recipeAdapter.getSelectedPositions().entrySet()) {
                    addItemFromRecipe(entry);
                }

                for (Map.Entry<Product, Double> entry : productAdapter.getSelectedPositions().entrySet()) {
                    addItemFromProduct(entry);
                }


                setResult(Activity.RESULT_OK,
                        new Intent().putExtra("products",products));
                finish();

            }
        });

    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        searchView.setQuery(null, true);
        ((ItemCartRegistrationAdapter) listView.getAdapter()).restoreList();
        switch (view.getId()) {
            case R.id.registration_itemCart_rbtnProducts:
                if (checked) {
                    listView.setAdapter(productAdapter);

                }
                break;
            case R.id.registration_itemCart_rbtnRecipes:
                if (checked) {
                    listView.setAdapter(recipeAdapter);

                }
                break;
        }
    }


    private void addItemFromProduct(Map.Entry<Product, Double> entry) {
        Log.d(TAG, String.valueOf(entry));
        addItem(entry.getKey());
    }


    private boolean isTemporaryProduct(Map.Entry<Product, Integer> entry) {
        return entry.getKey().getCategory().getTag().equals(DBHelper.CATEGORY_TEMPORARY_PRODUCT);
    }


    private void addItemFromRecipe(Map.Entry<Recipe, Double> entry) {
        //ItemCart itemCart = itemCartDAO.getByProductName(itemRecipe.getProduct().getTag());
        Recipe recipe = entry.getKey();
        Log.d("Bruno", recipe.toString());
        for(ItemRecipe itemRecipe : recipe.getIngredients()){
            itemRecipe.setAmount(itemRecipe.getAmount() * entry.getValue());
            for(Product product : products){
                if(product.getName().equals(itemRecipe.getProduct().getName())) {
                    product.getItemCart().setAmount(product.getItemCart().getAmount() + (itemRecipe.getAmount()*entry.getValue()));
                    break;
                }
            }
        }
    }

    private void addItem(Product product) {
        productDAO.add(product, new FirebasePostDataListener() {
            @Override
            public void addOnSuccessListener(Object objects) {
                Toast.makeText(getBaseContext(),R.string.registration_newitem, Toast.LENGTH_LONG).show();
            }

            @Override
            public void addOnFailureListener() {

            }
        });
    }


}
