package br.com.stralom.compras;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.stralom.adapters.CartAdapter;
import br.com.stralom.adapters.ItemClickListener;
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
        cartListView = view.findViewById(R.id.list_cart);
        Button btn_addRecipe = view.findViewById(R.id.btn_newItemCart_recipe);
        Button btn_addSimpleItem = view.findViewById(R.id.btn_newItemCart_simpleProduct);
        Button btn_newItemCart = view.findViewById(R.id.btn_newItemCart);

        cart = cartDAO.findById((long) 1);
        itemCartList = (ArrayList<ItemCart>) itemCartDAO.getAll(cart.getId());
        ArrayList<SimpleItem> simpleItemList = (ArrayList<SimpleItem>) simpleItemDAO.getAll(cart.getId());
        addSimpleProducts(simpleItemList);
        cart.setListItemCart(itemCartList);
        cartListView.setHasFixedSize(true);
        cartListView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(itemCartList, Objects.requireNonNull(getActivity()));
        cartListView.setAdapter(adapter);


        cartListView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),cartListView, new ItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                ItemCart item = itemCartList.get(position);
                CartAdapter.mViewHolder holder = (CartAdapter.mViewHolder) cartListView.findViewHolderForAdapterPosition(position);
                TextView productNameAmount = holder.itemView.findViewById(R.id.itemCart_itemList_nameAmount);

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





    private void loadRecipeItemCartDialog() {
        final View recipeItemCartView = getLayoutInflater().inflate(R.layout.dialog_itemcart_recipe_registration,null);

        final Spinner recipeSpinner = recipeItemCartView.findViewById(R.id.list_itemCart_recipe);
        List<Recipe> recipes = recipeDAO.getAll();
        basicViewHelper.loadSpinner(recipeSpinner,recipes);

        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Recipe recipe = (Recipe) recipeSpinner.getSelectedItem();
                List<ItemRecipe> itemRecipes = itemRecipeDAO.getAllByRecipe(recipe.getId());
                for (ItemRecipe itemRecipe :itemRecipes){
                    addItemFromRecipe(itemRecipe);
                }
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
                addItemFromProduct(itemCartView);
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"Cancelado.",Toast.LENGTH_LONG).show();
            }
        };

        basicViewHelper.createDialog(itemCartView,confirmListener,cancelListener,R.string.cart_dialogTitle_addProduct).show();

        Spinner productSpinner = itemCartView.findViewById(R.id.form_itemCart_product);
        List<Product> products = productDAO.getAll();
        basicViewHelper.loadSpinner(productSpinner, products);

    }
    private void loadSimpleProductItemCartDialog(){
        final View view = getLayoutInflater().inflate(R.layout.dialog_itemcart_simpleproduct_registration,null);

        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addItemFromSimpleProduct(view);
            }
        };



        basicViewHelper.createDialog(view,confirmListener,null, R.string.cart_dialogTitle_addSimpleProduct).show();
    }

    private void addItem(ItemCart newItemCart){
        try {
            Long id = itemCartDAO.add(newItemCart);
            newItemCart.setId(id);
            Toast.makeText(getContext(),R.string.toast_product_add,Toast.LENGTH_LONG).show();
            itemCartList.add(newItemCart);
            cartListView.getAdapter().notifyDataSetChanged();
        } catch (SQLiteConstraintException e){
            Toast.makeText(getContext(),R.string.toast_product_alreadyUpdate,Toast.LENGTH_LONG).show();
        }
    }


    private void addItemFromRecipe(ItemRecipe itemRecipe) {
        ItemCart itemCart = new ItemCart(itemRecipe.getProduct(), itemRecipe.getAmount(), cart);
        addItem(itemCart);
    }
    private void addItemFromProduct(View itemCartView) {
        ItemCartForm itemCartForm = new ItemCartForm(itemCartView, cart);
        ItemCart itemCart = itemCartForm.getItemCart();
        addItem(itemCart);
    }
    private void addItemFromSimpleProduct(View view){
        SimpleItemForm simpleItemForm = new SimpleItemForm(view,cart);
        SimpleItem simpleItem = simpleItemForm.getSimpleItem();
        Long id = simpleItemDAO.add(simpleItemDAO.getContentValues(simpleItem));

        ItemCart simpleItemConverted = simpleItem.convertToItemCart();
        simpleItemConverted.setConvertedId(id);
        cart.getListItemCart().add(simpleItemConverted);
        cartListView.getAdapter().notifyDataSetChanged();

    }

    private void addSimpleProducts(ArrayList<SimpleItem> simpleItemList){
        if(simpleItemList.size() > 0) {
            for (SimpleItem simpleItem:simpleItemList) {
                cart.getListItemCart().add(simpleItem.convertToItemCart());
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


