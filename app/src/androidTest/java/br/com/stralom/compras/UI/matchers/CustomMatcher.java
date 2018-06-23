package br.com.stralom.compras.UI.matchers;

import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.test.espresso.Root;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import br.com.stralom.entities.Product;

import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.v4.util.Preconditions.checkNotNull;

public class CustomMatcher {

    // https://stackoverflow.com/questions/21045509/check-if-a-dialog-is-displayed-with-espresso/34465170
    public static Matcher<Root> isToast() {
        return new TypeSafeMatcher<Root>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is toast");
            }

            @Override
            public boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                    IBinder windowToken = root.getDecorView().getWindowToken();
                    IBinder appToken = root.getDecorView().getApplicationWindowToken();
                    if (windowToken == appToken) {
                        // windowToken == appToken means this window isn't contained by any other windows.
                        // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                        return true;
                    }
                }
                return false;
            }
        };

}

    // https://stackoverflow.com/questions/21417954/espresso-thread-sleep
    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    // https://stackoverflow.com/a/34286462/9175197
    public static Matcher withError(final String expected) {
        return new BoundedMatcher<View,EditText>(EditText.class) {

            @Override
            protected boolean matchesSafely(EditText item) {
                return item.getError().toString().equals(expected);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Not found error message [" + expected + "]");
            }
        };
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static Matcher<Object> productSpinnerWithText(final String expectedName) {
        return new BoundedMatcher<Object, Product>(Product.class) {

            @Override
            protected boolean matchesSafely(Product item) {
                return item.getName().equals(expectedName);
            }


            @Override
            public void describeTo(Description description) {
                description.appendText("Not found  [" + expectedName + "]");
            }
        };
    }



}



