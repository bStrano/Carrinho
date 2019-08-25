package br.com.stralom.compras.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;

import br.com.stralom.compras.adapters.IngredientsAdapter;
import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.ItemRecipe;
import br.com.stralom.compras.entities.Product;


public class RecipeIngredients extends AppCompatActivity {
    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private IngredientsAdapter ingredientsAdapter;

    private ArrayList<Product> products;
    private ArrayList<ItemRecipe> selectedIngredients;
    private ArrayList<ItemRecipe> ingredients;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);


        initializeViews();
        toolbarSetUp();
        listSetup();
        searchViewSetup();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secundary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.registration_save:
                ArrayList<ItemRecipe> selectedIngredients = ingredientsAdapter.getSelectedItems();
                Intent intent = new Intent();
                intent.putExtra("selectedIngredients",selectedIngredients);
                setResult(RESULT_OK,intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }



    private void toolbarSetUp(){
        toolbar.setTitle(R.string.registration_recipe_selectIngredient);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void listSetup(){

        Intent intent = getIntent();
        products = intent.getParcelableArrayListExtra("products");
        selectedIngredients = intent.getParcelableArrayListExtra("selectedIngredients");
        ingredients = new ArrayList<>();

       if(selectedIngredients == null) selectedIngredients = new ArrayList<>();
        ingredients.addAll(selectedIngredients);
        boolean hasIngredient ;
        for(Product product : products){
            hasIngredient = false;
          for(ItemRecipe itemRecipe: selectedIngredients){
              if(product.getName().equals(itemRecipe.getProduct().getName())){
                hasIngredient = true;
                break;
              }
          }
          if(!hasIngredient){
              ingredients.add(new ItemRecipe(0,product));
          }
        }

        Log.d("Bruno2",ingredients.toString());
        ingredientsAdapter = new IngredientsAdapter(this, ingredients);

        recyclerView.setAdapter(ingredientsAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void searchViewSetup(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ingredientsAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void initializeViews(){
        toolbar = findViewById(R.id.registration_recipe_ingredients_toolbar);
        searchView = findViewById(R.id.registration_recipe_ingredients_search);
        recyclerView = findViewById(R.id.registration_recipe_ingredients_productList);

    }

}
