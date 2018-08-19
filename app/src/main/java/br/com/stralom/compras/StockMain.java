package br.com.stralom.compras;


import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.com.stralom.adapters.StockAdapter;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.StockDAO;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Stock;
import br.com.stralom.helper.BasicViewHelper;
import br.com.stralom.helper.forms.ItemStockForm;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockMain extends BasicViewHelper<ItemStock> {
    private static final String TAG = "StockMain";
    private ItemStockDAO itemStockDAO;
    private ProductDAO productDAO;
    private Stock stock;
    private FloatingActionButton fabAddStock, fabUpdateStock;
    private boolean fabPressed = false;


    @Override
    public boolean callChangeItemBackgroundColor(View view, int position) {
        ViewGroup background = view.findViewById(R.id.stock_itemList_foregroundView);
        return ((StockAdapter) listView.getAdapter()).changeItemBackgroundColor(background,position);

    }



    @Override
    public void initializeSuperAttributes() {
        list = (ObservableArrayList<ItemStock>) itemStockDAO.getAll(stock.getId());
        listView = mainView.findViewById(R.id.list_itemStock);
        fab = mainView.findViewById(R.id.fab_addStock);
        managementMenu = mainView.findViewById(R.id.stock_management_list);
    }

    public StockMain() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_stock_main, container, false);



        //DAOS
        itemStockDAO = new ItemStockDAO(getContext());
        productDAO = new ProductDAO(getContext());
        StockDAO stockDAO = new StockDAO(getContext());

        stock = stockDAO.findById((long) 1);
        if(stock == null){
            stock = new Stock((long) 1);
            stockDAO.add(stockDAO.getContentValues(stock));
        }
        initializeSuperAttributes();


        //VIEWS
        View view = inflater.inflate(R.layout.fragment_stock_main, container, false);




        list = (ObservableArrayList<ItemStock>) itemStockDAO.getAll(stock.getId());





        setUpEmptyListView(mainView, list,R.id.stock_emptyList, R.drawable.ic_stock,R.string.stock_emptyList_title,R.string.stock_emptyList_description);

        StockAdapter adapter = new StockAdapter(list,getActivity());
        listView.setAdapter(adapter);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        setUpManagementMenu(adapter);



         // Button newItemStock = view.findViewById(R.id.btn_newItemStock);

        fabAddStock = mainView.findViewById(R.id.fab_addStock);






        fabAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getActivity(),StockRegistration.class);
               startActivity(intent);
            }
        });

        return mainView;
    }







    public void onResume() {
        //Temporary
        super.onResume();
        list.clear();
        list.addAll(itemStockDAO.getAll((long) 1));
        listView.getAdapter().notifyDataSetChanged();

    }


}
