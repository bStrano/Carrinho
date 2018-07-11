package br.com.stralom.compras;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.adapters.CartAdapter;
import br.com.stralom.adapters.RecipeSpinnerAdapter;
import br.com.stralom.dao.CartDAO;
import br.com.stralom.dao.ItemCartDAO;
import br.com.stralom.dao.ItemRecipeDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.dao.SimpleItemDAO;
import br.com.stralom.entities.Cart;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;
import br.com.stralom.entities.SimpleItem;
import br.com.stralom.helper.BasicViewHelper;
import br.com.stralom.helper.ItemCartForm;
import br.com.stralom.helper.SimpleItemForm;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartMain extends BasicViewHelper<ItemCart>{
    private ProductDAO productDAO;
    private ItemCartDAO itemCartDAO;
    private ItemRecipeDAO itemRecipeDAO;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_cart_main, container, false);

        initializeSuperAttributes();

        //DAO
        cartDAO = new CartDAO(getContext());
        itemCartDAO = new ItemCartDAO(getContext());
        productDAO = new ProductDAO(getContext());
        recipeDAO = new RecipeDAO(getContext());
        itemRecipeDAO = new ItemRecipeDAO(getContext());
        simpleItemDAO = new SimpleItemDAO(getContext());

        // Views
        FloatingActionButton btn_addRecipe = mainView.findViewById(R.id.itemCart_btn_registerRecipe);
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
                loadItemCartDialog();
            }
        });
        btn_addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRecipeItemCartDialog();
            }
        });
        btn_addSimpleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {loadSimpleProductItemCartDialog();
            }
        });

        return mainView;
    }




    /**
     * Obtem todos os item contidos no {@code cart}
     * @param cart
     */
    private void loadItemsFromCart(Cart cart) {
        list = (ObservableArrayList<ItemCart>) itemCartDAO.getAll(cart.getId());
        cart.setListItemCart(list);
        ArrayList<SimpleItem> simpleItemList = (ArrayList<SimpleItem>) simpleItemDAO.getAll(cart.getId());
        addSimpleProducts(simpleItemList);
    }



    private void loadRecipeItemCartDialog() {
        final View recipeItemCartView = getLayoutInflater().inflate(R.layout.dialog_itemcart_recipe_registration,null);

        final Spinner recipeSpinner = recipeItemCartView.findViewById(R.id.list_itemCart_recipe);
        ArrayList<Recipe> recipes = (ArrayList<Recipe>) recipeDAO.getAll();
        RecipeSpinnerAdapter recipeSpinnerAdapter = new RecipeSpinnerAdapter(recipes,getContext(),getLayoutInflater());
        recipeSpinner.setAdapter(recipeSpinnerAdapter);

        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Recipe recipe = (Recipe) recipeSpinner.getSelectedItem();
                List<ItemRecipe> itemRecipes = itemRecipeDAO.getAllByRecipe(recipe.getId());
                for (ItemRecipe itemRecipe :itemRecipes){
                    addItemFromRecipe(itemRecipe);
                }
                // reloadItemsFromCart(cart,fragmentView);

            }
        };


        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { }
        };

        createDialog(recipeItemCartView,confirmListener,cancelListener,R.string.cart_dialogTitle_addRecipe).show();



    }
    private void loadItemCartDialog() {
        final View itemCartView = getLayoutInflater().inflate(R.layout.dialog_itemcart_registration,null);

        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //addItemFromProduct(itemCartView);
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),R.string.cancel,Toast.LENGTH_LONG).show();
            }
        };

        final AlertDialog dialog = createDialog(itemCartView,confirmListener,cancelListener,R.string.cart_dialogTitle_addProduct);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addItemFromProduct(itemCartView)){
                    dialog.dismiss();
                }
            }
        });


        Spinner productSpinner = itemCartView.findViewById(R.id.itemcart_form_product);
        List<Product> products = productDAO.getAll();
        loadSpinner(productSpinner, products);

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

    private void addItem(ItemCart newItemCart){
        ItemCart itemCartdb =itemCartDAO.getByProductName(newItemCart.getProduct().getName());
        if(itemCartdb == null){
            Long id = itemCartDAO.add(newItemCart);
            newItemCart.setId(id);
            Toast.makeText(getContext(),R.string.itemCart_toast_productAdded,Toast.LENGTH_LONG).show();
            list.add(newItemCart);
            listView.getAdapter().notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(),R.string.itemCart_toast_productAlreadyRegistered,Toast.LENGTH_LONG).show();
        }



    }
    /**
     * Add a ingredient of a Recipe to the cart.
     * Update the ingredient if it already was on the cart.
     * @param itemRecipe    Ingredient of a Recipe
     */
    private void addItemFromRecipe(ItemRecipe itemRecipe) {
        //ItemCart itemCart = itemCartDAO.getByProductName(itemRecipe.getProduct().getName());
        int index = -1;
        ItemCart itemCart = null;
        for(int i = 0; i< list.size() ; i++){
            if(list.get(i).getProduct().getName().equals(itemRecipe.getProduct().getName())){
                itemCart = list.get(i);
                index = i;
            }
        }
        if(index == -1){
            ItemCart convertedItemCart = new ItemCart(itemRecipe.getProduct(), itemRecipe.getAmount(), cart);
            addItem(convertedItemCart);
        } else {
            int updatedAmount = itemCart.getAmount() + itemRecipe.getAmount();
            itemCart.setAmount(updatedAmount);
            list.get(index).setAmount(updatedAmount);


            itemCartDAO.update(itemCart);
            list.get(index).setAmount(updatedAmount);
            listView.getAdapter().notifyDataSetChanged();
        }
    }
    /**
     * Add a new item to the cart, from a Product.
     * @param itemCartView an Dialog that contains the form
     * @return if the values are valid
     */
    private boolean addItemFromProduct(View itemCartView) {
        ItemCartForm itemCartForm = new ItemCartForm(getActivity(),itemCartView, cart);
        itemCartForm.getValidator().validate();
        if(itemCartForm.isValidationSuccessful()){
            ItemCart itemCart = itemCartForm.getItemCart();
            addItem(itemCart);
            return true;
        }
        return  false;
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

                ItemCart simpleItemConverted = ItemCart.convertToItemCart(simpleItem);


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
                cart.getListItemCart().add(ItemCart.convertToItemCart(simpleItem));
            }

        }
    }

    /**
     *  Search for a product with a specific name in the list
     * @param name
     * @return if the list contains a product with name = @param name
     */
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


