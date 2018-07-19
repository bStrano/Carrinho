package br.com.stralom.compras.UI;


import android.app.Activity;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import br.com.stralom.compras.MainActivity;
import br.com.stralom.compras.R;
import br.com.stralom.dao.CategoryDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.entities.Category;
import br.com.stralom.entities.Product;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.productSpinnerWithText;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.withError;
import static br.com.stralom.compras.UI.matchers.RecipeMatcher.withRecipeHolder;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecipeUITest {
    private Activity activity;
    private static boolean initialized = false;
    private static Product product;
    private static Category category;
    private static ProductDAO productDAO;
    private static CategoryDAO categoryDAO;

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp(){
        goToRecipeTab();
        activity = activityActivityTestRule.getActivity();
        if(!initialized){
            initialized = true;
            categoryDAO = new CategoryDAO(activity);
            productDAO = new ProductDAO(activity);

            category = categoryDAO.add("Recipe Category","Recipe Category",R.drawable.meat);
            product = productDAO.add(" ProductRecipe Test",2,category);
        }
    }


    @Test
    public void TestAddingRecipe(){
        String recipeName = "Recipe Test";
        String productAmount = "2";
        String ingredientsCount = activity.getString(R.string.recipe_itemList_ingredientCount);
        ingredientsCount = String.format(ingredientsCount,1);
        String price = activity.getString(R.string.recipe_itemList_price);
        double total = Double.valueOf(productAmount) * product.getPrice();

        price = String.format(price,total);

        registerRecipe(recipeName, product.getName(),productAmount);
//        onView(withId(R.id.recipe_view_main)).check(matches(isDisplayed()));
        onView(withId(R.id.list_recipe))
                .perform(RecyclerViewActions.scrollToHolder(withRecipeHolder(recipeName,ingredientsCount,price)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void TestAddingRecipeAlreadyRegistered(){
        String recipeName = "Recipe Test 2";
        registerRecipe(recipeName, product.getName(),"2");
        registerRecipe(recipeName, product.getName(),"2");
        onView(withText(R.string.toast_recipe_alreadyRegistered)).inRoot(withDecorView(not(Matchers.is(activity.getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
    }

    @Test
    public void TestAddingRecipeWithoutName(){
        registerRecipe("", product.getName(),"2");
        String errorMsg = activity.getString(R.string.validation_obrigatoryField);
        onView(withId(R.id.form_recipe_name))
                .check(matches(withError(errorMsg)));
    }

    @Test
    public void TestAddingRecipeWithoutIngredients(){
        String defaultMessage = activity.getResources().getString(R.string.addIngredient);
        String errorMessage = activity.getResources().getString(R.string.recipe_validation_ingredients);
        errorMessage = String.format(errorMessage, defaultMessage );
        onView(withId(R.id.btn_newRecipe)).perform(click());
        onView(withId(R.id.form_recipe_name)).perform(replaceText("No Ingredients"));

        onView(withId(R.id.registration_save)).perform(click());
        onView(withId(R.id.form_recipe_txtAddIngredient)).check(matches(withText(errorMessage)));
    }

    private static void addIngredient(String ingredientName){
        onView(withId(R.id.form_recipe_btn_newIngredient)).perform(click());
        onView(withId(R.id.form_itemRecipe_amount)).perform(replaceText("2"));
        onView(withId(R.id.form_itemRecipe_products)).perform(click());
        onData(productSpinnerWithText(ingredientName)).inRoot(isPlatformPopup()).perform(click());
        onView(withText(R.string.save)).perform(click());

    }

    public static void registerRecipe(String name, String productName, String productAmount){
        //goToRecipeTab();
        onView(withId(R.id.btn_newRecipe)).perform(click());
        onView(withId(R.id.form_recipe_name)).perform(replaceText(name));

        addIngredient(productName);
        onView(withId(R.id.registration_save)).perform(click());
    }

    public static void goToRecipeTab(){
        onView(withText("Receitas")).perform(click());
    }
}
