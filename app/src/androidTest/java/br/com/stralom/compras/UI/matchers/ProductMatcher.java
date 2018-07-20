package br.com.stralom.compras.UI.matchers;

import android.graphics.drawable.Drawable;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;


import br.com.stralom.adapters.ProductAdapter;
import br.com.stralom.compras.R;

/**
 * Created by Bruno Strano on 19/07/2018.
 */
public class ProductMatcher {

    public static Matcher<RecyclerView.ViewHolder> withProductViewHolder(final String productName, final double productPrice, final Drawable drawable ){
        return new BoundedMatcher<RecyclerView.ViewHolder,ProductAdapter.ProductViewHolder>(ProductAdapter.ProductViewHolder.class){

            @Override
            public void describeTo(Description description) {
                description.appendText("No Viewholders found");
            }

            @Override
            protected boolean matchesSafely(ProductAdapter.ProductViewHolder item) {
                TextView nameView = item.itemView.findViewById(R.id.product_itemList_name);
                TextView priceView = item.itemView.findViewById(R.id.product_itemList_price);
                ImageView categoryIconView = item.itemView.findViewById(R.id.product_itemList_categoryIcon);

                if(nameView == null|| priceView == null|| categoryIconView == null){
                    return false;
                } else {
                    String nameHolder = nameView.getText().toString();
                    String priceHolder = priceView.getText().toString();
                    Drawable drawableHolder = categoryIconView.getDrawable();

                    return (nameHolder.equals(productName) && priceHolder.equals(productPrice) && drawable.equals(drawableHolder));
                }
            }
        };
    }
}
