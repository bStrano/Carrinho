package br.com.stralom.compras;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import br.com.stralom.adapters.ItemCartRegistrationAdapter;
import br.com.stralom.adapters.ItemCartRegistrationProductAdapter;
import br.com.stralom.adapters.ItemCartRegistrationRecipeAdapter;
import br.com.stralom.dao.DBHelper;
import br.com.stralom.dao.ItemCartDAO;
import br.com.stralom.dao.ItemRecipeDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.dao.SimpleItemDAO;
import br.com.stralom.entities.Cart;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;
import br.com.stralom.entities.SimpleItem;


public class ItemCartRegistration extends AppCompatActivity {
    private ProductDAO productDAO;
    private RecipeDAO recipeDAO;
    private ItemCartDAO itemCartDAO;
    private ItemRecipeDAO itemRecipeDAO;
    private SimpleItemDAO simpleItemDAO;

    private ArrayList<Product> products;
    private ArrayList<Recipe> recipes;
    private ArrayList<SimpleItem> temporaryProducts;

    private Cart cart;
    private SimpleItem temporaryProduct;

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

        Log.d("DEBUG", "ONCREATE");
        // ****************** Temporary ********************
        cart = new Cart((long) 1);

        productDAO = new ProductDAO(this);
        recipeDAO = new RecipeDAO(this);
        itemCartDAO = new ItemCartDAO(this);
        itemRecipeDAO = new ItemRecipeDAO(this);
        simpleItemDAO = new SimpleItemDAO(this);

       // products = (ArrayList<Product>) productDAO.getAll();
        products = productDAO.getAllProductsNotInsertedInTheCart(cart);
        Log.e("DEBUG", products.toString());
        recipes = (ArrayList<Recipe>) recipeDAO.getAll();
        temporaryProducts = new ArrayList<>();
        temporaryProduct = null;

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
                    productAdapter.ifNecessaryCreateOrUpdateSimpleProduct(s,cart);
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


    private void addItemFromProduct(Map.Entry<Product,Integer> entry) {
        if(isTemporaryProduct(entry)){
            SimpleItem simpleItem = new SimpleItem(entry.getKey().getName(), entry.getValue(), cart);
            simpleItemDAO.add(simpleItem);
        } else {
            ItemCart itemCart = new ItemCart(entry.getKey(), entry.getValue(), cart);
            addItem(itemCart);
        }

    }

    private boolean isTemporaryProduct(Map.Entry<Product, Integer> entry) {
        return entry.getKey().getCategory().getName().equals(DBHelper.TEMPORARY_PRODUCT_CATEGORY);
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
