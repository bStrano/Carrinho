package br.com.stralom.compras;


import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.adapters.RecipeSpinnerAdapter;
import br.com.stralom.adapters.StockAdapter;
import br.com.stralom.adapters.StockSpinnerAdapter;
import br.com.stralom.dao.ItemRecipeDAO;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.dao.StockDAO;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Recipe;
import br.com.stralom.entities.Stock;
import br.com.stralom.helper.BasicViewHelper;
import br.com.stralom.interfaces.EditMenuInterface;
import br.com.stralom.interfaces.StockUpdateCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockMain extends BasicViewHelper<ItemStock> implements StockUpdateCallback {
    private static final String TAG = "StockMain";
    private ItemStockDAO itemStockDAO;
    private RecipeDAO recipeDAO;
    private ProductDAO productDAO;
    private ItemRecipeDAO itemRecipeDAO;
    private Stock stock;
    private FloatingActionButton fabAddStock, fabConsumeRecipe, fabConsumeItem;
    private boolean fabPressed = false;
    private ArrayList<Recipe> recipes;


    @Override
    public boolean callChangeItemBackgroundColor(View view, int position) {
        ViewGroup background = view.findViewById(R.id.stock_itemList_foregroundView);
        return ((StockAdapter) listView.getAdapter()).changeItemBackgroundColor(background, position);

    }


    @Override
    public void initializeSuperAttributes() {
        list = (ObservableArrayList<ItemStock>) itemStockDAO.getAll(stock.getId());
        listView = mainView.findViewById(R.id.list_itemStock);
        managementMenu = mainView.findViewById(R.id.stock_management_list);
    }

    public StockMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_stock_main, container, false);

        fabConsumeRecipe = mainView.findViewById(R.id.stock_fab_consumeRecipe);
        fabConsumeItem = mainView.findViewById(R.id.stock_fab_consumeProduct);
        fab = mainView.findViewById(R.id.stock_fab_main);
        fabAddStock = mainView.findViewById(R.id.stock_fab_addStock);


        //DAOS
        recipeDAO = new RecipeDAO(getContext());
        itemStockDAO = new ItemStockDAO(getContext());
        productDAO = new ProductDAO(getContext());
        itemRecipeDAO = new ItemRecipeDAO(getActivity());
        StockDAO stockDAO = new StockDAO(getContext());

        stock = stockDAO.findById((long) 1);
        if (stock == null) {
            stock = new Stock((long) 1);
            stockDAO.add(stockDAO.getContentValues(stock));
        }
        initializeSuperAttributes();
        //VIEWS
        View view = inflater.inflate(R.layout.fragment_stock_main, container, false);
        list = (ObservableArrayList<ItemStock>) itemStockDAO.getAll(stock.getId());
        recipes = (ArrayList<Recipe>) recipeDAO.getAll();

        setUpEmptyListView(mainView, list, R.id.stock_emptyList, R.drawable.ic_stock, R.string.stock_emptyList_title, R.string.stock_emptyList_description);

        StockAdapter adapter = new StockAdapter(this, list, getActivity());
        listView.setAdapter(adapter);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        setUpManagementMenu(adapter);


        // Button newItemStock = view.findViewById(R.id.btn_newItemStock);



        fabAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StockRegistration.class);
                startActivity(intent);
            }
        });

        fabConsumeRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeSpinnerAdapter recipeSpinnerAdapter = new RecipeSpinnerAdapter(recipes, getActivity());
                createConsumeDialog(recipeSpinnerAdapter, R.string.itemstock_consume_recipeTitle);
            }
        });

        fabConsumeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockSpinnerAdapter stockSpinnerAdapter = new StockSpinnerAdapter(list, getActivity());
                createConsumeDialog(stockSpinnerAdapter, R.string.itemstock_consume_productTitle);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fabPressed) {
                    fabPressed = true;
                    changeFabsVisibility(View.VISIBLE);
                    fab.animate().rotation(-225).setDuration(600);


                    fabAddStock.animate().translationY(-155).setDuration(600);
                    fabConsumeRecipe.animate().translationY(-150).setDuration(600);
                    fabConsumeItem.animate().translationY(-150).setDuration(600);

                } else {
                    fab.animate().rotation(0).setDuration(1000);
                    fabAddStock.animate().translationY(20).setDuration(200).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            fabAddStock.setVisibility(View.INVISIBLE);
                        }
                    });
                    fabConsumeRecipe.animate().translationY(120).setDuration(400).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            fabConsumeRecipe.setVisibility(View.INVISIBLE);
                        }
                    });
                    fabConsumeItem.animate().translationY(240).setDuration(600).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            fabConsumeItem.setVisibility(View.INVISIBLE);
                            fab.setBackgroundResource(R.color.colorPrimary);
                        }
                    });


                    //
                    fabPressed = false;
                }
            }
        });


        return mainView;
    }

    private void changeFabsVisibility(int visibility) {
        fabAddStock.setVisibility(visibility);
        fabConsumeItem.setVisibility(visibility);
        fabConsumeRecipe.setVisibility(visibility);
    }

    public void createConsumeDialog(final BaseAdapter adapter, int titleRes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_itemstock_consume, null);
        final Spinner spinner = dialogView.findViewById(R.id.itemStock_spinner_consume);
        final EditText amount = dialogView.findViewById(R.id.itemStock_consume_amount);
        TextView title = dialogView.findViewById(R.id.itemStock_consume_title);
        title.setText(titleRes);
        spinner.setAdapter(adapter);


        builder.setView(dialogView)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (adapter instanceof RecipeSpinnerAdapter) {
                            Recipe recipe = (Recipe) spinner.getSelectedItem();
                            ArrayList<ItemRecipe> ingredients = (ArrayList<ItemRecipe>) itemRecipeDAO.getAllByRecipe(recipe.getId());
                            Double amountDouble = Double.valueOf(amount.getText().toString());
                            for (ItemRecipe itemrecipe : ingredients) {
                                for (ItemStock itemStock : list) {
                                    if (itemStock.getProduct().getName().equals(itemrecipe.getProduct().getName())) {
                                        double amountToBeAdded = itemrecipe.getAmount() * amountDouble;
                                        consumeItem(itemStock, amountToBeAdded);
                                        break;
                                    }
                                }
                            }
                        } else if (adapter instanceof StockSpinnerAdapter) {
                            consumeItem((ItemStock) spinner.getSelectedItem(), Double.valueOf(amount.getText().toString()));
                        }
                    }

                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }


    private void consumeItem(ItemStock itemStock, double amountToBeAdded) {
        double newAmount = itemStock.getActualAmount() - amountToBeAdded;
        if (newAmount < 0) {
            itemStock.setActualAmount(0);
        } else {
            itemStock.setActualAmount(newAmount);
        }

        itemStockDAO.update(itemStock);
        listView.getAdapter().notifyDataSetChanged();
    }

    public void createEditDialogForItem(final ItemStock itemStock) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_itemstock_updateamounts, null);
        final TextInputEditText actualAmount = dialogView.findViewById(R.id.dialog_itemstock_actualamount);
        final TextInputEditText maxAmount = dialogView.findViewById(R.id.dialog_itemstock_maxamount);

        TextView productName = dialogView.findViewById(R.id.dialog_itemstock_productname);


        productName.setText(itemStock.getProduct().getName());
        actualAmount.setText("");
        maxAmount.setText(String.valueOf(itemStock.getAmount()));

        builder.setView(dialogView)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String dialogActualAmount = actualAmount.getText().toString();
                        String dialogMaxAmount = maxAmount.getText().toString();
                        if (!dialogActualAmount.equals("")) {
                            itemStock.setActualAmount(Double.valueOf(dialogActualAmount));
                        }
                        if (!dialogMaxAmount.equals("")) {
                            itemStock.setAmount(Double.valueOf(dialogMaxAmount));
                        }


                        itemStockDAO.update(itemStock);
                        listView.getAdapter().notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }


    public void onResume() {
        //Temporary
        super.onResume();
        list.clear();
        list.addAll(itemStockDAO.getAll((long) 1));
        listView.getAdapter().notifyDataSetChanged();
        recipes = (ArrayList<Recipe>) recipeDAO.getAll();

    }


    @Override
    public void edit(ItemStock itemStock) {
        createEditDialogForItem(itemStock);
    }

    @Override
    public boolean isEditModeOn() {
        return editMode;
    }

    @Override
    public void showEditModeMenu(final EditMenuInterface editMenuInterface) {
        if (fabPressed) {
            fab.callOnClick();
        }
        super.showEditModeMenu(editMenuInterface);
    }
}
