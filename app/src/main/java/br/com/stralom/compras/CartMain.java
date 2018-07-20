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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.stralom.adapters.CartAdapter;
import br.com.stralom.dao.CartDAO;
import br.com.stralom.dao.ItemCartDAO;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.dao.SimpleItemDAO;
import br.com.stralom.entities.Cart;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.SimpleItem;
import br.com.stralom.helper.BasicViewHelper;
import br.com.stralom.helper.SimpleItemForm;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartMain extends BasicViewHelper<ItemCart>{
    private ItemCartDAO itemCartDAO;
    private RecipeDAO recipeDAO;
    private CartDAO cartDAO;
    private Cart cart;
    private SimpleItemDAO simpleItemDAO;


    private CartAdapter adapter;

    private static final String TAG = "CartMainTAG";

    public CartMain() {
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
            list.clear();
            list.addAll(itemCartDAO.getAll(cart.getId()));
            listView.getAdapter().notifyDataSetChanged();

        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_cart_main, container, false);

        initializeSuperAttributes();

        //DAO
        cartDAO = new CartDAO(getContext());
        itemCartDAO = new ItemCartDAO(getContext());
        recipeDAO = new RecipeDAO(getContext());
        simpleItemDAO = new SimpleItemDAO(getContext());


        FloatingActionButton btn_addSimpleItem = mainView.findViewById(R.id.itemcart_btn_registerSimpleProduct);
        FloatingActionButton btn_newItemCart = mainView.findViewById(R.id.itemCart_btn_registerProduct);

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



        btn_newItemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadItemCartDialog();
                Intent intent = new Intent(getActivity(), ItemCartRegistration.class);
                startActivityForResult(intent,1);
            }
        });
        btn_addSimpleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {loadSimpleProductItemCartDialog();
            }
        });

        return mainView;
    }





    private void loadItemsFromCart(Cart cart) {
        list = (ObservableArrayList<ItemCart>) itemCartDAO.getAll(cart.getId());
        cart.setListItemCart(list);
        ArrayList<SimpleItem> simpleItemList = (ArrayList<SimpleItem>) simpleItemDAO.getAll(cart.getId());
        addSimpleProducts(simpleItemList);
    }






    private void loadSimpleProductItemCartDialog(){
        final View simpleProductView = getLayoutInflater().inflate(R.layout.dialog_itemcart_simpleproduct_registration,null);
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //addItemFromSimpleProduct(view);
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),R.string.cancel,Toast.LENGTH_LONG).show();
            }
        };
        final AlertDialog dialog = createDialog(simpleProductView,confirmListener,null, R.string.cart_dialogTitle_addSimpleProduct);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addItemFromSimpleProduct(simpleProductView)){
                    dialog.dismiss();
                }
            }
        });
    }



    /**
     * -> Add a new item to the cart, from a Temporary Product.
     * -> Checks if the list already have a product with the Temporary Product name.
     *  Temporary Product - When removed of the list, he's completely removed of the application.
     * @param view an Dialog that contains the form
     * @return if the validation was successful
     */
    private boolean addItemFromSimpleProduct(View view){
        SimpleItemForm simpleItemForm = new SimpleItemForm(getActivity(),view,cart);
        simpleItemForm.getValidator().validate();
        if ( simpleItemForm.isValidationSuccessful()){
            SimpleItem simpleItem = simpleItemForm.getSimpleItem();
            if(!checkForItemWithName(simpleItem.getName())){
                Long id = simpleItemDAO.add(simpleItemDAO.getContentValues(simpleItem));
                simpleItem.setId(id);

                ItemCart simpleItemConverted = simpleItem.convertToItemCart(simpleItem);


                cart.getListItemCart().add(simpleItemConverted);
                listView.getAdapter().notifyDataSetChanged();
                return true;
            } else {
                Toast.makeText(getActivity(),R.string.itemCart_validation_simpleItemAlreadyExists,Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }
    private void addSimpleProducts(ArrayList<SimpleItem> simpleItemList){
        if(simpleItemList.size() > 0) {
            for (SimpleItem simpleItem:simpleItemList) {
                cart.getListItemCart().add(simpleItem.convertToItemCart(simpleItem));
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


