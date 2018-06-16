package br.com.stralom.compras;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TimingLogger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import br.com.stralom.adapters.CartAdapter;
import br.com.stralom.adapters.ItemClickListener;
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
import br.com.stralom.helper.SwipeToDeleteCallback;
import br.com.stralom.listeners.RecyclerTouchListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartMain extends Fragment  {
    private ProductDAO productDAO;
    private ItemCartDAO itemCartDAO;
    private ItemRecipeDAO itemRecipeDAO;
    private RecipeDAO recipeDAO;
    private Cart cart;
    private RecyclerView cartListView;
    private BasicViewHelper basicViewHelper;
    private SimpleItemDAO simpleItemDAO;
    private ArrayList<ItemCart> itemCartList;
    private CartAdapter adapter;
    private ViewPager viewPager;

    private static final String TAG = "CartMainTAG";

    public CartMain() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_main, container, false);

        // Helpers
        basicViewHelper = new BasicViewHelper(getActivity());

        //DAO
        CartDAO cartDAO = new CartDAO(getContext());
        itemCartDAO = new ItemCartDAO(getContext());
        productDAO = new ProductDAO(getContext());
        recipeDAO = new RecipeDAO(getContext());
        itemRecipeDAO = new ItemRecipeDAO(getContext());
        simpleItemDAO = new SimpleItemDAO(getContext());

        // Views
        cartListView = view.findViewById(R.id.cart_list_itemCarts);
        Button btn_addRecipe = view.findViewById(R.id.itemCart_btn_registerRecipe);
        Button btn_addSimpleItem = view.findViewById(R.id.itemcart_btn_registerSimpleProduct);
        Button btn_newItemCart = view.findViewById(R.id.itemCart_btn_registerProduct);

        cart = cartDAO.findById((long) 1);
        loadItemsFromCart(cart);

        cartListView.setHasFixedSize(true);
        cartListView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(itemCartList,getActivity());
        cartListView.setAdapter(adapter);


        cartListView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),cartListView, new ItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                ItemCart item = itemCartList.get(position);
                CartAdapter.cartViewHolder holder = (CartAdapter.cartViewHolder) cartListView.findViewHolderForAdapterPosition(position);
                TextView productNameAmount = holder.itemView.findViewById(R.id.itemCart_itemList_name);

                // Bitwise operators
                // https://stackoverflow.com/questions/276706/what-are-bitwise-operators
                   // productName.setPaintFlags(productName.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
//                if((productName.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
//                    productName.setPaintFlags( (productName.getPaintFlags()) & (~Paint.STRIKE_THRU_TEXT_FLAG));
//                } else {
//                    productName.setPaintFlags( productName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//                   }
                productNameAmount.setPaintFlags( productNameAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    adapter.completeItem(position,productNameAmount);

                Toast.makeText(getContext(),item.getProduct().getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                ItemCart item = itemCartList.get(position);
                Toast.makeText(getContext(),item.getProduct().getName(), Toast.LENGTH_LONG).show();
            }
        }));


        ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(cartListView);



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

        return view;
    }

    /**
     * Obtem todos os item contidos no {@code cart}
     * @param cart
     */
    private void loadItemsFromCart(Cart cart) {
        itemCartList = (ArrayList<ItemCart>) itemCartDAO.getAll(cart.getId());
        cart.setListItemCart(itemCartList);
        ArrayList<SimpleItem> simpleItemList = (ArrayList<SimpleItem>) simpleItemDAO.getAll(cart.getId());
        addSimpleProducts(simpleItemList);
    }

    private void reloadItemsFromCart(Cart cart){
          //itemCartList.clear();

          loadItemsFromCart(cart);
         // cartListView.getAdapter().notifyDataSetChanged();
         adapter = new CartAdapter(itemCartList,getActivity());
         cartListView.setAdapter(adapter);
    }

    private void loadRecipeItemCartDialog() {
        final View recipeItemCartView = getLayoutInflater().inflate(R.layout.dialog_itemcart_recipe_registration,null);

        final Spinner recipeSpinner = recipeItemCartView.findViewById(R.id.list_itemCart_recipe);
        ArrayList<Recipe> recipes = recipeDAO.getAll();
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
                reloadItemsFromCart(cart);

            }
        };

        
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        };

        basicViewHelper.createDialog(recipeItemCartView,confirmListener,cancelListener,R.string.cart_dialogTitle_addRecipe).show();



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

        final AlertDialog dialog = basicViewHelper.createDialog(itemCartView,confirmListener,cancelListener,R.string.cart_dialogTitle_addProduct);
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
        basicViewHelper.loadSpinner(productSpinner, products);

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
        final AlertDialog dialog = basicViewHelper.createDialog(simpleProductView,confirmListener,null, R.string.cart_dialogTitle_addSimpleProduct);
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
        try {
            Long id = itemCartDAO.add(newItemCart);
            newItemCart.setId(id);
            Toast.makeText(getContext(),R.string.itemCart_toast_productAdded,Toast.LENGTH_LONG).show();
            itemCartList.add(newItemCart);
            cartListView.getAdapter().notifyDataSetChanged();
        } catch (SQLiteConstraintException e){
             Toast.makeText(getContext(),R.string.itemCart_toast_productAlreadyRegistered,Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Add a ingredient of a Recipe to the cart.
     * Update the ingredient if it already was on the cart.
     * @param itemRecipe    Ingredient of a Recipe
     */
    private void addItemFromRecipe(ItemRecipe itemRecipe) {
        ItemCart itemCart = itemCartDAO.getByProductName(itemRecipe.getProduct().getName());
        if(itemCart == null){
            ItemCart convertedItemCart = new ItemCart(itemRecipe.getProduct(), itemRecipe.getAmount(), cart);
            addItem(convertedItemCart);
        } else {
            int updatedAmount = itemCart.getAmount() + itemRecipe.getAmount();
            Log.e("AddItemFromRecipe",  "aa" + updatedAmount);
            itemCart.setAmount(updatedAmount);
            itemCartDAO.update(itemCart);

            cartListView.getAdapter().notifyDataSetChanged();
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
                cartListView.getAdapter().notifyDataSetChanged();
                return true;
            } else {
                Toast.makeText(getActivity(),R.string.itemCart_validation_simpleItemAlreadyExists,Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }


    /**
     *  Search for a product with a specific name in the itemCartList
     * @param name
     * @return if the list contains a product with name = @param name
     */
    private boolean checkForItemWithName(String name){
        for (ItemCart itemCart:itemCartList) {
            if(itemCart.getProduct().getName().equals(name)){
                return true;
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


