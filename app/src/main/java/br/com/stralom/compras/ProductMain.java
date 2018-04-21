package br.com.stralom.compras;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.stralom.adapters.CategorySpinnerAdapter;
import br.com.stralom.adapters.ProductAdapter;
import br.com.stralom.dao.CategoryDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.entities.Category;
import br.com.stralom.entities.Product;
import br.com.stralom.helper.ProductForm;
import br.com.stralom.helper.SwipeToDeleteCallback;

import static android.content.ContentValues.TAG;


public class ProductMain extends Fragment {
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private ProductDAO productDAO;



    public ProductMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_produtos, container, false);

        Button buttonView = view.findViewById(R.id.btn_newProduct);
        RecyclerView productListView = view.findViewById(R.id.list_products);

        productDAO = new ProductDAO(getActivity());
        productList = productDAO.getAll();
        Log.e(TAG,"SIZE: " + productList);
        registerForContextMenu(productListView);

        // Recycler View
       productAdapter = new ProductAdapter(productList,getActivity());
        productListView.setAdapter(productAdapter);
        productListView.setLayoutManager(new LinearLayoutManager(getContext()));
        productListView.setHasFixedSize(true);
        ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(productAdapter);
        ItemTouchHelper itemTouch = new ItemTouchHelper(callback);
        itemTouch.attachToRecyclerView(productListView);


        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View newProductView = getLayoutInflater().inflate(R.layout.product_registration,null);
                final Spinner spinner = newProductView.findViewById(R.id.form_productCategory);
                CategoryDAO categoryDAO = new CategoryDAO(getContext());
                final ArrayList<Category> categories = categoryDAO.getAll();


                CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getContext(),categories);

                //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                final ProductForm productForm = new ProductForm(newProductView);


                builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Product product = productForm.getProduct();
                        try{
                            productDAO.add(product);
                            productList.add(product);
                            productAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(),"Produto salvo!",Toast.LENGTH_LONG).show();
                        }catch (android.database.sqlite.SQLiteConstraintException e){
                            Toast.makeText(getActivity(),"Produto já está cadastrado.",Toast.LENGTH_LONG).show();
                        }


                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Registro cancelado", Toast.LENGTH_LONG).show();
                    }
                });


                builder.setView(newProductView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        inflater.inflate(R.menu.main_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_editId:
                Toast.makeText(getActivity(),"Produto editado!",Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_deleteId:
                Toast.makeText(getActivity(),"Produto deletado.", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }


}
