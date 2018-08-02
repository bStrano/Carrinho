package br.com.stralom.compras;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.stralom.adapters.ProductAdapter;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.entities.Product;
import br.com.stralom.helper.BasicViewHelper;

import static android.app.Activity.RESULT_OK;


public class ProductMain extends BasicViewHelper<Product> {
    private static final int REGISTRATION_REQUEST = 1;
    private ProductAdapter productAdapter;
    private ProductDAO productDAO;

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


        productDAO = new ProductDAO(getActivity());
        list = (ObservableArrayList<Product>) productDAO.getAll();
        setUpEmptyListView(mainView, list,R.id.product_emptyList, R.drawable.ic_info, R.string.product_emptyList_title,R.string.product_emptyList_description);
        //registerForContextMenu(listView);

       productAdapter = new ProductAdapter(list,getActivity());
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setHasFixedSize(true);
        listView.setAdapter(productAdapter);

        setUpManagementMenu(productAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductRegistration.class);
                startActivityForResult(intent,REGISTRATION_REQUEST);
            }

//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                final View newProductView = getLayoutInflater().inflate(R.layout.product_registration,null);
//                final Spinner spinner = newProductView.findViewById(R.id.form_productCategory);
//                CategoryDAO categoryDAO = new CategoryDAO(getContext());
//                final ArrayList<Category> categories = categoryDAO.getAll();
//
//
//                CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getContext(),categories);
//
//                //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(adapter);
//
//                final ProductForm productForm = new ProductForm(getActivity(),newProductView);
//
//
//                builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                });
//                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getActivity(), "Registro cancelado", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//
//                builder.setView(newProductView);
//                final AlertDialog dialog = builder.create();
//                dialog.show();
//                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        productForm.getValidator().validate();
//                        if(productForm.isValidationSuccessful()){
//                            Product product = productForm.getProduct();
//                            try{
//                                Long id = productDAO.add(product);
//                                product.setId(id);
//                                list.add(product);
//                                productAdapter.notifyDataSetChanged();
//                                Toast.makeText(getActivity(),R.string.toast_produc_register,Toast.LENGTH_LONG).show();
//                            }catch (SQLiteConstraintException e){
//                                Toast.makeText(getActivity(),R.string.toast_product_alreadyRegistered,Toast.LENGTH_LONG).show();
//                            }
//                            dialog.dismiss();
//                        }
//                    }
//
//                });
//
//            }
        });
        return mainView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTRATION_REQUEST){
            if(resultCode == RESULT_OK){
                Product product = data.getParcelableExtra("product");
                if(product != null){
                    list.add(product);
                    listView.getAdapter().notifyDataSetChanged();
                }
            }
        }

    }







}
