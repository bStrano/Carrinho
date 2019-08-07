package br.com.stralom.compras.UI;

import android.app.Activity;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.core.content.ContextCompat;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import br.com.stralom.compras.activities.MainActivity;
import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.CategoryDAO;
import br.com.stralom.compras.dao.DBHelper;
import br.com.stralom.compras.dao.ItemCartDAO;
import br.com.stralom.compras.dao.ItemRecipeDAO;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.dao.RecipeDAO;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.ItemRecipe;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.entities.Recipe;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static br.com.stralom.compras.UI.matchers.CartMatcher.withCartHolder;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.recyclerViewClickHolder;
import static br.com.stralom.compras.UI.matchers.ItemCartRegistrationMatcher.withItemCartHolder;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CartRegistrationUITest {

    private Activity activity;
    private boolean initialized = false;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private RecipeDAO recipeDAO;
    private ItemRecipeDAO itemRecipeDAO;
    private ItemCartDAO itemCartDAO;

    private Product product1;
    private Product product2;
    private Product product3;

    private ItemRecipe itemRecipe1;
    private ItemRecipe itemRecipe2;

    private Category category;
    private Recipe recipe;
    private Recipe recipe2;

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void setUp(){

    }

    @Before
    public void init(){
        if(!initialized) {
            initialized = true;
            activity = activityActivityTestRule.getActivity();
            productDAO = new ProductDAO(activity);
            categoryDAO = new CategoryDAO(activity);
            recipeDAO = new RecipeDAO(activity);
            itemRecipeDAO = new ItemRecipeDAO(activity);
            itemCartDAO = new ItemCartDAO(activity);
        }
            category = categoryDAO.add("Cart Category", "Cart Category", R.drawable.cherries);
            product1 = productDAO.add("Cart Product", 12, category);
            product2 = productDAO.add("Cart Product 2", 25, category);
            product3 = productDAO.add("Cart 3", 25, category);

            ArrayList<ItemRecipe> itemRecipes = new ArrayList<>();
            itemRecipe1 = new ItemRecipe(2.0, product1);
            itemRecipe2 = new ItemRecipe(3.0, product2);
            itemRecipes.add(itemRecipe1);

            itemRecipes.add(itemRecipe2);
            recipe = recipeDAO.add("Cart Recipe", itemRecipes, null);
            recipe.setIngredients(itemRecipes);
            recipe2 = recipeDAO.add("Recipe Cart", itemRecipes, null);
            recipe2.setIngredients(itemRecipes);
            itemRecipe1.setRecipe(recipe);
            itemRecipe2.setRecipe(recipe);

            itemRecipeDAO.add(itemRecipe1);
            itemRecipeDAO.add(itemRecipe2);
            onView(withId(R.id.itemCart_btn_registerProduct)).perform(click());

    }

    @After
    public void tearDown(){
        InstrumentationRegistry.getTargetContext().deleteDatabase(DBHelper.DATABASE_NAME);
    }


    @Test
    public void testNavigationButton(){
        onView(withId(R.id.registration_itemCart_cancel)).perform(click());
        onView(withId(R.id.cart_view_main)).check(matches(isDisplayed()));
    }

    @Test
    public void testFilteringProducts() {
        typeSearchBar("Cart Product");
        scrollToItemCartHolder(product1.getName(), "");
        scrollToItemCartHolder(product2.getName(), "");
        onView(withText(product3.getName())).check(doesNotExist());
    }

    @Test
    public void testFilteringRecipes() {
        swapToRecipe();
        typeSearchBar("Recipe");
        onView(withText(recipe2.getName())).check(matches(isDisplayed()));
        onView(withText(recipe.getName())).check(doesNotExist());
    }


    @Test
    public void testProductsDisplayed(){
        scrollToItemCartHolder(product1.getName(), "");
        scrollToItemCartHolder(product2.getName(), "");
        scrollToItemCartHolder(product3.getName(), "");
    }

    @Test
    public void testRecipesDisplayed(){
        swapToRecipe();
        scrollToItemCartHolder(recipe.getName(), "");
    }

    @Test
    public void testPlusOneAmount() {
        incrementHolderAmountAtPosition(0,3);
        scrollToItemCartHolder(product1.getName(), "3");
    }

    @Test
    public void testLessOneAmount(){
        incrementHolderAmountAtPosition(0,3);
        decrementHolderAmountAtPosition(0,2);
        scrollToItemCartHolder(product1.getName(), "1");
    }

    @Test
    public void testLessOneToZeroAmount(){
        incrementHolderAmountAtPosition(0,1);
        decrementHolderAmountAtPosition(0,1);
        scrollToItemCartHolder(product1.getName(), "");
    }


    @Test
    public void testRegisterRecipe(){
        swapToRecipe();
        int recipeAmount = 2;
        incrementHolderAmountAtPosition(0,recipeAmount);
        onView(withId(R.id.registration_itemCart_confirm)).perform(click());
        scrollToCartHolder(product1.getName(), String.valueOf((itemRecipe1.getAmount() * recipeAmount)));
    }

    @Test
    public void testRegisterItemCarts(){
        incrementHolderAmountAtPosition(0,2);
        incrementHolderAmountAtPosition(1,3);
        onView(withId(R.id.registration_itemCart_confirm)).perform(click());
        scrollToCartHolder(product1.getName(), "2");
        scrollToCartHolder(product2.getName(), "3");
    }


    @Test
    public void testDifferentiatedSimpleProductBackground(){
        typeSearchBar("Testing");
        int colorId = ContextCompat.getColor(activity,R.color.bg_temproaryProduct);
        scrollToItemCartHolder("Testing", "",colorId);
    }

    @Test
    public void testRegisterTemporaryProduct() {
        String name = "Simple Product";
        typeSearchBar(name);
        incrementHolderAmountAtPosition(0,2);
        onView(withId(R.id.registration_itemCart_confirm)).perform(click());
        scrollToCartHolder(name, "2");
    }



    private void scrollToItemCartHolder(String name, String amount, int colorId) {
        onView(withId(R.id.registration_itemCart_list)).perform(RecyclerViewActions.scrollToHolder(withItemCartHolder(name,amount,colorId)));
    }

    private void scrollToItemCartHolder(String name, String amount) {
        onView(withId(R.id.registration_itemCart_list)).perform(RecyclerViewActions.scrollToHolder(withItemCartHolder(name,amount)));
    }

    private void scrollToCartHolder(String name, String amount) {
        onView(withId(R.id.cart_list_itemCarts)).perform(RecyclerViewActions.scrollToHolder(withCartHolder(name,amount)));
    }

    private void decrementHolderAmountAtPosition(int holderPosition, int amount){
        for(int i = 0 ; i < amount ; i++){
            onView(withId(R.id.registration_itemCart_list)).perform(RecyclerViewActions.actionOnItemAtPosition(holderPosition,recyclerViewClickHolder(R.id.registration_itemCart_btnManagement)));
        }

    }

    private void incrementHolderAmountAtPosition(int holderPosition, int amount){
        for(int i = 0 ; i < amount ; i++){
            onView(withId(R.id.registration_itemCart_list)).perform(RecyclerViewActions.actionOnItemAtPosition(holderPosition,recyclerViewClickHolder(R.id.registration_itemCart_btnAdd)));
        }

    }

    private void typeSearchBar(String text) {
        onView(withId(R.id.search_itemCartRegistration)).perform(click());
        onView(withId(R.id.search_itemCartRegistration)).perform(typeText(text));
    }

    private void swapToRecipe() {
        onView(withId(R.id.registration_itemCart_rbtnRecipes)).perform(click());
    }

}
