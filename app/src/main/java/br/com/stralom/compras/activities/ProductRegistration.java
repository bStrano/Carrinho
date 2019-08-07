package br.com.stralom.compras.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import br.com.stralom.compras.adapters.CategorySpinnerAdapter;
import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.CategoryDAO;
import br.com.stralom.compras.dao.ItemCartDAO;
import br.com.stralom.compras.dao.ItemStockDAO;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.entities.Cart;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.ItemCart;
import br.com.stralom.compras.entities.ItemStock;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.entities.Stock;
import br.com.stralom.compras.helper.TriplePair;
import br.com.stralom.compras.helper.forms.ProductForm;

public class ProductRegistration extends AppCompatActivity {
    ProductForm productForm ;


    private CategoryDAO categoryDAO;
    private ProductDAO productDAO;
    private ItemStockDAO itemStockDAO;
    private ItemCartDAO itemCartDAO;

    private FrameLayout spinner_frame;
    private CheckBox addStock;
    private ConstraintLayout addStock_mainView;
    private TextView addCartTitle;
    private CheckBox addCart;

    private Spinner categoriesView;
    private TextInputLayout cartAmountLayout;






    private ArrayList<Category> categories;

    private CategorySpinnerAdapter categorySpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_registration);

        categoryDAO = new CategoryDAO(this);
        productDAO = new ProductDAO(this);
        itemStockDAO = new ItemStockDAO(this);
        itemCartDAO = new ItemCartDAO(this);

        Toolbar toolbar = findViewById(R.id.registration_toolbar);

        addStock = findViewById(R.id.registration_product_addStock);
        addStock_mainView = findViewById(R.id.registration_product_addStock_mainView);
        addCartTitle = findViewById(R.id.registration_product_addCartTitle);
        addCart = findViewById(R.id.registration_product_addCart);
        categoriesView = findViewById(R.id.registration_product_categories);
        cartAmountLayout = findViewById(R.id.registration_product_addCartAmountLayout);


        productForm = new ProductForm(this);

        categories = categoryDAO.getAll();

        categorySpinnerAdapter = new CategorySpinnerAdapter(this,categories);
        categoriesView.setAdapter(categorySpinnerAdapter);


        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);

        categoriesView.setAdapter(categorySpinnerAdapter);

        addCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    addCartTitle.setVisibility(View.GONE);
                    cartAmountLayout.setVisibility(View.VISIBLE);
                } else {
                    cartAmountLayout.setVisibility(View.INVISIBLE);
                    addCartTitle.setVisibility(View.VISIBLE);
                }
            }
        });

        addStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    addStock_mainView.setVisibility(View.VISIBLE);
                } else {
                    addStock_mainView.setVisibility(View.GONE);
                }
            }
        });




        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secundary_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.registration_save:
                TriplePair triplePair = register();
                if(triplePair !=  null){
                    Intent intent = new Intent();
                    intent.putExtra("product", (Product) triplePair.first);
                    setResult(RESULT_OK,intent);

                    finish();
                }

    }
        return true;
    }



    private TriplePair<Product,ItemStock,ItemCart> register(){
        Product product = productForm.getValidProduct();
        ItemCart itemCart = null;
        ItemStock itemStock = null;

        boolean isValid = true;
        if(product == null){
            isValid = false;
        }

        if(addStock.isChecked()){
            Pair<Double,Double> validStockAmounts = productForm.getValidStockAmounts();
            if(validStockAmounts == null){
                isValid = false;
            } else {
                itemStock = new ItemStock();
                //TEMPORARIO
                Stock stock = new Stock();
                stock.setId((long) 1);
                itemStock.setStock(stock);
                itemStock.setAmounts(validStockAmounts.first,validStockAmounts.second);
                Log.d("TESTE",itemStock.toString());
            }

        }
        if(addCart.isChecked()){
            double addCartAmount = productForm.getValidItemCartAmount();
            // TEMPORARIO
            Cart cart = new Cart();
            cart.setId((long) 1);
            if(addCartAmount == -1){
                isValid = false;
            } else {
                itemCart = new ItemCart();
                itemCart.setAmount(addCartAmount);
                itemCart.setCart(cart);
            }

        }

        if(!isValid){
            return null;
        } else {
            try{
                Long id = productDAO.add(product);
                product.setId(id);
            } catch (SQLiteConstraintException e){
                // coode 2067 - UNIQUE error code
                if(e.getMessage().contains("code 2067")){
                    Toast.makeText(this,R.string.error_product_alreadyRegistered, Toast.LENGTH_LONG).show();
                    return null;
                }


            }




            if(itemStock != null){
                itemStock.setProduct(product);
                itemStockDAO.add(itemStock);
            }
            if(itemCart != null){
                itemCart.setProduct(product);
                itemCartDAO.add(itemCart);
            }

            return new TriplePair<>(product,itemStock,itemCart);
        }

    }



}
