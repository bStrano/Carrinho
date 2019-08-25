package br.com.stralom.compras.activities;


import android.content.Intent;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.stralom.compras.adapters.CartAdapter;
import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.CartDAO;
import br.com.stralom.compras.dao.CategoryDAO;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.dao.SimpleItemDAO;
import br.com.stralom.compras.entities.Cart;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.helper.BasicViewHelper;
import br.com.stralom.compras.interfaces.EditMenuInterface;
import br.com.stralom.compras.interfaces.ItemCheckListener;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartMain extends BasicViewHelper<Product> implements ItemCheckListener {
    private static final int REGISTRATION_REQUEST = 22654;
    private ProductDAO productDAO;
    private CartDAO cartDAO;
    private CategoryDAO categoryDAO;
    private Cart cart;
    private SimpleItemDAO simpleItemDAO;
    private Category temporaryProductsCategory;
    private boolean setUpSections = true;
    private Snackbar confirmConcludeElement;
    private CartAdapter adapter;

    private static final String TAG = "CartMainTAG";

    public CartMain() {
    }


    @Override
    protected void onClickManagementMenu(View view, int position) {
        int viewType = adapter.getItemViewType(position);
        if(viewType == 1 ){
            super.onClickManagementMenu(view, position);
        }

    }

    @Override
    protected void onLongClickManagementMenu(View view, int position, EditMenuInterface editMenuInterface) {
        int viewType = adapter.getItemViewType(position);
        if(viewType == 1 ){
            super.onLongClickManagementMenu(view, position, editMenuInterface);
        }

    }

    @Override
    public void initializeSuperAttributes() {
        list = new ObservableArrayList<>();
        listView = mainView.findViewById(R.id.cart_list_itemCarts);
        managementMenu = mainView.findViewById(R.id.cart_management_list);
        fab = mainView.findViewById(R.id.itemCart_btn_registerProduct);
    }

    @Override
    public boolean callChangeItemBackgroundColor(View view, int position) {
        ViewGroup background = view.findViewById(R.id.cart_view_foreground);
        return ((CartAdapter) listView.getAdapter()).changeItemBackgroundColor(background,position);
    }




    @Override
    public void onResume() {
        Log.d(TAG,"ONRESUME");
        if(setUpSections){
            Log.d(TAG,"ONRESUME2");
            list.clear();
            loadItemsFromCart();
            setUpSections = false;
        }
        super.onResume();
    }


    public void onPause() {

        setUpSections = true;
        super.onPause();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_cart_main, container, false);

        setUpSections = false;

        initializeSuperAttributes();

        //DAO

        productDAO = new ProductDAO(getContext());



        temporaryProductsCategory = new Category("Temporary");



        setUpEmptyListView(mainView, list,R.id.itemCart_emptyList,R.drawable.ic_cart, R.string.itemCart_emptyList_title,R.string.itemCart_emptyList_description);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(this,list,getActivity());
        listView.setAdapter(adapter);

        loadItemsFromCart();

        setUpManagementMenu(adapter);





//        ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(adapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(listView);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadItemCartDialog();
                Intent intent = new Intent(getActivity(), ItemCartRegistration.class);
                intent.putExtra("products",((MainActivity) getActivity()).productList);
                intent.putExtra("recipes",((MainActivity) getActivity()).recipeList);
                startActivityForResult(intent,REGISTRATION_REQUEST);
            }
        });


        return mainView;
    }





    private void loadItemsFromCart() {
        list.clear();

        ArrayList<Product> products = ((MainActivity) getActivity()).productList;
        for(Product product : products){
            if(product.getItemCart().getAmount() > 0) {
                this.list.add(product);
            }
        }
      //  ArrayList<SimpleItem> simpleItemList = (ArrayList<SimpleItem>) simpleItemDAO.getAll(cart.getId());
       // addSimpleProducts(simpleItemList);
       // list.addAll(itemCartDAO.getAllOrderedByCategory(cart.getId()));
//        cart.setListItemCart(this.list);
        adapter.customNotifyDataSetChanged();

    }






//    private void addSimpleProducts(ArrayList<SimpleItem> simpleItemList){
//        if(simpleItemList.size() > 0) {
//4
//            for (SimpleItem simpleItem:simpleItemList) {
//                ItemCart itemCart = simpleItem.convertToItemCart(simpleItem);
//                itemCart.getProduct().setCategory(temporaryProductsCategory);
//                list.add(itemCart);
//            }
//
//        }
//    }


//    private boolean checkForItemWithName(String name){
//        for (ItemCart itemCart: list) {
//            if(itemCart.getProduct().getName().equals(name)){
//                return true;
//            }
//        }
//        return false;
//    }

    // https://stackoverflow.com/questions/9590386/fragment-onhiddenchanged-not-called
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if(adapter != null){
                adapter.dismissSnackbar();
            }

            if(confirmConcludeElement != null){
                confirmConcludeElement.dismiss();
            }

        }
    }

    public void createConfirmSnackBar(final Pair<Product,Integer> itemCartPair){
        FrameLayout main = getActivity().findViewById(R.id.main_frame);
        confirmConcludeElement = Snackbar.make(main,R.string.cart_snackbar_confirmCheck, Snackbar.LENGTH_SHORT);
        confirmConcludeElement.setAction(R.string.snackbar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter.undoConcludedElement(itemCartPair);
            }
        });


        confirmConcludeElement.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                confirmConcludeElement = null;
                if(itemCartPair.first != null){

                    if(itemCartPair.first.getItemCart().isRemoved()){
                        confirmConclude(itemCartPair.first);
                        adapter.customNotifyDataSetChanged();
                    }


                   super.onDismissed(transientBottomBar, event);
                }

            }

            @Override
            public void onShown(Snackbar transientBottomBar) {
                super.onShown(transientBottomBar);
            }
        });



        confirmConcludeElement.show();


    }

    public void confirmConclude(final Product product) {
        if (product.getId() != null) {
            productDAO.remove(product.getId());
            boolean containsProduct = false;
            for(Product item : list){
                if(item.getId() == product.getId()){
                    containsProduct = true;
                    break;
                }
            }
            if (containsProduct) {
                product.getItemStock().setActualAmount(product.getItemStock().getActualAmount() + product.getItemCart().getAmount());
                productDAO.update(product);
            }
//        } else if(product.getConvertedId() != null){
//            simpleItemDAO.remove( product.getConvertedId());
//
//        }
            adapter.setConcludedElement(null);
        }
    }


    @Override
    public void showConfirmSnackbar() {
        if(confirmConcludeElement != null) {
            confirmConcludeElement.dismiss();
        }
        createConfirmSnackBar(adapter.getConcludedElement());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, String.valueOf(requestCode));
        if(requestCode == REGISTRATION_REQUEST){
            Log.d(TAG, String.valueOf(resultCode));
            if(resultCode == RESULT_OK){
                Log.d(TAG, "Update");
                ArrayList<Product> products = (ArrayList<Product>) data.getExtras().get("products");
                ((MainActivity) getActivity()).productList = products;
                loadItemsFromCart();
                }
            }
        }

    }
