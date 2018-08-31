package br.com.stralom.compras.UI;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import br.com.stralom.compras.ItemCartRegistration;
import br.com.stralom.compras.MainActivity;
import br.com.stralom.compras.R;
import br.com.stralom.dao.DBHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@SmallTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartUITest {


   @BeforeClass
   public static void setUp(){
       InstrumentationRegistry.getTargetContext().deleteDatabase(DBHelper.DATABASE_NAME);
  }



    @After
    public void tearDown(){
        InstrumentationRegistry.getTargetContext().deleteDatabase(DBHelper.DATABASE_NAME);
    }



    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);



    @Test
    public void ATestEmptyListView(){
        onView(withId(R.id.cart_list_itemCarts)).check(matches(isDisplayed()));
    }

    @Test
    public void TestOpenRegistrationActivity(){
        Intents.init();
        onView(withId(R.id.itemCart_btn_registerProduct)).perform(click());
        intended(hasComponent(ItemCartRegistration.class.getName()));
    }



}
