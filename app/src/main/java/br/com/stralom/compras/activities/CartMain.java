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

import java.util.ArrayList;

import br.com.stralom.compras.adapters.CartAdapter;
import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.CartDAO;
import br.com.stralom.compras.dao.CategoryDAO;
import br.com.stralom.compras.dao.DBHelper;
import br.com.stralom.compras.dao.ItemCartDAO;
import br.com.stralom.compras.dao.ItemStockDAO;
import br.com.stralom.compras.dao.SimpleItemDAO;
import br.com.stralom.compras.entities.Cart;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.ItemCart;
import br.com.stralom.compras.entities.ItemStock;
import br.com.stralom.compras.entities.SimpleItem;
import br.com.stralom.compras.helper.BasicViewHelper;
import br.com.stralom.compras.interfaces.EditMenuInterface;
import br.com.stralom.compras.interfaces.ItemCheckListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartMain extends BasicViewHelper<ItemCart> implements ItemCheckListener {
    private ItemCartDAO itemCartDAO;
    private CartDAO cartDAO;
    private CategoryDAO categoryDAO;
    private Cart cart;
    private SimpleItemDAO simpleItemDAO;
    private ItemStockDAO itemStockDAO;
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
        cartDAO = new CartDAO(getContext());
        itemCartDAO = new ItemCartDAO(getContext());
        simpleItemDAO = new SimpleItemDAO(getContext());
        categoryDAO = new CategoryDAO(getContext());
        itemStockDAO = new ItemStockDAO(getContext());


        temporaryProductsCategory = categoryDAO.findByName(DBHelper.CATEGORY_TEMPORARY_PRODUCT);

        cart = cartDAO.findById((long) 1);



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
                startActivityForResult(intent,1);
            }
        });


        return mainView;
    }





    private void loadItemsFromCart() {

        ArrayList<SimpleItem> simpleItemList = (ArrayList<SimpleItem>) simpleItemDAO.getAll(cart.getId());
        addSimpleProducts(simpleItemList);
        list.addAll(itemCartDAO.getAllOrderedByCategory(cart.getId()));
        cart.setListItemCart(list);
        adapter.customNotifyDataSetChanged();

    }






    private void addSimpleProducts(ArrayList<SimpleItem> simpleItemList){
        if(simpleItemList.size() > 0) {

            for (SimpleItem simpleItem:simpleItemList) {
                ItemCart itemCart = simpleItem.convertToItemCart(simpleItem);
                itemCart.getProduct().setCategory(temporaryProductsCategory);
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

            if(confirmConcludeElement != null){
                confirmConcludeElement.dismiss();
            }

        }
    }

    public void createConfirmSnackBar(final Pair<ItemCart,Integer> itemCartPair){
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

                    if(itemCartPair.first.isRemoved()){
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

    public void confirmConclude(ItemCart itemCart) {
        if(itemCart.getId() != null) {
            itemCartDAO.remove( itemCart.getId());
            ItemStock itemStock = itemStockDAO.findByProductName(itemCart.getProduct().getName());
            if(itemStock != null){
                itemStock.setActualAmount(itemStock.getActualAmount() + itemCart.getAmount());
                itemStockDAO.update(itemStock);
            }
        } else if(itemCart.getConvertedId() != null){
            simpleItemDAO.remove( itemCart.getConvertedId());

        }
        adapter.setConcludedElement(null);
    }


    @Override
    public void showConfirmSnackbar() {
        if(confirmConcludeElement != null) {
            confirmConcludeElement.dismiss();
        }
        createConfirmSnackBar(adapter.getConcludedElement());
    }
}


