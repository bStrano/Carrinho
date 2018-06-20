package br.com.stralom.compras.UI.matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import br.com.stralom.adapters.StockAdapter;
import br.com.stralom.compras.R;

public class StockMatcher {

    public static Matcher<RecyclerView.ViewHolder> withStockHolder(final String productName, final String actualAmount, final String maxAmount){
        return new BoundedMatcher<RecyclerView.ViewHolder, StockAdapter.StockViewHolder>(StockAdapter.StockViewHolder.class){

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found [ Name : " + productName +
                " / Actual Amount: " + actualAmount + " / Max Amount: " + maxAmount);
            }

            @Override
            protected boolean matchesSafely(StockAdapter.StockViewHolder item) {
                TextView nameView = item.itemView.findViewById(R.id.stock_name);
                TextView actualAmountView = item.itemView.findViewById(R.id.stock_actualAmount);
                TextView maxAmountView = item.itemView.findViewById(R.id.stock_maxAmount);


                if(productName == null || actualAmount == null || maxAmount == null){
                    return false;
                }


                return ( nameView.getText().toString().equals(productName) &&
                actualAmountView.getText().toString().equals(actualAmount) &&
                maxAmountView.getText().toString().equals(maxAmount));
            }
        };
    }
}
