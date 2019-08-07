package br.com.stralom.compras.UI;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import br.com.stralom.compras.activities.ItemCartRegistration;
import br.com.stralom.compras.activities.MainActivity;
import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.DBHelper;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


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
