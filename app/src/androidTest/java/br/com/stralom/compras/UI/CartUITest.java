package br.com.stralom.compras.UI;

import android.app.Activity;
import android.support.test.espresso.ViewInteraction;
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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.stralom.compras.UI.matchers.CartMatcher.withCartHolder;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.productSpinnerWithText;
import static br.com.stralom.compras.UI.matchers.CustomMatcher.withError;
import static br.com.stralom.compras.UI.matchers.RecipeMatcher.recipeSpinnerWithText;


@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartUITest {
    private Activity activity;
    private ViewInteraction registerSimpleProductBtn;
    private ViewInteraction cartViewMain;
    private static Product product;
    private static Category category;
    private static Recipe recipe;
    private static CategoryDAO categoryDAO;
    private static ProductDAO productDAO;
    private static RecipeDAO recipeDAO;
    private static ItemRecipeDAO itemRecipeDAO;
    private static boolean initialized = false;


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
        goToCartTab();
        if(!initialized){
            initialized = true;
            productDAO = new ProductDAO(activity);
            categoryDAO = new CategoryDAO(activity);
            recipeDAO = new RecipeDAO(activity);
            itemRecipeDAO = new ItemRecipeDAO(activity);

            category = categoryDAO.add("Cart Category","Cart Category", R.drawable.cherries);
            product = productDAO.add("Cart Product",12,category);
            ArrayList<ItemRecipe> itemRecipes = new ArrayList<>();
            ItemRecipe itemRecipe = new ItemRecipe(2,product);
            itemRecipes.add(itemRecipe);
            recipe = recipeDAO.add("Cart Recipe",itemRecipes,null);
            itemRecipeDAO.add(itemRecipe.getAmount(),product,recipe);

        }
    }

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);



    @Test
    public void TestOpenSimpleItemRegisterDialog(){
        onView(withId(R.id.itemcart_btn_registerSimpleProduct)).perform(click());
        onView(withId(R.id.itemcart_dialog_simpleProduct)).check(matches(isDisplayed()));
    }

    @Test
    public void TestAddingSimpleItemWithAmount(){
        String amount = "10";
        String name = "Simple Product";
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

        Product product = productDAO.add("Cart Add Recipe",10,category);
        Product product2 = productDAO.add("Cart Add Recipe 2",20,category);

        ItemRecipe itemRecipe = new ItemRecipe(2,product);
        ItemRecipe itemRecipe2 = new ItemRecipe(1,product2);
        ArrayList<ItemRecipe> itemRecipes = new ArrayList<>();
        itemRecipes.add(itemRecipe);
        itemRecipes.add(itemRecipe2);

        Recipe recipe = recipeDAO.add("Cart add Recipe",itemRecipes,null);
        itemRecipe=  itemRecipeDAO.add(itemRecipe.getAmount(),product,recipe);
        itemRecipe2 = itemRecipeDAO.add(itemRecipe2.getAmount(),product2,recipe);

        //String name = "TestAddingRecipe";
        //setUp(name, name, "10","2");
        registerItemCartRecipe(recipe.getName());


        onView(withId(R.id.cart_list_itemCarts))
                .perform(RecyclerViewActions.scrollToHolder(withCartHolder(itemRecipe.getProduct().getName(), String.valueOf(itemRecipe.getAmount()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.cart_list_itemCarts))
                .perform(RecyclerViewActions.scrollToHolder(withCartHolder(itemRecipe2.getProduct().getName(), String.valueOf(itemRecipe2.getAmount()))))
                .check(matches(isDisplayed()));


    }

    @Test
    public void TestAddingRecipeAlreadyRegistered(){
        Product product = productDAO.add("CartRecipe Already Registered",10,category);

        ItemRecipe itemRecipe = new ItemRecipe(2,product);
        ArrayList<ItemRecipe> itemRecipes = new ArrayList<>();
        itemRecipes.add(itemRecipe);

        Recipe recipe = recipeDAO.add("CartRecipe Already Registered",itemRecipes,null);
        itemRecipeDAO.add(itemRecipe.getAmount(),product,recipe);

        registerItemCartRecipe(recipe.getName());
        registerItemCartRecipe(recipe.getName());

        String amount = String.valueOf(itemRecipe.getAmount() *2);
        onView(withId(R.id.cart_list_itemCarts))
                .perform(RecyclerViewActions.scrollToHolder(withCartHolder(product.getName(),amount)))
                .check(matches(isDisplayed()));
    }




    @Test
   public void TestAddingProduct(){


        registerItemCartProduct(product.getName(),"10");

        cartViewMain
                .check(matches(isDisplayed()));

        onView(withId(R.id.cart_list_itemCarts)).perform(RecyclerViewActions.scrollToHolder(withCartHolder(product.getName())))
            .check(matches(isDisplayed()));
   }



    @Test
   public void TestAddingProductWithEmptyAmount(){

        registerItemCartProduct(product.getName(),"");
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



    private static void goToCartTab(){
        onView(withText("Carrinho")).perform(click());
    }
}
