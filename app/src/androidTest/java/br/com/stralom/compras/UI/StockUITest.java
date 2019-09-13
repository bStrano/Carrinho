package br.com.stralom.compras.UI;

import android.app.Activity;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import br.com.stralom.compras.activities.MainActivity;
import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.CategoryDAO;
import br.com.stralom.compras.dao.DBHelper;
import br.com.stralom.compras.dao.ItemRecipeDAO;
import br.com.stralom.compras.dao.ItemStockDAO;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.dao.RecipeDAO;
import br.com.stralom.compras.dao.StockDAO;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.ItemRecipe;
import br.com.stralom.compras.entities.ItemStock;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.entities.Recipe;
import br.com.stralom.compras.entities.Stock;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.waitFor;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.withError;


@RunWith(Enclosed.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StockUITest {

    private static  void expandFabMenu() {
        onView(isRoot()).perform(waitFor(400));
        onView(withId(R.id.stock_fab_main)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
    }

    @RunWith(AndroidJUnit4.class)
    @LargeTest
    public static class StockWithData {
        private static final String CATEGORY_NAME = "Stock Category";
        private static final String PRODUCT_NAME = "Stock Product";
        private static final String RECIPE_NAME = "Stock Recipe";
        private boolean initialized = false;
        private  static ProductDAO productDAO;
        private static  CategoryDAO categoryDAO;
        private static  ItemStockDAO itemStockDAO;
        private static StockDAO stockDAO;
        private static  RecipeDAO recipeDAO;
        private static  ItemRecipeDAO itemRecipeDAO;

        private  Stock stock;
        private   Product product;
        private   Product product2;
        private   Category category;
        private   Recipe recipe;
        private   ItemStock itemStock;
        private static Activity activity;


        @Rule
        public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

        @Before
        public void init() {
            if(!initialized){
                initialized = true;
                activity = activityActivityTestRule.getActivity();
                productDAO = new ProductDAO(activity);
                categoryDAO = new CategoryDAO(activity);
                recipeDAO = new RecipeDAO(activity);
                itemRecipeDAO = new ItemRecipeDAO(activity);
                itemStockDAO = new ItemStockDAO(activity);
                stockDAO = new StockDAO(activity);
            }


            category = categoryDAO.add(CATEGORY_NAME, CATEGORY_NAME, R.drawable.meat);
            product = productDAO.add(PRODUCT_NAME, 120, category);
            product2 = productDAO.add("Product 2",150,category);
            ItemRecipe itemRecipe = new ItemRecipe(2, product);
            ArrayList<ItemRecipe> products = new ArrayList<>();
            products.add(itemRecipe);
            recipe = recipeDAO.add(RECIPE_NAME, products, null);
            itemRecipeDAO.add(2, product, recipe);
            itemStock = new ItemStock(2,product,1);
            Long stockId = stockDAO.add(new Stock());
            itemStock.setStock(new Stock(stockId));
            itemStockDAO.add(itemStock);

            goToStockTab();
        }

        @After
        public void tearDown() {
            InstrumentationRegistry.getTargetContext().deleteDatabase(DBHelper.DATABASE_NAME);
        }


        @Test
        public void TestExpandFabsMenu() {
            expandFabMenu();
            onView(withId(R.id.stock_fab_addStock)).check(matches(isDisplayed()));
            onView(withId(R.id.stock_fab_consumeProduct)).check(matches(isDisplayed()));
            onView(withId(R.id.stock_fab_consumeRecipe)).check(matches(isDisplayed()));
        }


        public void TestCollapseFabMenu() {
            onView(withId(R.id.stock_fab_main)).check(matches(isDisplayed()));
            onView(withId(R.id.stock_fab_main)).perform(click());
            onView(withId(R.id.stock_fab_main)).check(matches(isDisplayed()));
            onView(withId(R.id.stock_fab_main)).perform(click());
        }


        @Test
        public void TestStartItemStockRegistrationActivity() {
            openAddStockDialog();
            onView(withId(R.id.stock_registration_main)).check(matches(isDisplayed()));
        }

        @Test
        public void TestOpenConsumeRecipeDialog() {
            openConsumeRecipeDialog();

            onView(withText(R.string.itemstock_consume_recipeTitle)).check(matches(isDisplayed()));
            testConsumeDialog();
        }

        @Test
        public void TestConsumeRecipeDialogEmptyAmount(){
            openConsumeRecipeDialog();

            checkEmptyAmountValidation();
        }




        @Test
        public void TestOpenConsumeItemDialog() {
            openConsumeProductDialog();

            onView(withText(R.string.itemstock_consume_productTitle)).check(matches(isDisplayed()));

            testConsumeDialog();


        }

        @Test
        public void TestConsumeItemDialogEmptyAmount(){
            openConsumeProductDialog();

            checkEmptyAmountValidation();
        }


        @Test
        public void TestClickOnListItem(){
            onView(isRoot()).perform(waitFor(400));
            onView(withId(R.id.list_itemStock)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
            onView(isRoot()).perform(waitFor(400));
            onView(withId(R.id.perfil_dialog_title))
                    .check(matches(isDisplayed()))
                    .check(matches(withText(R.string.updateItem)));
            onView(withId(R.id.dialog_itemstock_productname))
                    .check(matches(isDisplayed()))
                    .check(matches(withText(product.getName())));
            String productLabel = activity.getText(R.string.itemStock_update_productTitle).toString();
            onView(withId(R.id.dialog_itemstock_productnameLabel))
                    .check(matches(isDisplayed()))
                    .check(matches(withText(productLabel)));
            onView(withId(R.id.dialog_itemstock_actualAmountLayout))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.dialog_itemstock_actualamount))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("")));
            onView(withId(R.id.dialog_itemstock_maxAmountLayout))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.dialog_itemstock_maxamount))
                    .check(matches(isDisplayed()))
                    .check(matches(withText(String.valueOf(itemStock.getAmount()))));
        }

        private void checkEmptyAmountValidation() {
            String obrigatoryField = activity.getText(R.string.validation_obrigatoryField).toString();
            onView(withText(R.string.save)).perform(click());
            onView(withId(R.id.itemStock_consume_amount)).check(matches(withError(obrigatoryField)));
        }
        private void testConsumeDialog() {
            onView(withId(R.id.itemStock_consume_amount)).check(matches(isDisplayed()));
            onView(withId(R.id.itemStock_consume_amountLabel)).check(matches(isDisplayed()));
            onView(withId(R.id.itemStock_spinner_consume)).check(matches(isDisplayed()));
            onView(withText(R.string.save)).check(matches(isDisplayed()));
            onView(withText(R.string.cancel)).check(matches(isDisplayed()));
        }



        private void goToStockTab() {
            String tab = activity.getResources().getStringArray(R.array.tab_titles)[3];
            onView(withText(tab)).perform(click());
        }
    }

    private static void openConsumeRecipeDialog() {
        expandFabMenu();
        onView(withId(R.id.stock_fab_consumeRecipe)).perform(click());
    }

    @RunWith(AndroidJUnit4.class)
    public static class StockEmpty{
        private Activity activity;

        @Rule
        public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);


        @Before
        public void init(){
            activity = activityActivityTestRule.getActivity();
            String tab = activity.getResources().getStringArray(R.array.tab_titles)[3];
            onView(withText(tab)).perform(click());
        }


        @Test
        public void TestEmptyList() {
           onView(withId(R.id.stock_emptyList)).check(matches(isDisplayed()));
           //onView(withText(R.string.stock_emptyList_title)).check(matches(isDisplayed()));
           onView(withText(R.string.stock_emptyList_description)).check(matches(isDisplayed()));
        }

        @Test
        public void TestOpenStockRegistrationErrorDialog(){
            openAddStockDialog();
            checkErrorDialog(R.string.error_title_procutsNotRegistered, R.string.error_message_productsNotRegistered,R.string.error_positiveButton_productsNotRegistered);
        }

        @Test
        public void TestOpenStockRegistrationErrorDialogPositiveButton(){
            openAddStockDialog();
            onView(withText(R.string.error_positiveButton_productsNotRegistered)).perform(click());
            onView(withId(R.id.product_view_main)).check(matches(isDisplayed()));
        }

        @Test
        public void TestOpenStockRegistrationErrorDialogCancelButton(){
            openAddStockDialog();
            checkDialogCancelButton();
        }

        @Test
        public void TestOpenConsumeProductErrorDialog(){
            openConsumeProductDialog();
            checkErrorDialog(R.string.error_title_itemStockNotRegistered, R.string.error_message_itemStockNotRegistered,R.string.error_positiveButton_itemStockNotRegistered);
        }

        @Test
        public void TestOpenConsumeProductErrorDialogPositiveButton(){
            openConsumeProductDialog();
            onView(withText(R.string.error_positiveButton_itemStockNotRegistered)).perform(click());
            onView(withText(R.string.error_positiveButton_itemStockNotRegistered))
                    .check(doesNotExist());
            onView(withText(R.string.error_message_productsNotRegistered))
                    .check(matches(isDisplayed()));

        }



        @Test
        public void TestOpenConsumeProductErrorDialogCancelButton(){
            openConsumeProductDialog();
            checkDialogCancelButton();
        }





        @Test
        public void TestOpenConsumeRecipeErrorDialog(){
            openConsumeRecipeDialog();
            checkErrorDialog(R.string.error_title_recipesNotRegistered, R.string.error_message_recipesNotRegistered,R.string.error_positiveButton_recipesNotRegistered);
        }

        @Test
        public void TestOpenConsumeRecipeErrorDialogPositiveButton(){
            openConsumeRecipeDialog();
            onView(withText(R.string.error_positiveButton_recipesNotRegistered)).perform(click());
            onView(withText(R.string.error_positiveButton_recipesNotRegistered))
                    .check(doesNotExist());
            onView(withId(R.id.recipe_view_main))
                    .check(matches(isDisplayed()));
        }

        @Test
        public void TestOpenConsumeRecipeErrorDialogCancelButton(){
            openConsumeRecipeDialog();
            checkDialogCancelButton();
        }

        private void checkDialogCancelButton() {
            onView(withText(R.string.cancel)).perform(click());
            onView(withId(R.string.cancel)).check(doesNotExist());
            onView(withId(R.id.stock_main)).check(matches(isDisplayed()));
        }

        private void checkErrorDialog(int errorTitleRes, int errorDescriptionRes, int positiveButtonRes){
            String errorTitle = activity.getResources().getString(errorTitleRes);
            String errorDescription = activity.getResources().getString(errorDescriptionRes);
            String positiveButton = activity.getResources().getString(positiveButtonRes);
            onView(withId(R.id.dialog_error_title))
                    .check(matches(isDisplayed()))
                    .check(matches(withText(errorTitle)));
            onView(withId(R.id.dialog_error_message))
                    .check(matches(isDisplayed()))
                    .check(matches(withText(errorDescription)));
            onView(withText(positiveButton)).check(matches(isDisplayed()));
            onView(withText(R.string.cancel)).check(matches(isDisplayed()));
        }
    }

    private static void openConsumeProductDialog() {
        expandFabMenu();
        onView(withId(R.id.stock_fab_consumeProduct)).perform(click());
    }

    private static void openAddStockDialog() {
        expandFabMenu();
        onView(withId(R.id.stock_fab_addStock)).perform(click());
    }
}
