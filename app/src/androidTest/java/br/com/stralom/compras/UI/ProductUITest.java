package br.com.stralom.compras.UI;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.stralom.compras.UI.matchers.CartMatcher.withCartHolder;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.withError;
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
    private static Product product;
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
            product = productDAO.add("Product", 10,category );
        }
    }




    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);






    @Test
    public void TestAddingProducts() {
        String testAddingProducts = "Test Adding Products";
        double value = 10;
        Drawable drawable = ContextCompat.getDrawable(activity,category.getIconFlag());

        registerProduct(testAddingProducts,String.valueOf(value));
        onView(withId(R.id.product_list)).perform(RecyclerViewActions.scrollToHolder(withProductViewHolder(testAddingProducts,value,drawable)));
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
        double stockActualAmount = 5;
        double stockMaxAmount = 10 ;
        Drawable drawable = ContextCompat.getDrawable(activity,category.getIconFlag());

        fillProductInfo(testAddingProducts, String.valueOf(value));
        onView(withId(R.id.registration_product_addStock)).perform(click());
        onView(withId(R.id.registration_product_addStockActualAmount)).perform(replaceText(String.valueOf(stockActualAmount)));
        onView(withId(R.id.registration_product_addStockMaxAmount)).perform(scrollTo());
        onView(withId(R.id.registration_product_addStockMaxAmount)).perform(replaceText(String.valueOf(stockMaxAmount)));
        onView(withId(R.id.registration_save)).perform(click());
        onView(withText(activity.getResources().getStringArray(R.array.tab_titles)[3])).perform(click());
        onView(withId(R.id.list_itemStock)).perform(RecyclerViewActions
                .scrollToHolder(withStockHolder(testAddingProducts,String.valueOf(stockActualAmount),String.valueOf(stockMaxAmount))));

    }



    @Test
    public void TestAddingProductsAlreadyRegistered(){
        registerProduct(DEFAULTPRODUCTNAME,"10");
        registerProduct(DEFAULTPRODUCTNAME,"10");

        onView(withText(R.string.error_product_alreadyRegistered)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void TestEmptyErrorProduct(){
        onView(withId(R.id.product_btn_addNew)).perform(click());
        onView(withId(R.id.registration_save)).perform(click());
        String error = activity.getResources().getString(R.string.validation_obrigatoryField);
        onView(withId(R.id.registration_product_nameLayout))
                .check(matches(withTextInputError(error)));
        onView(withId(R.id.registration_product_priceLayout))
                .check(matches(withTextInputError(error)));
    }

    @Test
    public void TestProductValidationNameToLong(){
        registerProduct("This product name is greater than the max chars permited","12");
        String errorMsg = activity.getResources().getString(R.string.validation_maxLenght);
        //onView(withId(R.id.product_dialog_mainLayout)).check((matches(hasErrorText(errorMsg))));
        onView(withId(R.id.form_productName)).check(matches(withError(errorMsg)));
    }

    private   void registerProduct(String productName, String productPrice) {
        fillProductInfo(productName, productPrice);
        onView(withId(R.id.registration_save)).perform(click());

    }

    private  void fillProductInfo(String productName, String productPrice) {
        onView(withId(R.id.product_btn_addNew)).perform(click());
        onView(withId(R.id.registration_product_name)).perform(replaceText(productName));
        onView(withId(R.id.registration_product_price)).perform(replaceText(productPrice));
        onView(withId(R.id.registration_product_categories)).perform(click());
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click());
    }

    public static void goToProductTab(){
        onView(withText("Produtos")).perform(click());
    }

}
