package br.com.stralom.compras;

import android.database.sqlite.SQLiteConstraintException;
import android.databinding.ObservableArrayList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.stralom.adapters.ProductAdapter;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Stock;
import br.com.stralom.helper.exceptions.InvalidElementForm;
import br.com.stralom.helper.forms.ItemStockForm;
import br.com.stralom.interfaces.ItemClickListener;


public class StockRegistration extends AppCompatActivity  {
    private static final String TAG = "STOCK_REGISTRATION";
    private Stock stock ;

    private Toolbar toolbar;
    private RecyclerView productsList;
    private SearchView productsSearchView;

    private Group selectedProductGroup;
    private TextView selectedProductView;
    private Group unselectedProductGroup;
    private TextView selectProductTitle;

    private ArrayList<Product> products ;
    private Product selectedProduct;

    private ProductAdapter productAdapter;
    private ItemStockForm itemStockForm;

    private ProductDAO productDAO;
    private ItemStockDAO itemStockDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_registration);

        //Temporary
        stock = new Stock((long) 1);

        productDAO = new ProductDAO(this);
        itemStockDAO = new ItemStockDAO(this);

        itemStockForm = new ItemStockForm(this);

        toolbar = findViewById(R.id.registration_toolbar);
        productsList = findViewById(R.id.stock_registration_products);
        productsSearchView = findViewById(R.id.stock_registration_search);
        selectedProductGroup = findViewById(R.id.stock_registration_group_selectedProduct);
        unselectedProductGroup = findViewById(R.id.stock_registration_group_productList);
        selectedProductView = findViewById(R.id.stock_registration_selectedProduct);
        selectProductTitle = findViewById(R.id.stock_regidtration_selectProductTitle);

        products = (ArrayList<Product>) productDAO.getAllOrderedByNameWithoutItemStock();

        toolbar.setTitle(R.string.stock_register);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        for (int id: selectedProductGroup.getReferencedIds()) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unSelectProduct();
                }
            });
        }


        productAdapter = new ProductAdapter((ObservableArrayList<Product>) products, this, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                selectedProduct = products.get(position);
                selectProduct();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });

        productsList.setAdapter(productAdapter);
        productsList.setLayoutManager(new LinearLayoutManager(this));
        productsList.setHasFixedSize(true);



        productsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void selectProduct(){
        unselectedProductGroup.setVisibility(View.GONE);
        selectedProductGroup.setVisibility(View.VISIBLE);
        selectedProductView.setText(selectedProduct.getName());
        selectProductTitle.setTextColor(Color.BLACK);
    }

    public void unSelectProduct(){
        unselectedProductGroup.setVisibility(View.VISIBLE);
        selectedProductGroup.setVisibility(View.GONE);
        selectedProductView.setText("");
        selectedProduct = null;
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
                try {
                    ItemStock itemStock = itemStockForm.getValidItemStock(selectedProduct);
                    itemStock.setStock(stock);
                    Long id = itemStockDAO.add(itemStockDAO.getContentValues(itemStock));
                    itemStock.setId(id);
//                    Intent intent = new Intent();
//                    intent.putExtra("itemStock", itemStock);
//                    setResult(RESULT_OK,intent);

                    finish();
                } catch (InvalidElementForm invalidElementForm) {
                    if(invalidElementForm.getErrorCode() == ItemStockForm.EMPTY_PRODUCT_ERRORCODE){
                        selectProductTitle.setTextColor(Color.RED);
                    }
                    Log.w(TAG,invalidElementForm);
                } catch ( SQLiteConstraintException e){
                    if(e.getMessage().contains("code 2067")){
                        Toast.makeText(this,R.string.error_product_alreadyRegistered, Toast.LENGTH_LONG).show();
                    } else {
                        e.printStackTrace();
                    }
                }

        }
        return true;
    }


}
