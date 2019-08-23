package br.com.stralom.compras.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.adapters.RecipeSpinnerAdapter;
import br.com.stralom.compras.adapters.StockAdapter;
import br.com.stralom.compras.adapters.StockSpinnerAdapter;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.dao.RecipeDAO;
import br.com.stralom.compras.entities.ItemRecipe;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.entities.Recipe;
import br.com.stralom.compras.entities.Stock;
import br.com.stralom.compras.helper.BasicViewHelper;
import br.com.stralom.compras.helper.DialogHelper;
import br.com.stralom.compras.interfaces.EditMenuInterface;
import br.com.stralom.compras.interfaces.StockUpdateCallback;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockMain extends BasicViewHelper<Product> implements StockUpdateCallback {
    private static final String TAG = "StockMain";
    private RecipeDAO recipeDAO;
    private ProductDAO productDAO;

    private Stock stock;
    private FloatingActionButton fabAddStock, fabConsumeRecipe, fabConsumeItem;
    private boolean fabPressed = false;
    private ArrayList<Recipe> recipes;
    private ViewPager viewPager;
    private ArrayList<Product> products;
    private final int REGISTRATION_ITEMSTOCK = 555;


    @Override
    public boolean callChangeItemBackgroundColor(View view, int position) {
        ViewGroup background = view.findViewById(R.id.stock_itemList_foregroundView);
        return ((StockAdapter) listView.getAdapter()).changeItemBackgroundColor(background, position);

    }


    @Override
    public void initializeSuperAttributes() {
//        list = (ObservableArrayList<ItemStock>) itemStockDAO.getAll(stock.getId());
//        ArrayList<Product> products1 = getArguments().getParcelableArrayList("products");

        products = ((MainActivity) getActivity()).productList;
        for(Product product: products){
            if(product.getItemStock().getAmount() > 0) {
                this.list.add(product);
            }
        }
        recipes = getArguments().getParcelableArrayList("recipes");

//        products.addAll(products1);
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
        viewPager = getActivity().findViewById(R.id.mViewPager);

        //DAOS
        recipeDAO = new RecipeDAO(getContext());
        productDAO = new ProductDAO(getContext());


        initializeSuperAttributes();


        //VIEWS
        View view = inflater.inflate(R.layout.fragment_stock_main, container, false);


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

                if(list.size() > 0 ){
                    Intent intent = new Intent(getActivity(), StockRegistration.class);

                    ArrayList<Product> productsNotInStock = new ArrayList<>();
                    for(Product product: products){
                        if(product.getItemStock().getAmount() == 0){
                            productsNotInStock.add(product);
                        }
                    }
                    intent.putExtra("products", productsNotInStock);
                    startActivityForResult(intent, REGISTRATION_ITEMSTOCK);
                } else {
                    DialogHelper.createErrorDialog(getActivity(), R.string.error_title_procutsNotRegistered, R.string.error_message_productsNotRegistered, R.string.error_positiveButton_productsNotRegistered, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            viewPager.setCurrentItem(1);
                        }
                    });
                }

            }
        });

        fabConsumeRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeSpinnerAdapter recipeSpinnerAdapter = new RecipeSpinnerAdapter(recipes, getActivity());
                if(recipes.size() > 0){
                    createConsumeDialog(recipeSpinnerAdapter, R.string.itemstock_consume_recipeTitle);
                } else {
                    DialogHelper.createErrorDialog(getActivity(), R.string.error_title_recipesNotRegistered, R.string.error_message_recipesNotRegistered, R.string.error_positiveButton_recipesNotRegistered, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            viewPager.setCurrentItem(2);
                        }
                    });
                }

            }
        });

        fabConsumeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockSpinnerAdapter stockSpinnerAdapter = new StockSpinnerAdapter(list, getActivity());
                if(list.size() > 0){
                    createConsumeDialog(stockSpinnerAdapter, R.string.itemstock_consume_productTitle);
                } else {

                    DialogHelper.createErrorDialog(getActivity(), R.string.error_title_itemStockNotRegistered, R.string.error_message_itemStockNotRegistered, R.string.error_positiveButton_itemStockNotRegistered, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            fabAddStock.performClick();
                        }
                    });
                }

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
                            fabAddStock.show();
                        }
                    });
                    fabConsumeRecipe.animate().translationY(120).setDuration(400).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            fabConsumeRecipe.show();
                        }
                    });
                    fabConsumeItem.animate().translationY(240).setDuration(600).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            fabConsumeItem.show();
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
        if(visibility ==  View.VISIBLE) {
            fabAddStock.show();
            fabConsumeItem.show();
            fabConsumeRecipe.show();
        } else if(visibility == View.INVISIBLE){
            fabAddStock.hide();
            fabConsumeItem.hide();
            fabConsumeRecipe.hide();
        }
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
                    }

                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getText().toString().equals("")){
                    amount.setError(getText(R.string.validation_obrigatoryField));
                } else {
                    if (adapter instanceof RecipeSpinnerAdapter) {
                        Recipe recipe = (Recipe) spinner.getSelectedItem();
                       // ArrayList<ItemRecipe> ingredients = (ArrayList<ItemRecipe>) itemRecipeDAO.getAllByRecipe(recipe.getId());

                        // TODO: Refatoração em progresso , linha temporaria
                        ArrayList<ItemRecipe> ingredients = new ArrayList<>();

                        Double amountDouble = Double.valueOf(amount.getText().toString());
                        for (ItemRecipe itemrecipe : ingredients) {
                            for (Product product : list) {
                                if (product.getName().equals(itemrecipe.getProduct().getName())) {
                                    double amountToBeAdded = itemrecipe.getAmount() * amountDouble;
                                    consumeItem(product, amountToBeAdded);
                                    break;
                                }
                            }
                        }
                    } else if (adapter instanceof StockSpinnerAdapter) {
                        consumeItem((Product) spinner.getSelectedItem(), Double.valueOf(amount.getText().toString()));
                    }

                    dialog.dismiss();
                }
            }
        });
    }


    private void consumeItem(Product product, double amountToBeAdded) {
        double newAmount = product.getItemStock().getActualAmount() - amountToBeAdded;
        if (newAmount < 0) {
            product.getItemStock().setActualAmount(0);
        } else {
            product.getItemStock().setActualAmount(newAmount);
        }

        productDAO.update(product);
        listView.getAdapter().notifyDataSetChanged();
    }

    public void createEditDialogForItem(final Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_itemstock_updateamounts, null);
        final TextInputEditText actualAmount = dialogView.findViewById(R.id.dialog_itemstock_actualamount);
        final TextInputEditText maxAmount = dialogView.findViewById(R.id.dialog_itemstock_maxamount);

        TextView productName = dialogView.findViewById(R.id.dialog_itemstock_productname);


        productName.setText(product.getName());
        actualAmount.setText("");
        maxAmount.setText(String.valueOf(product.getItemStock().getAmount()));

        builder.setView(dialogView)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String dialogActualAmount = actualAmount.getText().toString();
                        String dialogMaxAmount = maxAmount.getText().toString();
                        if (!dialogActualAmount.equals("")) {
                            product.getItemStock().setActualAmount(Double.valueOf(dialogActualAmount));
                        }
                        if (!dialogMaxAmount.equals("")) {
                            product.getItemStock().setAmount(Double.valueOf(dialogMaxAmount));
                        }


                        productDAO.update(product);
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
        ArrayList<Product> products = ((MainActivity) getActivity()).productList;
        for(Product product: products){
            if(product.getItemStock().getAmount() > 0) {
                this.list.add(product);
            }
        }
        listView.getAdapter().notifyDataSetChanged();
        recipes = (ArrayList<Recipe>) recipeDAO.getAll();

    }


    @Override
    public void edit(Product product) {
        createEditDialogForItem(product);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if(fabPressed){
                fab.performClick();
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTRATION_ITEMSTOCK) {
            if(resultCode == RESULT_OK){
                Product product = data.getParcelableExtra("product");

                if (product != null) {
                    boolean updated = false;
                    int index = 0;
                    for (Product productItem : products) {

                        if (productItem.getName().toLowerCase().equals(product.getName().toLowerCase())) {
                            updated = true;
                            this.products.set(index, product);
                            this.listView.getAdapter().notifyDataSetChanged();
                            Toast.makeText(getContext(), "Produto inserido ao estoque !", Toast.LENGTH_LONG).show();

                            break;
                        }
                        index++;
                    }
                }
            }

        }
    }
}
