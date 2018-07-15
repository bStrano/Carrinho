package br.com.stralom.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 14/07/2018.
 */
public abstract class ItemCartRegistrationAdapter<T> extends RecyclerView.Adapter<ItemCartRegistrationAdapter.ViewHolder> implements Filterable{
    private ArrayList<T> list;
    private ArrayList<T> listClone;
    protected Activity activity;
    protected static boolean filtering = false;
    private HashMap<T,Integer> selectedPositions;

    public ItemCartRegistrationAdapter(ArrayList<T> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        listClone = (ArrayList<T>) this.list.clone();
        selectedPositions = new HashMap<>();
    }

    public abstract String getName(T t);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_itemcartregistration,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final T object = getObject(position);
        holder.productName.setText(getName(object));
        //holder.productName.setText(product.getName());
        holder.productAmount.setText("");


        if(selectedPositions.containsKey(object)){
            setAddButtonChecked(holder);
            holder.manageButton.setEnabled(true);
            if(selectedPositions.get(object) ==  1){
                holder.manageButton.setBackgroundResource(R.drawable.ic_cancel);
            } else {
                holder.manageButton.setBackgroundResource(R.drawable.ic_remove);
            }


            holder.productAmount.setText(String.valueOf(selectedPositions.get(object)));
        } else {
            holder.manageButton.setChecked(false);
            holder.addButton.setChecked(false);
            holder.addButton.setBackgroundResource(R.drawable.ic_add_with_circle);
            holder.manageButton.setEnabled(false);
            holder.manageButton.setBackgroundResource(0);
        }


        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddButtonChecked(holder);
               // Product product = getProduct(position);
                if(!selectedPositions.containsKey(object)){
                    selectedPositions.put(object,1);
                } else {
                    selectedPositions.put(object,selectedPositions.get(object) + 1);
                    //selectedPositions.put(position, selectedPositions.get(position).getAmount() +1);

                }
                notifyDataSetChanged();
            }
        });
        holder.manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPositions.get(object)== 1){

                    selectedPositions.remove(object);
                } else {
                    selectedPositions.put(object, selectedPositions.get(object) -1);
                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ToggleButton addButton;
        public ToggleButton manageButton;
        public TextView productName;
        public TextView productAmount;


        public ViewHolder(View itemView) {
            super(itemView);
            this.addButton = itemView.findViewById(R.id.registration_itemCart_btnAdd);
            this.manageButton = itemView.findViewById(R.id.registration_itemCart_btnManagement);
            this.productName = itemView.findViewById(R.id.registration_itemCart_txtProductName);
            this.productAmount = itemView.findViewById(R.id.registration_itemCart_txtProductAmount);
        }
    }

    private void setAddButtonChecked(@NonNull ViewHolder holder) {
        holder.addButton.setChecked(true);
        holder.addButton.setBackgroundResource(R.drawable.ic_added_with_circle);
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<T> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    filtering = false;
                    filteredList.addAll(listClone);
                } else {
                    filtering = true;
                    for (T object : listClone) {
                        if (getName(object).toLowerCase().trim().contains(charSequence.toString().toLowerCase().trim())) {
                            filteredList.add(object);
                        }
                    }
                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list.clear();
                list.addAll((Collection<? extends T>) filterResults.values);
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    private T getObject(int position){
        if(filtering){
            return  list.get(position);
        } else {
            return listClone.get(position);
        }
    }

    public void restoreList(){
        list.clear();
        list.addAll(listClone);
    }

    public HashMap<T, Integer> getSelectedPositions() {
        return selectedPositions;
    }

    public void setSelectedPositions(HashMap<T, Integer> selectedPositions) {
        this.selectedPositions = selectedPositions;
    }
}

