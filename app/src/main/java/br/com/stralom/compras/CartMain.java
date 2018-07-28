package br.com.stralom.compras;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.stralom.adapters.CartAdapter;
import br.com.stralom.dao.CartDAO;
import br.com.stralom.dao.CategoryDAO;
import br.com.stralom.dao.DBHelper;
import br.com.stralom.dao.ItemCartDAO;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.dao.SimpleItemDAO;
import br.com.stralom.entities.Cart;
import br.com.stralom.entities.Category;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.SimpleItem;
import br.com.stralom.helper.BasicViewHelper;
import br.com.stralom.helper.SimpleItemForm;
import br.com.stralom.interfaces.EditMenuInterface;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartMain extends BasicViewHelper<ItemCart>{
    private ItemCartDAO itemCartDAO;
    private RecipeDAO recipeDAO;
    private CartDAO cartDAO;
    private CategoryDAO categoryDAO;
    private Cart cart;
    private SimpleItemDAO simpleItemDAO;
    private Category temporyProductsCategory;
    private boolean setUpSections = true;


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
    public void callCleanBackgroundColor() {
        ((CartAdapter)listView.getAdapter()).cleanBackGroundColor();
    }

    @Override
    public void onResume() {
        Log.d(TAG,"ONRESUME");
        if(setUpSections){
            list.clear();
            addSimpleProducts((ArrayList<SimpleItem>) simpleItemDAO.getAll(cart.getId()));
            list.addAll(itemCartDAO.getAllOrderedByCategory(cart.getId()));

            adapter.customNotifyDataSetChanged();

        }
        super.onResume();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_cart_main, container, false);

        setUpSections = false;

        initializeSuperAttributes();

        //DAO
        cartDAO = new CartDAO(getContext());
        itemCartDAO = new ItemCartDAO(getContext());
        recipeDAO = new RecipeDAO(getContext());
        simpleItemDAO = new SimpleItemDAO(getContext());
        categoryDAO = new CategoryDAO(getContext());


        temporyProductsCategory = categoryDAO.findByName(DBHelper.TEMPORARY_PRODUCT_CATEGORY);


        cart = cartDAO.findById((long) 1);
        loadItemsFromCart(cart);


        setUpEmptyListView(mainView, list,R.id.itemCart_emptyList,R.drawable.ic_cart, R.string.itemCart_emptyList_title,R.string.itemCart_emptyList_description);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(list,getActivity());
        listView.setAdapter(adapter);


        setUpManagementMenu(adapter);





//        ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(adapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(listView);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadItemCartDialog();
                Intent intent = new Intent(getActivity(), ItemCartRegistration.class);
                startActivityForResult(intent,1);
            }
        });


        return mainView;
    }





    private void loadItemsFromCart(Cart cart) {

        list = (ObservableArrayList<ItemCart>) itemCartDAO.getAllOrderedByCategory(cart.getId());
        cart.setListItemCart(list);
        ArrayList<SimpleItem> simpleItemList = (ArrayList<SimpleItem>) simpleItemDAO.getAll(cart.getId());
        addSimpleProducts(simpleItemList);
    }







    private void addSimpleProducts(ArrayList<SimpleItem> simpleItemList){
        if(simpleItemList.size() > 0) {

            for (SimpleItem simpleItem:simpleItemList) {
                ItemCart itemCart = simpleItem.convertToItemCart(simpleItem);
                itemCart.getProduct().setCategory(temporyProductsCategory);
                list.add(itemCart);
            }

        }
    }


    private boolean checkForItemWithName(String name){
        for (ItemCart itemCart: list) {
            if(itemCart.getProduct().getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    // https://stackoverflow.com/questions/9590386/fragment-onhiddenchanged-not-called
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if(adapter != null){
                adapter.dismissSnackbar();
            }

        }
    }




}


