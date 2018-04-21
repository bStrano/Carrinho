package br.com.stralom.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno on 24/02/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements ItemTouchHelperAdapter {
    private final List<Product> products;
    private final Activity activity;
    private final ProductDAO productDAO;
    private boolean undoSwipe;
    private Resources res;

    public ProductAdapter(List<Product> products, Activity activity) {
        this.products = products;
        this.activity = activity;
        res = activity.getResources();
        productDAO = new ProductDAO(activity);
        this.undoSwipe = false;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(String.format(res.getString(R.string.product_itemList_price), product.getPrice()));
        Log.e("Teste",product.toString());
         holder.categoryIcon.setImageResource(product.getCategory().getIconFlag());
        //holder.categoryIcon.setImageDrawable(R.drawable.ic_add);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
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
                if(!undoSwipe) {
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

    class ProductViewHolder extends  RecyclerView.ViewHolder{
        final ImageView categoryIcon;
        final TextView name;
        final TextView price;
        final View viewForeground;
        final View backgroundView;



        ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_itemList_name);
            price = itemView.findViewById(R.id.product_itemList_price);
            categoryIcon = itemView.findViewById(R.id.product_itemList_categoryIcon);
            viewForeground = itemView.findViewById(R.id.product_view_foreground);
            backgroundView = itemView.findViewById(R.id.view_background);


        }
    }

    // Retorna o icone de acordo com a categoria. As categorias
    int getIcon(Product product){
        String productName = product.getName();
        String categories[] = res.getStringArray(R.array.categories);

        return 0;
    }
}
