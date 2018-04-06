package br.com.stralom.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno on 24/02/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements ItemTouchHelperAdapter {
    private List<Product> products;
    private Activity activity;
    private ProductDAO productDAO;
    private boolean undoSwipe;

    public ProductAdapter(List<Product> products, Activity activity) {
        this.products = products;
        this.activity = activity;
        productDAO = new ProductDAO(activity);
        this.undoSwipe = false;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(Double.toString(product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        final Product product = products.get(position);
        final int deletedIndex = position;

        String name = product.getName();

        // Remover Temporiariamente
        products.remove(position);
        notifyItemRemoved(position);

        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.cart_view_main), name + " removido!", Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.BLUE);

        // Desfazer remoção
        snackbar.setAction("DESFAZER", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                products.add(deletedIndex,product);
                notifyItemInserted(deletedIndex);
                undoSwipe = true;
            }
        });

        // Remover Definitivamente
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if(undoSwipe == false) {
                    productDAO.remove(product.getId());
                }
            }
        });

        snackbar.show();
        //

    }

    @Override
    public View getForegroundView(RecyclerView.ViewHolder viewHolder) {
        return  ((ProductViewHolder) viewHolder).viewForeground;
    }

    public class ProductViewHolder extends  RecyclerView.ViewHolder{
        ImageView categoryIcon;
        TextView name;
        TextView price;
        View viewForeground;
        View backgroundView;



        public ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_itemList_name);
            price = itemView.findViewById(R.id.product_itemList_price);
            categoryIcon = itemView.findViewById(R.id.product_itemList_categoryIcon);
            viewForeground = itemView.findViewById(R.id.product_view_foreground);
            backgroundView = itemView.findViewById(R.id.view_background);


        }
    }
}
