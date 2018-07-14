package br.com.stralom.compras;


import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import br.com.stralom.adapters.ItemCartProductAdapter;
import br.com.stralom.adapters.RecipeAdapter;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;


public class ItemCartRegistration extends AppCompatActivity {
    private ProductDAO productDAO;
    private RecipeDAO recipeDAO;

    private ArrayList<Product> products;
    private ArrayList<Recipe> recipes;

    private SearchView searchView;
    private RecyclerView listView;
    private Button btnCancel;
    private Button btnConfirm;
    private RadioButton rbtnProducts;
    private RadioButton rbtnRecipes;

    private ItemCartProductAdapter productAdapter;
    private RecipeAdapter recipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cart_registration);

        productDAO = new ProductDAO(this);
        recipeDAO = new RecipeDAO(this);

        products = (ArrayList<Product>) productDAO.getAll();
        recipes = (ArrayList<Recipe>) recipeDAO.getAll();

        searchView = findViewById(R.id.search_itemCartRegistration);
        listView = findViewById(R.id.listView_itemCartRegistration);
        btnCancel = findViewById(R.id.registration_itemCart_cancel);
        rbtnProducts = findViewById(R.id.registration_itemCart_rbtnProducts);
        rbtnRecipes = findViewById(R.id.registration_itemCart_rbtnRecipes);


        productAdapter = new ItemCartProductAdapter( products,this);
       // RecipeAdapter recipeAdapter = new RecipeAdapter((ObservableArrayList<Recipe>) recipes,this);


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
                if(rbtnProducts.isChecked()){
                    productAdapter.getFilter().filter(s);
                } else if ( rbtnRecipes.isChecked()){
                    //recipeAdapter
                }

                return false;
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.registration_itemCart_rbtnProducts:
                if(checked){
                    listView.setAdapter(productAdapter);
                }
                break;
            case R.id.registration_itemCart_rbtnRecipes:
                if(checked){
                    listView.setAdapter(recipeAdapter);
                }
                break;
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.register_product,menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        MenuItem searchItem = menu.findItem(R.id.search_itemCart);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setQueryHint("Adicione um novo item");
//
//
//
//        return true;
//    }
}
