package br.com.stralom.compras.UI;

import android.app.Activity;
import android.app.Fragment;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.internal.util.Checks;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


import br.com.stralom.adapters.ProductAdapter;
import br.com.stralom.adapters.StockAdapter;
import br.com.stralom.compras.MainActivity;
import br.com.stralom.compras.R;


import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductUITest {
    private Activity activity;
    @Before
    public void init(){
        activity = activityActivityTestRule.getActivity();
        onView(withText("Produtos")).perform(click());
    }

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void aTestOpenRegisterProductDialog(){
        onView(ViewMatchers.withId(R.id.product_btn_addNew)).perform(click());
        onView(withId(R.id.product_dialog_mainLayout))
                .check(matches(isDisplayed()));
    }

    @Test
    public void bTestAddingProducts() {
        registerProduct();

        onView(withText(R.string.toast_produc_register)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void cTestAddingProductsAlreadyRegistered(){
        registerProduct();

        onView(withText(R.string.toast_product_alreadyRegistered)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }


    private void registerProduct() {
        onView(withId(R.id.product_btn_addNew)).perform(click());
        onView(withId(R.id.form_productName)).perform(replaceText("Register Product Test"));
        onView(withId(R.id.form_productPrice)).perform(replaceText("12"));
        onView(withId(R.id.form_productCategory)).perform(click());
        onData(anything()).atPosition(1).inRoot(isPlatformPopup()).perform(click());
        onView(withText(R.string.save)).perform(click());
    }
}
