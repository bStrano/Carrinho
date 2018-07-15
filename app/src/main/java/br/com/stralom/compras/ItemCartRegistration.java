package br.com.stralom.compras;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.stralom.adapters.ItemCartRegistrationAdapter;
import br.com.stralom.adapters.ItemCartRegistrationProductAdapter;
import br.com.stralom.adapters.ItemCartRegistrationRecipeAdapter;
import br.com.stralom.adapters.RecipeAdapter;
import br.com.stralom.dao.ItemCartDAO;
import br.com.stralom.dao.ItemRecipeDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.entities.Cart;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;
import br.com.stralom.helper.ItemCartForm;


public class ItemCartRegistration extends AppCompatActivity {
    private ProductDAO productDAO;
    private RecipeDAO recipeDAO;
    private ItemCartDAO itemCartDAO;
    private ItemRecipeDAO itemRecipeDAO;

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
    private ItemCartRegistrationRecipeAdapter  recipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cart_registration);


        // ****************** Temporary ********************
        cart = new Cart((long) 1);

        productDAO = new ProductDAO(this);
        recipeDAO = new RecipeDAO(this);
        itemCartDAO = new ItemCartDAO(this);
        itemRecipeDAO = new ItemRecipeDAO(this);

        products = (ArrayList<Product>) productDAO.getAll();
        recipes = (ArrayList<Recipe>) recipeDAO.getAll();

        searchView = findViewById(R.id.search_itemCartRegistration);
        listView = findViewById(R.id.listView_itemCartRegistration);
        btnCancel = findViewById(R.id.registration_itemCart_cancel);
        btnConfirm = findViewById(R.id.registration_itemCart_confirm);
        rbtnProducts = findViewById(R.id.registration_itemCart_rbtnProducts);
        rbtnRecipes = findViewById(R.id.registration_itemCart_rbtnRecipes);


        productAdapter = new ItemCartRegistrationProductAdapter( products,this);
        recipeAdapter = new ItemCartRegistrationRecipeAdapter(recipes,this);


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
                    recipeAdapter.getFilter().filter(s);
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
                ArrayList<ItemCart> itemCarts  = new ArrayList<>();

                for (Map.Entry<Product, Integer> entry: productAdapter.getSelectedPositions().entrySet()) {
                    addItemFromProduct(entry);

                }

                for (Map.Entry<Recipe, Integer> entry: recipeAdapter.getSelectedPositions().entrySet()) {
                    addItemFromRecipe(entry);
                }
                finish();
            }
        });

    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        searchView.setQuery(null,true);
        ((ItemCartRegistrationAdapter) listView.getAdapter()).restoreList();
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


    /**
     * Add a new item to the cart, from a Product.
     */
    private void addItemFromProduct(Map.Entry<Product,Integer> entry) {
        ItemCart itemCart = new ItemCart(entry.getKey(),entry.getValue(),cart);
        addItem(itemCart);
    }


    private void addItemFromRecipe(Map.Entry<Recipe,Integer> entry) {
        //ItemCart itemCart = itemCartDAO.getByProductName(itemRecipe.getProduct().getName());
        Recipe recipe = entry.getKey();
        recipe.setIngredients(itemRecipeDAO.getAllByRecipe(recipe.getId()));
        for(ItemRecipe itemRecipe : recipe.getIngredients()){
            itemRecipe.setAmount(itemRecipe.getAmount() * entry.getValue());
            ItemCart itemCartDB = itemCartDAO.getByProductName(itemRecipe.getProduct().getName());
            if((itemCartDB == null )){
                itemCartDAO.add(itemRecipe.convertToItemCart(cart));

            } else {
                itemCartDB.setAmount(itemCartDB.getAmount() + itemRecipe.getAmount());
                itemCartDAO.update(itemCartDB);
            }
        }
    }

    private void addItem(ItemCart newItemCart){
        ItemCart itemCartdb =itemCartDAO.getByProductName(newItemCart.getProduct().getName());
        if(itemCartdb == null){
            Long id = itemCartDAO.add(newItemCart);
            newItemCart.setId(id);
            Toast.makeText(this,R.string.itemCart_toast_productAdded,Toast.LENGTH_LONG).show();
            //list.add(newItemCart);
            //listView.getAdapter().notifyDataSetChanged();
        } else {
            Toast.makeText(this,R.string.itemCart_toast_productAlreadyRegistered,Toast.LENGTH_LONG).show();
        }

    }
}
