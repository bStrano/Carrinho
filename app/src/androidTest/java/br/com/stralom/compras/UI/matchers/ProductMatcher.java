package br.com.stralom.compras.UI.matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;


import br.com.stralom.compras.adapters.ProductAdapter;
import br.com.stralom.compras.R;

/**
 * Created by Bruno Strano on 19/07/2018.
 */
public class ProductMatcher {

    public static Matcher<RecyclerView.ViewHolder> withProductViewHolder(final String productName, final String productPrice, final int drawable ){
        return new BoundedMatcher<RecyclerView.ViewHolder,ProductAdapter.ProductViewHolder>(ProductAdapter.ProductViewHolder.class){



            @Override
            public void describeTo(Description description) {
                description.appendText("No Viewholders found. \n" ).appendText("Values Expected: [").appendValue(productName).appendText(" / ")
                .appendValue(productPrice).appendText(" / ").appendValue(drawable).appendText(" ] ");
            }

            @Override
            protected boolean matchesSafely(ProductAdapter.ProductViewHolder item) {
                TextView nameView = item.itemView.findViewById(R.id.product_itemList_name);
                TextView priceView = item.itemView.findViewById(R.id.product_itemList_price);
                ImageView categoryIconView = item.itemView.findViewById(R.id.product_itemList_categoryIcon);
                int tag = (int) categoryIconView.getTag();

                if(nameView == null|| priceView == null){
                    return false;
                } else {


                    return (nameView.getText().toString().equals(productName)
                            && priceView.getText().toString().equals(productPrice)
                            && (tag == drawable));
                }
            }
        };
    }
}
