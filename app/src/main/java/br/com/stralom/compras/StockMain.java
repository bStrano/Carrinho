package br.com.stralom.compras;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class StockMain extends Fragment {
    private static final String TAG = "StockMain";
    ItemStockDAO itemStockDAO;
    ProductDAO productDAO;
    private StockDAO stockDAO;
    private Stock stock;

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

        ListView productsStockView = view.findViewById(R.id.list_itemStock);
        ArrayList<ItemStock> productsStock = (ArrayList<ItemStock>) itemStockDAO.getAll(stock.getId());
        StockAdapter adapter = new StockAdapter(productsStock,getContext());
        productsStockView.setAdapter(adapter);





          Button newItemStock = view.findViewById(R.id.btn_newItemStock);
//        FloatingActionButton newItemStock = view.findViewById(R.id.btn_newItemStock);
        newItemStock.setOnClickListener(new View.OnClickListener() {
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
                dialogBuilder.setTitle(R.string.title_ItemStockRegistration);
                dialogBuilder.setView(viewDialog);
                dialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ItemStockForm itemStockForm = new ItemStockForm(viewDialog);
                        ItemStock itemStock = itemStockForm.getItemStock();
                        itemStock.setStock(stock);
                        Log.i(TAG,stock.toString());
                        itemStockDAO.add(itemStockDAO.getContentValues(itemStock));
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

}
