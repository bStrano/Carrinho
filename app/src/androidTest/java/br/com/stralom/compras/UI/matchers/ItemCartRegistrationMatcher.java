package br.com.stralom.compras.UI.matchers;

import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import br.com.stralom.compras.adapters.ItemCartRegistrationAdapter;
import br.com.stralom.compras.R;

public class ItemCartRegistrationMatcher {


    public static Matcher<RecyclerView.ViewHolder> withItemCartHolder(final String productName, final String amount) {
        return new BoundedMatcher<RecyclerView.ViewHolder, ItemCartRegistrationAdapter.ViewHolder>(ItemCartRegistrationAdapter.ViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found [" + amount + "]");
            }


            @Override
            protected boolean matchesSafely(ItemCartRegistrationAdapter.ViewHolder item) {
                return withNameAndAmount(item, productName, amount);
            }


        };
    }


    public static Matcher<RecyclerView.ViewHolder> withItemCartHolder(final String productName, final String amount, final int colorId) {
        return new BoundedMatcher<RecyclerView.ViewHolder, ItemCartRegistrationAdapter.ViewHolder>(ItemCartRegistrationAdapter.ViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found [" + productName + " / " + amount + "/ " + colorId + "]");
            }


            @Override
            protected boolean matchesSafely(ItemCartRegistrationAdapter.ViewHolder item) {
                boolean checkNameAmount = withNameAndAmount(item, productName, amount);
                if (!checkNameAmount) {
                    return false;
                }

                ConstraintLayout main = item.itemView.findViewById(R.id.registration_itemCart_mainView);
                ColorDrawable color = (ColorDrawable) main.getBackground();

                return (color.getColor() == colorId);

            }
        };
    }

    private static boolean withNameAndAmount(ItemCartRegistrationAdapter.ViewHolder item, String productName, String amount) {
        TextView amountView = item.itemView.findViewById(R.id.registration_itemCart_txtProductAmount);
        TextView nameView = item.itemView.findViewById(R.id.registration_itemCart_txtProductName);

        if ((productName == null || amount == null)) {
            return false;
        }


        return amountView.getText().toString().equals(amount) && nameView.getText().toString().equals(productName);
    }
}