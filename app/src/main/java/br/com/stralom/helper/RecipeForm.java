package br.com.stralom.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Recipe;

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

    private final Activity activity;
    private boolean validationSuccessful;

    public RecipeForm(Activity activity, ArrayList<ItemRecipe> ingredients) {
        super(activity);
        this.activity = activity;
        this.name = activity.findViewById(R.id.form_recipe_name);
        this.image = activity.findViewById(R.id.form_recipe_image);
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
            String defaultMessage = activity.getResources().getString(R.string.addIngredient);
            String errorMessage = activity.getResources().getString(R.string.recipe_validation_ingredients);
            errorMessage = String.format(errorMessage, defaultMessage );
            TextView textView = activity.findViewById(R.id.form_recipe_txtAddIngredient);
            textView.setText(errorMessage);
            textView.setTextColor(Color.RED);
            Log.e(TAG, textView.getText().toString());
            return false;
        } else {
            return true;
        }

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
