package br.com.stralom.compras;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import br.com.stralom.adapters.StockAdapter;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.StockDAO;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Stock;
import br.com.stralom.helper.ItemStockForm;
import br.com.stralom.helper.SwipeToDeleteCallback;

import static br.com.stralom.helper.BasicViewHelper.hideSoftKeyBoard;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockMain extends Fragment {
    private static final String TAG = "StockMain";
    private ItemStockDAO itemStockDAO;
    private ProductDAO productDAO;
    private Stock stock;
    private FloatingActionButton fabAddStock, fabUpdateStock, fabMain;
    private boolean fabPressed = false;
    private RecyclerView productsStockView;
    private ArrayList<ItemStock> productsStock;

    public StockMain() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //DAOS
        itemStockDAO = new ItemStockDAO(getContext());
        productDAO = new ProductDAO(getContext());
        StockDAO stockDAO = new StockDAO(getContext());

        stock = stockDAO.findById((long) 1);
        if(stock == null){
            stock = new Stock((long) 1);
            stockDAO.add(stockDAO.getContentValues(stock));
        }
        //VIEWS
        View view = inflater.inflate(R.layout.fragment_stock_main, container, false);
        productsStockView = view.findViewById(R.id.list_itemStock);
        productsStock = (ArrayList<ItemStock>) itemStockDAO.getAll(stock.getId());
        StockAdapter adapter = new StockAdapter(productsStock,getActivity());
        productsStockView.setAdapter(adapter);
        productsStockView.setHasFixedSize(true);
        productsStockView.setLayoutManager(new LinearLayoutManager(getContext()));


        ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(productsStockView);




         // Button newItemStock = view.findViewById(R.id.btn_newItemStock);

        fabAddStock = view.findViewById(R.id.fab_addStock);
        fabUpdateStock = view.findViewById(R.id.fab_addRecipe);
        fabMain = view.findViewById(R.id.fab_stock);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fabPressed){
                    openFabMenu();
                }else {
                    closeFabMenu();
                }
            }
        });




        productsStockView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy>0){
                    hideFabs();
                    if(fabPressed) {
                        closeFabMenu();
                    }
                } else {
                    showFabs();
                }
                super.onScrolled(recyclerView, dx, dy);
            }

        });

        fabAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFabMenu();
                final View viewDialog = getLayoutInflater().inflate(R.layout.dialog_itemstock_registration,null);
                // Load Products Spinner
                Spinner spinner = viewDialog.findViewById(R.id.form_itemStock_products);
                ArrayList<Product> products = (ArrayList<Product>) productDAO.getAll();
                ArrayAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_item, products);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                // Load Alert Dialog
                Log.i(TAG,stock.toString());
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle(R.string.stock_dialogTitle_addProduct);
                dialogBuilder.setView(viewDialog);
                dialogBuilder.setPositiveButton(R.string.save, null);
                dialogBuilder.setNegativeButton(R.string.cancel, null);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(registerItemStock(viewDialog)){
                            dialog.dismiss();
                        }
                    }
                });

                //
            }
        });

        return view;
    }

    /**
     * Register a item if it is valid.
     * @param viewDialog Dialog View
     * @return  true if the item is valid and false otherwise.
     */
    private boolean registerItemStock(View viewDialog) {
        ItemStockForm itemStockForm = new ItemStockForm(getActivity(),viewDialog);
        itemStockForm.getValidator().validate();

        if(itemStockForm.isValidationSuccessful()){
            ItemStock itemStock = itemStockForm.getItemStock();
            itemStock.setStock(stock);
            if(itemStockDAO.findByProductName(itemStock.getProduct().getName()) == null) {
                Long id = itemStockDAO.add(itemStockDAO.getContentValues(itemStock));
                itemStock.setId(id);
                productsStock.add(itemStock);
                productsStockView.getAdapter().notifyDataSetChanged();
                Toast.makeText(getContext(),R.string.toast_itemStock_validRegistration, Toast.LENGTH_LONG).show();
                return  true;
            } else {
                hideSoftKeyBoard(getContext(),viewDialog);
                Toast toast = Toast.makeText(getContext(),R.string.toast_itemStock_invalidRegistration, Toast.LENGTH_LONG);
                TextView toastView = toast.getView().findViewById(android.R.id.message);
                toastView.setTextColor(Color.RED);
                toast.show();


                return false;
            }

        } else {
            return false;
        }
    }

    private void closeFabMenu() {
        fabMain.animate().rotation(-0F);
        fabPressed = false;
        fabUpdateStock.animate().translationY(0);
        fabAddStock.animate().translationY(0);
    }

    private void openFabMenu() {
        fabMain.animate().rotation(135.0F);
        fabPressed = true;
        fabAddStock.animate().translationY(-getResources().getDimension(R.dimen.fab_61));
        fabUpdateStock.animate().translationY(-getResources().getDimension(R.dimen.fab_111));

    }

    private void hideFabs(){
        fabMain.hide();
        fabAddStock.hide();
        fabUpdateStock.hide();
    }

    private void showFabs(){
        fabMain.show();
        fabUpdateStock.show();
        fabAddStock.show();
    }
}
