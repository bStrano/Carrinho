package br.com.stralom.compras;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;

import br.com.stralom.adapters.StockAdapter;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.StockDAO;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Stock;
import br.com.stralom.helper.ItemStockForm;
import br.com.stralom.helper.SwipeToDeleteCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockMain extends Fragment {
    private static final String TAG = "StockMain";
    ItemStockDAO itemStockDAO;
    ProductDAO productDAO;
    private StockDAO stockDAO;
    private Stock stock;
    private FloatingActionButton fabAddStock, fabUpdateStock, fabMain;
    private boolean fabPressed = false;
    private RecyclerView productsStockView;

    public StockMain() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //DAOS
        itemStockDAO = new ItemStockDAO(getContext());
        productDAO = new ProductDAO(getContext());
        stockDAO = new StockDAO(getContext());

        stock = stockDAO.findById((long) 1);
        if(stock == null){
            stock = new Stock((long) 1);
            stockDAO.add(stockDAO.getContentValues(stock));
        }
        //VIEWS
        View view = inflater.inflate(R.layout.fragment_stock_main, container, false);
        productsStockView = view.findViewById(R.id.list_itemStock);
        final ArrayList<ItemStock> productsStock = (ArrayList<ItemStock>) itemStockDAO.getAll(stock.getId());
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
                    if(fabPressed == true) {
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
                final View viewDialog = getLayoutInflater().inflate(R.layout.dialog_itemstock_registration,null);
                // Load Products Spinner
                Spinner spinner = viewDialog.findViewById(R.id.list_productsStock);
                ArrayList<Product> products = (ArrayList<Product>) productDAO.getAll();
                ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, products);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                // Load Alert Dialog
                Log.i(TAG,stock.toString());
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle(R.string.stock_dialogTitle_addProduct);
                dialogBuilder.setView(viewDialog);
                dialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ItemStockForm itemStockForm = new ItemStockForm(viewDialog);
                        ItemStock itemStock = itemStockForm.getItemStock();
                        itemStock.setStock(stock);
                        Log.e(TAG,"Quantidade atual: " + itemStock.getActualAmount());
                        Log.e(TAG,"Quantidade Máxima: " + itemStock.getAmount());
                        itemStockDAO.add(itemStockDAO.getContentValues(itemStock));
                        getActivity().recreate();
                        Toast.makeText(getContext(),"Produto adicionado.", Toast.LENGTH_LONG);
                    }
                });
                dialogBuilder.setNegativeButton(R.string.cancel, null);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                //
            }
        });

        return view;
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
