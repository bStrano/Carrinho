package br.com.stralom.compras;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import br.com.stralom.dao.ItemRecipeDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;
import br.com.stralom.helper.ItemRecipeForm;
import br.com.stralom.helper.RecipeForm;

public class RecipeRegistration extends AppCompatActivity {
    private static final String TAG = "RecipeRegistration";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private ItemRecipeDAO itemRecipeDAO;
    private RecipeDAO recipeDAO;
    private RecipeForm recipeForm;
    private ArrayList<ItemRecipe> ingredients;
    private ArrayAdapter<ItemRecipe> itemRecipeArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe_registration);


        Button btn_newImage = findViewById(R.id.form_recipe_btn_newImage);
        Button btn_newIngredient = findViewById(R.id.form_recipe_btn_newIngredient);
        ListView list_ingredients = findViewById(R.id.form_recipe_ingredients);

        recipeDAO = new RecipeDAO(this);
        itemRecipeDAO = new ItemRecipeDAO(this);
        ingredients = new ArrayList<>();

        recipeForm = new RecipeForm(this, ingredients);

        // Toolbar
        Toolbar mToolbar = findViewById(R.id.registration_toolbar);

        mToolbar.setTitle(R.string.title_RecipeRegistration);
        Objects.requireNonNull(this).setSupportActionBar(mToolbar);
        //setHasOptionsMenu(true);

        // Toolbar - Back
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        itemRecipeArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(this), android.R.layout.simple_list_item_1, ingredients);
        list_ingredients.setAdapter(itemRecipeArrayAdapter);

    }

    private AlertDialog loadAlertDialogNewItemRecipe() {
        View newItemRecipeLayout = getLayoutInflater().inflate(R.layout.dialog_itemrecipe_registration, null);
        loadProductsSpinner(newItemRecipeLayout);

        final ItemRecipeForm itemRecipeForm = new ItemRecipeForm(newItemRecipeLayout);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.form_title_ItemRecipeRegistration);

        builder.setView(newItemRecipeLayout);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ingredients.add(itemRecipeForm.getItemRecipe());
                Toast.makeText(getBaseContext(), "Ingrediente adicionado!", Toast.LENGTH_LONG).show();

                itemRecipeArrayAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    private void loadProductsSpinner(View newItemRecipeLayout) {
        Spinner list_products = newItemRecipeLayout.findViewById(R.id.form_itemRecipe_products);
        ProductDAO productDAO = new ProductDAO(this);
        ArrayList<Product> products = (ArrayList<Product>) productDAO.getAll();
        ArrayAdapter<Product> productArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(this), android.R.layout.simple_spinner_item, products);
        productArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_products.setAdapter(productArrayAdapter);
    }


//    private void loadIngredientList(ListView list_ingredients, ItemRecipeDAO itemRecipeDAO, long recipeId){
//        ArrayList<ItemRecipe> ingredients = itemRecipeDAO.getAll();
//        ArrayAdapter adapter = new ArrayAdapter<ItemRecipe>(getContext(),android.R.layout.simple_list_item_1, ingredients);
//        list_ingredients.setAdapter(adapter);
//    }

    private void capturePhotoIntent() {
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentCamera.resolveActivity(Objects.requireNonNull(this).getPackageManager()) != null) {
            File photo = null;
            try {
                photo = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photo != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "br.com.stralom.compras", photo);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intentCamera, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(this).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    recipeForm.loadImage(mCurrentPhotoPath);
                }
                break;


        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secundary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.registration_save:
                recipeForm.getValidator().validate();
                Recipe recipe = recipeForm.getRecipe();
                if (recipeForm.isValidationSuccessful()) {
                    if(registerRecipe(recipe)){
                        Intent data = new Intent();
                        data.putExtra("recipe",recipe);
                        setResult(RESULT_OK,data);
                        finish();


                    }
                }


        }

        return super.onOptionsItemSelected(item);
    }

    private boolean registerRecipe(Recipe recipe) {
        if (recipeDAO.findByName(recipe.getName()) == null) {
            Long idRecipe = recipeDAO.add(recipeDAO.getContentValues(recipe));
            recipe.setId(idRecipe);
            for (ItemRecipe ingredient : ingredients) {
                ingredient.setRecipe(recipe);
                itemRecipeDAO.add(itemRecipeDAO.getContentValues(ingredient));
            }
            return true;
        } else {
            Toast.makeText(this, R.string.toast_recipe_alreadyRegistered, Toast.LENGTH_LONG).show();
            return false;
        }
    }
}

