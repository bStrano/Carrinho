package br.com.stralom.compras.UI;

import android.app.Activity;
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

import java.util.ArrayList;

import br.com.stralom.compras.MainActivity;
import br.com.stralom.compras.R;
import br.com.stralom.dao.CategoryDAO;
import br.com.stralom.dao.ItemRecipeDAO;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.entities.Category;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.isToast;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.productSpinnerWithText;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.waitFor;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.withError;
import static br.com.stralom.compras.UI.matchers.StockMatcher.withStockHolder;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StockUITest {
    private static final String  CATEGORY_NAME = "Stock Category";
    private static final String PRODUCT_NAME = "Stock Product";
    private static final String RECIPE_NAME = "Stock Recipe";
    private static boolean initialized = false;
    private static ProductDAO productDAO;
    private static CategoryDAO categoryDAO;
    private static RecipeDAO recipeDAO;
    private static ItemRecipeDAO itemRecipeDAO;
    private static Product product;
    private static Category category;
    private static Recipe recipe;
    private  Activity activity;


    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        activity = activityActivityTestRule.getActivity();
        if(!initialized){
            productDAO = new ProductDAO(activity);
            categoryDAO = new CategoryDAO(activity);
            recipeDAO = new RecipeDAO(activity);
            itemRecipeDAO = new ItemRecipeDAO(activity);
            category = categoryDAO.add(CATEGORY_NAME,CATEGORY_NAME,R.drawable.meat);
            product = productDAO.add(PRODUCT_NAME,120,category);
            ItemRecipe itemRecipe = new ItemRecipe(2,product);
            ArrayList<ItemRecipe> products = new ArrayList<>();
            products.add(itemRecipe);
            recipe = recipeDAO.add(RECIPE_NAME,products, null);
            itemRecipeDAO.add(2,product,recipe);
            initialized = true;
        }
        goToStockTab();
    }



    @Test
    public void TestAddingItem()  {
        String actualAmount = "20";
        String maxAmount = "40";



        openDialogAndRegisterItem(PRODUCT_NAME, actualAmount,maxAmount);

        onView(withId(R.id.list_itemStock)).perform(RecyclerViewActions.scrollToHolder(withStockHolder(PRODUCT_NAME,actualAmount,maxAmount)))
                .check(matches(isDisplayed()));
        onView(withText(R.string.toast_itemStock_validRegistration)).inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void TestAddingWithEmptyActualAmount(){

        openDialogAndRegisterItem(PRODUCT_NAME,null,"41");

        String validationMsg = activity.getResources().getString(R.string.validation_obrigatoryField);
        onView(withId(R.id.form_itemStock_actualAmount)).check(matches(withError(validationMsg)));
    }

    @Test
    public void TestWithMaxEmptyAmount(){

        openDialogAndRegisterItem(PRODUCT_NAME,"20",null);

        String validationMsg = activity.getResources().getString(R.string.validation_obrigatoryField);
        onView(withId(R.id.form_itemStock_maxAmount)).check(matches(withError(validationMsg)));

    }

    @Test
    public void TestAddingItemAlreadyRegistered(){
        String productName = "Already Registered";
        productDAO = new ProductDAO(activity);
        productDAO.add(productName,2,category);

        openDialogAndRegisterItem(productName,"20","40");
        openDialogAndRegisterItem(productName,"20","40");

        onView(withText(R.string.toast_itemStock_invalidRegistration)).inRoot(isToast())
                .check(matches(isDisplayed()));


    }


    private void openDialogAndRegisterItem(String productName, String actualAmount, String maxAmount){
        accessStockProductDialog();
        registerProductItemStock(productName,actualAmount,maxAmount);
    }

    private void registerProductItemStock(String productName, String actualAmount, String maxAmount){
        if(actualAmount != null){
            onView(withId(R.id.form_itemStock_actualAmount)).perform(replaceText(actualAmount));
        }
        if(maxAmount != null){
            onView(withId(R.id.form_itemStock_maxAmount)).perform(replaceText(maxAmount));
        }
        onView(withId(R.id.form_itemStock_products)).perform(click());
        onData(productSpinnerWithText(productName)).inRoot(isPlatformPopup()).perform(click());

        onView(withText(R.string.save)).perform(click());
    }

    private void accessStockProductDialog() {
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.fab_stock)).perform(click());
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.fab_addStock)).perform(click());
    }


    private void goToStockTab(){
        String tab = activity.getResources().getStringArray(R.array.tab_titles)[3];
        onView(withText(tab)).perform(click());
    }
}
