package br.com.stralom.compras;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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


import java.util.ArrayList;

import br.com.stralom.adapters.CategorySpinnerAdapter;
import br.com.stralom.dao.CategoryDAO;
import br.com.stralom.dao.ItemCartDAO;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.entities.Cart;
import br.com.stralom.entities.Category;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Stock;
import br.com.stralom.helper.forms.ProductForm;

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
                Product product = register();
                if(product !=  null){
                    Intent intent = new Intent();
                    intent.putExtra("product",product);
                    setResult(RESULT_OK,intent);
                    finish();
                }

    }
        return true;
    }



    private Product register(){
        Product product = productForm.getValidProduct();
        ItemCart itemCart = null;
        ItemStock itemStock = null;
        boolean isValid = true;
        if(product == null){
            isValid = false;
        }

        if(addStock.isChecked()){
            Pair<Integer,Integer> validStockAmounts = productForm.getValidStockAmounts();
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
            Long id = productDAO.add(product);
            product.setId(id);


            if(itemStock != null){
                itemStock.setProduct(product);
                itemStockDAO.add(itemStock);
            }
            if(itemCart != null){
                itemCart.setProduct(product);
                itemCartDAO.add(itemCart);
            }
            return product;
        }

    }



}
