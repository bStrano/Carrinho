package br.com.stralom.compras.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.ItemRecipe;
import br.com.stralom.compras.entities.Recipe;

import static android.content.ContentValues.TAG;

/**
 * Created by Bruno Strano on 17/01/2018.
 */

public class RecipeForm extends BasicFormValidation {
    @NotEmpty(messageResId = R.string.validation_obrigatoryField)
    private final EditText name;
    //private final double total;
    private final ImageView image;
    private final ArrayList<ItemRecipe> ingredients;
    private Recipe recipe;
    Button addIngredient;

    private final Activity activity;
    private boolean validationSuccessful;

    public RecipeForm(Activity activity, ArrayList<ItemRecipe> ingredients) {
        super(activity);
        this.activity = activity;
        this.name = activity.findViewById(R.id.registration_recipe_name);
        this.image = activity.findViewById(R.id.registration_recipe_image);
        this.addIngredient = activity.findViewById(R.id.registration_recipe_btn_addIngredient);
        this.ingredients = ingredients;
        this.validationSuccessful = false;
        recipe = null;
        //  total = 0;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void loadImage(String mCurrentPhotoPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        image.setImageBitmap(bitmap);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setTag(mCurrentPhotoPath);
    }


    public boolean validateIngredients() {
        Log.e(TAG,String.valueOf(ingredients.size()));
        if (ingredients.size() == 0 ){
            String defaultMessage = activity.getResources().getString(R.string.registration_recipe_selectIngredient);
            String errorMessage = activity.getResources().getString(R.string.recipe_validation_ingredients);
            errorMessage = String.format(errorMessage, defaultMessage );
            addIngredient.setText(errorMessage);
            addIngredient.setTextColor(Color.RED);
            Log.e(TAG, addIngredient.getText().toString());
            return false;
        } else {
            return true;
        }

    }

    public void restoreButtonValidState(){
        String defaultMessage = activity.getResources().getString(R.string.registration_recipe_selectIngredient);
        addIngredient.setText(defaultMessage);
        addIngredient.setTextColor(Color.WHITE);
    }

    @Override
    public void onValidationSucceeded() {
        if(validateIngredients()){
            recipe = new Recipe(name.getText().toString(),ingredients,(String) image.getTag());
            validationSuccessful = true;
        } else {
            validationSuccessful = false;
        }
    }

    @Override
    public boolean isValidationSuccessful() {
        return validationSuccessful;
    }
}
