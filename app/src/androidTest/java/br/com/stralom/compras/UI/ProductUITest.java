package br.com.stralom.compras.UI;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import br.com.stralom.compras.activities.MainActivity;
import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.CategoryDAO;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.entities.Category;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.stralom.compras.UI.matchers.CartMatcher.withCartHolder;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.withEmptyList;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.withTextInputError;
import static br.com.stralom.compras.UI.matchers.ProductMatcher.withProductViewHolder;
import static br.com.stralom.compras.UI.matchers.StockMatcher.withStockHolder;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;



@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductUITest {
    private static String DEFAULTPRODUCTNAME = "Register Product Test";
    private Activity activity;
    private static boolean initialized = false;
    private static ProductDAO productDAO;
    private static CategoryDAO categoryDAO;
    private static Category category;
    @Before
    public void init(){
        goToProductTab();
        activity = activityActivityTestRule.getActivity();
        if(!initialized){
            initialized = true;
            productDAO = new ProductDAO(activity);
            categoryDAO = new CategoryDAO(activity);


            category = categoryDAO.add("Product", "Product", R.drawable.cherries);


            //product = productDAO.add("Product", 10,category );
        }
    }




    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);



    @Test
    public void ATestEmptyList(){
        onView(withId(R.id.product_emptyList))
                .check(matches(isDisplayed()));
        String title = activity.getResources().getString(R.string.product_emptyList_title);
        String description = activity.getResources().getString(R.string.product_emptyList_description);
        Drawable drawable = ContextCompat.getDrawable(activity,R.drawable.ic_info);
        Log.d("TESTE", String.valueOf(R.drawable.ic_info));
        onView(withId(R.id.product_emptyList))
                .check(matches(withEmptyList(title,description,drawable)));

    }




    @Test
    public void TestAddingProducts() {
        String testAddingProducts = "TestAddingProducts";
        double value = 10;

        registerProduct(testAddingProducts,String.valueOf(value));
        onView(withId(R.id.product_list)).check(matches(isDisplayed()));

        String price = String.format(activity.getResources().getString(R.string.product_itemList_price),value);
        onView(withId(R.id.product_list)).perform(RecyclerViewActions.scrollToHolder(withProductViewHolder(testAddingProducts,price,category.getIconFlag())));

       // onView(withText(R.string.toast_produc_register)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
        //        .check(matches(isDisplayed()));
    }

    @Test
    public void TestAddingProductsWithCart(){
        String testAddingProducts = "TestAddingProductsWithCart";
        double value = 10;
        double cartAmount = 5;

        fillProductInfo(testAddingProducts,String.valueOf(value));
        onView(withId(R.id.registration_product_addCart)).perform(click());
        onView(withId(R.id.registration_product_addCartAmount)).check(matches(isDisplayed()));
        onView(withId(R.id.registration_product_addCartAmount)).perform(replaceText(String.valueOf(cartAmount)));
        onView(withId(R.id.registration_save)).perform(click());
        String cartTab = activity.getResources().getStringArray(R.array.tab_titles)[0];
        onView(withText(cartTab)).perform(click());
        onView(withId(R.id.cart_list_itemCarts)).perform(RecyclerViewActions.scrollToHolder(withCartHolder(testAddingProducts, String.valueOf(cartAmount))));



    }

    @Test
    public void TestAddingProductWithStock(){
        String testAddingProducts = "TestAddingProductWithStock";
        double value = 10;
        String stockActualAmount = "5";
        String stockMaxAmount = "10" ;


        fillProductInfo(testAddingProducts, String.valueOf(value));
        onView(withId(R.id.registration_product_addStock)).perform(click());
        onView(withId(R.id.registration_product_addStockActualAmount)).perform(replaceText(String.valueOf(stockActualAmount)));
        onView(withId(R.id.registration_product_addStockMaxAmount)).perform(scrollTo());
        onView(withId(R.id.registration_product_addStockMaxAmount)).perform(replaceText(String.valueOf(stockMaxAmount)));
        onView(withId(R.id.registration_save)).perform(click());
        onView(withText(activity.getResources().getStringArray(R.array.tab_titles)[3])).perform(click());
        onView(withId(R.id.list_itemStock)).perform(RecyclerViewActions.scrollToHolder(withStockHolder(testAddingProducts,stockActualAmount,stockMaxAmount)));

    }



    @Test
    public void TestAddingProductsAlreadyRegistered(){
        String productsAlreadyRegistered = "ProductsAlreadyRegistered";
        registerProduct(productsAlreadyRegistered,"10");
        registerProduct(productsAlreadyRegistered,"10");

        onView(withText(R.string.error_product_alreadyRegistered)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void TestEmptyErrorProduct(){
        onView(withId(R.id.product_btn_addNew)).perform(click());
        onView(withId(R.id.registration_save)).perform(click());
        String error = getEmptyErrorMsg();
        onView(withId(R.id.registration_product_nameLayout))
                .check(matches(withTextInputError(error)));
        onView(withId(R.id.registration_product_priceLayout))
                .check(matches(withTextInputError(error)));
    }

    @Test
    public void TestEmptyErrorProductCart(){
        onView(withId(R.id.product_btn_addNew)).perform(click());
        onView(withId(R.id.registration_product_addCart)).perform(click());
        onView(withId(R.id.registration_save)).perform(click());

        String error = getEmptyErrorMsg();
        onView(withId(R.id.registration_product_addCartAmountLayout))
                .check(matches(withTextInputError(error)));
    }

    @NonNull
    private String getEmptyErrorMsg() {
        return activity.getResources().getString(R.string.validation_obrigatoryField);
    }


    @Test
    public void TestEmptyErrorProductStock(){
        onView(withId(R.id.product_btn_addNew)).perform(click());
        onView(withId(R.id.registration_product_addStock)).perform(click());
        onView(withId(R.id.registration_save)).perform(click());
        onView(withId(R.id.registration_product_addStockActualAmountLayout))
                .check(matches(withTextInputError(getEmptyErrorMsg())));
        onView(withId(R.id.registration_product_addStockMaxAmountLayout))
                .check(matches(withTextInputError(getEmptyErrorMsg())));
    }



//
//    @Test
//    public void TestProductValidationNameToLong(){
//        registerProduct("This product name is greater than the max chars permited","12");
//        String errorMsg = activity.getResources().getString(R.string.validation_maxLenght);
//        //onView(withId(R.id.product_dialog_mainLayout)).check((matches(hasErrorText(errorMsg))));
//        onView(withId(R.id.form_productName)).check(matches(withError(errorMsg)));
//    }

    private   void registerProduct(String productName, String productPrice) {
        fillProductInfo(productName, productPrice);
        onView(withId(R.id.registration_save)).perform(click());

    }

    private  void fillProductInfo(String productName, String productPrice) {
        onView(withId(R.id.product_btn_addNew)).perform(click());
        onView(withId(R.id.registration_product_name)).perform(replaceText(productName));
        onView(withId(R.id.registration_product_price)).perform(replaceText(productPrice));
        onView(withId(R.id.registration_product_categories)).perform(click());
        onData(anything()).atPosition(1).inRoot(isPlatformPopup()).perform(click());
    }

    public static void goToProductTab(){
        onView(withText("Produtos")).perform(click());
    }

}
