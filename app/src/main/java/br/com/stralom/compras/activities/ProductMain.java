package br.com.stralom.compras.activities;

import android.content.Intent;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;

import br.com.stralom.compras.adapters.ProductAdapter;
import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.helper.BasicViewHelper;

import static android.app.Activity.RESULT_OK;


public class ProductMain extends BasicViewHelper<Product> {
    private static final int REGISTRATION_REQUEST = 1;
    private ProductAdapter productAdapter;
    @Override
    public void initializeSuperAttributes() {
        listView = mainView.findViewById(R.id.product_list);
        managementMenu = mainView.findViewById(R.id.product_management_list);
        fab = mainView.findViewById(R.id.product_btn_addNew);
    }

    @Override
    public boolean callChangeItemBackgroundColor(View view, int position) {
        Log.d("DEBUG", "CALL CHANGE ITEM");
        ViewGroup background = view.findViewById(R.id.product_view_foreground);
        return ((ProductAdapter) listView.getAdapter()).changeItemBackgroundColor(background,position);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_product_main, container, false);
        initializeSuperAttributes();


//        ArrayList<Product> products1 = getArguments().getParcelableArrayList("products");
        ArrayList<Product> products1 = ((MainActivity) getActivity()).productList;

        ObservableArrayList<Product> products = new ObservableArrayList<Product>();
        products.addAll(products1);
        list = products;
        productAdapter = new ProductAdapter(list,getActivity(),null);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setHasFixedSize(true);
        listView.setAdapter(productAdapter);
        setUpManagementMenu(productAdapter);

        setUpEmptyListView(mainView, list,R.id.product_emptyList,R.drawable.ic_info, R.string.product_emptyList_title,R.string.product_emptyList_description);

        //registerForContextMenu(listView);






        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductRegistration.class);
                startActivityForResult(intent,REGISTRATION_REQUEST);
            }

        });
        return mainView;
    }

    private void addOrdered(Product newProduct){
        Product product;
        for (int i = 0 ; i < list.size() ; i ++) {
            product = list.get(i);
            if(product.getName().compareToIgnoreCase(newProduct.getName()) > 0 ){
                list.add(i,newProduct);
                listView.getAdapter().notifyItemInserted(i);
                return;
            }
        }
        list.add(newProduct);
        listView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTRATION_REQUEST){
            if(resultCode == RESULT_OK){
                Product product = data.getParcelableExtra("product");
                if(product != null){
                    boolean updated = false;
                    int index = 0;
                    for(Product productItem: this.list){

                        if(productItem.getName().toLowerCase().equals(product.getName().toLowerCase())){
                            updated = true;
                            this.list.set(index, product);
                            this.listView.getAdapter().notifyDataSetChanged();

                            break;
                        }
                        index++;
                    }

                    if(!updated){
                        addOrdered(product);
                    } else {
                        Toast.makeText(getContext(), R.string.toast_product_update, Toast.LENGTH_LONG).show();
                    }
                    ((MainActivity) getActivity()).productList = this.list;
                }
            }
        }

    }







}
