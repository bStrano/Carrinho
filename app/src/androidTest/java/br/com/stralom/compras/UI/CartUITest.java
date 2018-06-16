package br.com.stralom.compras.UI;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
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
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static br.com.stralom.compras.UI.ProductUITest.goToProductTab;
import static br.com.stralom.compras.UI.ProductUITest.registerProduct;
import static br.com.stralom.compras.UI.RecipeUITest.goToRecipeTab;
import static br.com.stralom.compras.UI.RecipeUITest.registerRecipe;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.atPosition;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.productSpinnerWithText;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.withCartHolder;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.withError;
import static br.com.stralom.compras.UI.matchers.RecipeMatcher.recipeSpinnerWithText;


@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartUITest {
    private Activity activity;
    private ViewInteraction registerSimpleProductBtn;
    private ViewInteraction cartViewMain;
    private final String defaultProductName = "Product for Test";
    private final String defaultRecipeName = "Product Recipe Test";


//   @BeforeClass
//   public static void setUp(){ goToProductTab();
//      registerProduct("Product for Test","10");
//      goToCartTab();
//   }

    @Before
    public void init(){
        activity = activityActivityTestRule.getActivity();
        registerSimpleProductBtn = onView(withId(R.id.itemcart_btn_registerSimpleProduct));
        cartViewMain = onView(withId(R.id.cart_view_main));
    }

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ATestSetUp(){
        setUp(defaultRecipeName,defaultProductName,"10","2");
    }



    @Test
    public void TestOpenSimpleItemRegisterDialog(){
        onView(withId(R.id.itemcart_btn_registerSimpleProduct)).perform(click());
        onView(withId(R.id.itemcart_dialog_simpleProduct)).check(matches(isDisplayed()));
    }

    @Test
    public void TestAddingSimpleItemWithAmount(){
        String amount = "10";
        String name = "Simple" + defaultProductName;
        registerSimpleProduct(name,amount);
        cartViewMain
                .check(matches(isDisplayed()));

       // onView(withId(R.id.cart_list_itemCarts)).perform(RecyclerViewActions.scrollToHolder(withCartHolder(name)))
         //       .check(matches(isDisplayed()));

    }

    @Test
    public void TestAddingSimpleItemWithEmptyName(){
        registerSimpleProduct("","10");

        String errorMsg = activity.getString(R.string.validation_obrigatoryField);
        onView(withId(R.id.itemCart_form_simpleProduct_name)).check(matches(withError(errorMsg)));

    }

    @Test
    public void TestAddingRecipe(){
        String name = "TestAddingRecipe";
        setUp(name, name, "10","2");
        registerItemCartRecipe(name);

        onView(withId(R.id.cart_list_itemCarts))
                .perform(RecyclerViewActions.scrollToHolder(withCartHolder(name,null)))
                .check(matches(isDisplayed()));

    }

    @Test
    public void TestAddingRecipeAlreadyRegistered(){
        String name = "TestAddingRecipe";
        registerItemCartRecipe(name);

        onView(withId(R.id.cart_list_itemCarts))
                .perform(RecyclerViewActions.scrollToHolder(withCartHolder(name,"4")))
                .check(matches(isDisplayed()));
    }




    @Test
   public void TestAddingProduct(){


        registerItemCartProduct(defaultProductName,"10");

        cartViewMain
                .check(matches(isDisplayed()));

        onView(withId(R.id.cart_list_itemCarts)).perform(RecyclerViewActions.scrollToHolder(withCartHolder(defaultProductName)))
            .check(matches(isDisplayed()));
   }



    @Test
   public void TestAddingProductWithEmptyAmount(){

        registerItemCartProduct(defaultProductName,"");
        String errorMsg= activity.getString(R.string.itemCart_validation_amount);
        onView(withId(R.id.itemCart_form_productAmount)).check(matches(withError(errorMsg)));
   }



    private void registerItemCartRecipe(String name) {
        onView(withId(R.id.itemCart_btn_registerRecipe)).perform(click());

        onView(withId(R.id.list_itemCart_recipe)).perform(click());
        onData(recipeSpinnerWithText(name)).inRoot(isPlatformPopup()).perform(click());

        onView(withText(R.string.save)).perform(click());

    }

    private void registerItemCartProduct(String productName, String productAmount) {
        onView(withId(R.id.itemCart_btn_registerProduct)).perform(click());
        onView(withId(R.id.itemcart_form_product)).perform(click());
        onData(productSpinnerWithText(productName)).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.itemCart_form_productAmount)).perform(replaceText(productAmount));
        onView(withText(R.string.save)).perform(click());
    }

    private void registerSimpleProduct(String name, String amount) {
        registerSimpleProductBtn.perform(click());
        onView(withId(R.id.itemCart_form_simpleProduct_name)).perform(replaceText(name));
        onView(withId(R.id.itemCart_form_simpleProduct_amount)).perform(replaceText(amount));
        onView(withText(R.string.save)).perform(click());
    }

    /**
     * Register a Product and back to Cart Tab.
     */

    private void setUp(String recipeName, String productName, String productPrice, String productAmount){
        goToProductTab();
        registerProduct(productName,productPrice);
        goToRecipeTab();
        registerRecipe(recipeName, productName,productAmount);
        goToCartTab();
    }

    private static void goToCartTab(){
        onView(withText("Carrinho")).perform(click());
    }
}
