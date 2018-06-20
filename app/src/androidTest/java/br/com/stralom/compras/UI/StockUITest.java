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

import br.com.stralom.compras.MainActivity;
import br.com.stralom.compras.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.stralom.compras.UI.ProductUITest.registerProduct;
import static br.com.stralom.compras.UI.RecipeUITest.registerRecipe;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.productSpinnerWithText;
import static br.com.stralom.compras.UI.matchers.StockMatcher.withStockHolder;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StockUITest {
    private static final String PRODUCT_NAME = "Stock Test";
    private Activity activity;


    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        activity = activityActivityTestRule.getActivity();
        goToStockTab();
    }

    @Test
    public void ASetup(){
        registerProduct(PRODUCT_NAME,"2");
        registerRecipe("StockRecipeTest",PRODUCT_NAME,"2");
    }



    @Test
    public void test()  {
        onView(withId(R.id.fab_stock)).perform(click());

        onView(withId(R.id.fab_addStock)).perform(click());

    }

    @Test
    public void TestAddingProduct()  {
        String actualAmount = "20";
        String maxAmount = "40";

        onView(withId(R.id.fab_stock)).perform(click());

        onView(withId(R.id.fab_addStock)).perform(click());



        onView(withId(R.id.form_itemStock_actualAmount)).perform(replaceText(actualAmount));
        onView(withId(R.id.form_itemStock_maxAmount)).perform(replaceText(maxAmount));
        onView(withId(R.id.form_itemStock_products)).perform(click());
        onData(productSpinnerWithText(PRODUCT_NAME)).inRoot(isPlatformPopup()).perform(click());

        onView(withText(R.string.save)).perform(click());

        onView(withId(R.id.list_itemStock)).perform(RecyclerViewActions.scrollToHolder(withStockHolder(PRODUCT_NAME,actualAmount,maxAmount)))
                .check(matches(isDisplayed()));
    }



    public void goToStockTab(){
        String tab = activity.getResources().getStringArray(R.array.tab_titles)[3];
        onView(withText(tab)).perform(click());
    }
}
