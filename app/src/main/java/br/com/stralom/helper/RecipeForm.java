package br.com.stralom.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Recipe;

/**
 * Created by Bruno Strano on 17/01/2018.
 */

public class RecipeForm {
    private static final String TAG = "RecipeForm";
    private EditText name;
    private  ListView ingredientsTest;
    private double total;
    private ImageView image;
    private ArrayList<ItemRecipe> ingredients;

    public RecipeForm(View view, ArrayList<ItemRecipe> ingredients) {
        this.name = view.findViewById(R.id.form_recipe_name);
        this.ingredientsTest = view.findViewById(R.id.form_recipe_ingredients);
        this.image = view.findViewById(R.id.form_recipe_image);
        this.ingredients = ingredients;
        total = 0;
    }

    public Recipe getRecipe(){
        String name = this.name.getText().toString();
        String imagePath = (String) image.getTag();
        return new Recipe(name,ingredients,imagePath);
    }

    public void loadImage(String mCurrentPhotoPath){
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        image.setImageBitmap(bitmap);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setTag(mCurrentPhotoPath);
    }
}
