package br.com.stralom.compras.UI;


import android.app.Fragment;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


import br.com.stralom.compras.MainActivity;
import br.com.stralom.compras.R;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.stralom.compras.UI.ProductUITest.goToProductTab;
import static br.com.stralom.compras.UI.ProductUITest.registerProduct;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.productSpinnerWithText;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static br.com.stralom.compras.UI.matchers.RecipeMatcher.withRecipeHolder;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecipeUITest {
    private static final String productName = " ProductRecipe Test";

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init(){
        goToRecipeTab();
    }

    @Test
    public void ASetUp(){
        registerProduct(productName, "2");
        goToRecipeTab();
    }

    @Test
    public void registerRecipeTest(){
        String recipeName = "Recipe Test";
        registerRecipe(recipeName,productName,"2");
        onView(withId(R.id.recipe_view_main)).check(matches(isDisplayed()));
        onView(withId(R.id.list_recipe))
                .perform(RecyclerViewActions.scrollToHolder(withRecipeHolder("Recipe Test")))
                .check(matches(isDisplayed()));
    }

    public static void registerRecipe(String name, String productName, String productAmount){
        onView(withId(R.id.btn_newRecipe)).perform(click());
        onView(withId(R.id.form_recipe_name)).perform(replaceText(name));

        onView(withId(R.id.form_recipe_btn_newIngredient)).perform(click());
        onView(withId(R.id.form_itemRecipe_amount)).perform(replaceText(productAmount));

        onView(withId(R.id.form_itemRecipe_products)).perform(click());

        onData(productSpinnerWithText(productName)).inRoot(isPlatformPopup()).perform(click());
        onView(withText("Salvar")).perform(click());
        onView(withId(R.id.recipeRegistration_save)).perform(click());
    }

    public static void goToRecipeTab(){
        onView(withText("Receitas")).perform(click());
    }
}
