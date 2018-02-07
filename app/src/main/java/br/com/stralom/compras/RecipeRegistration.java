package br.com.stralom.compras;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.stralom.compras.R;
import br.com.stralom.dao.DBHelper;
import br.com.stralom.dao.ItemRecipeDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;
import br.com.stralom.helper.ItemRecipeForm;
import br.com.stralom.helper.RecipeForm;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeRegistration extends Fragment {
    private static final String TAG = "RecipeRegistration";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private ItemRecipeDAO itemRecipeDAO;
    private RecipeDAO recipeDAO;
    private RecipeForm recipeForm;
    private ArrayList<ItemRecipe> ingredients;
    private ArrayAdapter<ItemRecipe> itemRecipeArrayAdapter;


    public RecipeRegistration() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_registration, container, false);


        Button btn_newImage = view.findViewById(R.id.form_recipe_btn_newImage);
        Button btn_newIngredient = view.findViewById(R.id.form_recipe_btn_newIngredient);
        ListView list_ingredients = view.findViewById(R.id.form_recipe_ingredients);

        recipeDAO = new RecipeDAO(getContext());
        itemRecipeDAO = new ItemRecipeDAO(getActivity());
         ingredients = new ArrayList<>();

        recipeForm = new RecipeForm(view,ingredients);

        // Toolbar
        Toolbar mToolbar = view.findViewById(R.id.toolbar3);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitle(R.string.title_RecipeRegistration);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);

        // Toolbar - Back
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                RecipeRegistration fragment = new RecipeRegistration();
                intent.putExtra(RecipeRegistration.class.getSimpleName(),RecipeRegistration.class.getSimpleName());
                startActivity(intent);
            }
        });

        // ToolBar - Save


        // Add Image
        btn_newImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhotoIntent();
            }
        });
        // Add Ingredient
        btn_newIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAlertDialogNewItemRecipe().show();
            }
        });
        // Update Ingredient List
        itemRecipeArrayAdapter = new ArrayAdapter<ItemRecipe>(getContext(),android.R.layout.simple_list_item_1,ingredients);
        list_ingredients.setAdapter(itemRecipeArrayAdapter);

        // Save Recipe


        return view;
    }

    private AlertDialog loadAlertDialogNewItemRecipe() {
        View newItemRecipeLayout = getLayoutInflater().inflate(R.layout.dialog_itemrecipe_registration,null);
        loadProductsSpinner(newItemRecipeLayout);

        final ItemRecipeForm itemRecipeForm = new ItemRecipeForm(newItemRecipeLayout);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.form_title_ItemRecipeRegistration);

        builder.setView(newItemRecipeLayout);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ingredients.add(itemRecipeForm.getItemRecipe());
                Toast.makeText(getActivity(),"Ingrediente adicionado!",Toast.LENGTH_LONG).show();

                itemRecipeArrayAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.cancel,null);
        return builder.create();
    }

    private void loadProductsSpinner(View newItemRecipeLayout) {
        Spinner list_products = newItemRecipeLayout.findViewById(R.id.form_itemRecipe_products);
        ProductDAO productDAO = new ProductDAO(getContext());
        ArrayList<Product> products = (ArrayList<Product>) productDAO.getAll();
        ArrayAdapter<Product> productArrayAdapter = new ArrayAdapter<Product>(getContext(),android.R.layout.simple_spinner_item,products);
        productArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_products.setAdapter(productArrayAdapter);
    }


//    private void loadIngredientList(ListView list_ingredients, ItemRecipeDAO itemRecipeDAO, long recipeId){
//        ArrayList<ItemRecipe> ingredients = itemRecipeDAO.getAll();
//        ArrayAdapter adapter = new ArrayAdapter<ItemRecipe>(getContext(),android.R.layout.simple_list_item_1, ingredients);
//        list_ingredients.setAdapter(adapter);
//    }

    private void capturePhotoIntent(){
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentCamera.resolveActivity(getActivity().getPackageManager()) != null){
            File photo = null;
            try{
                photo = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(photo != null){
                Uri photoURI = FileProvider.getUriForFile(getActivity(),"br.com.stralom.compras",photo);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intentCamera,REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == Activity.RESULT_OK) {
                    recipeForm.loadImage(mCurrentPhotoPath);
                }
                break;


        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.secundary_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.productRegistration_save:
                Recipe recipe = recipeForm.getRecipe();
                Long idRecipe = recipeDAO.add(recipeDAO.getContentValues(recipe));
                recipe.setId(idRecipe);
                for (ItemRecipe ingredient: ingredients) {
                    ingredient.setRecipe(recipe);
                    // *********************************
                    itemRecipeDAO.add(itemRecipeDAO.getContentValues(ingredient));
                }
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(RecipeRegistration.class.getSimpleName(),RecipeRegistration.class.getSimpleName());
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
