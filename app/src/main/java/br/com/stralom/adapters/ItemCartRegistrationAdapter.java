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
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import br.com.stralom.compras.R;

/**
 * Created by Bruno Strano on 14/07/2018.
 */
public abstract class ItemCartRegistrationAdapter<T> extends RecyclerView.Adapter<ItemCartRegistrationAdapter.ViewHolder> implements Filterable{
    protected ArrayList<T> list;
    protected ArrayList<T> listClone;
    protected Activity activity;
    protected boolean filtering = false;
    protected HashMap<T,Integer> selectedPositions;

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
        holder.productAmount.setText("");
        setUpViewHolderLayout(holder, object);


        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddButtonChecked(holder);
                addAmount(object);
                notifyDataSetChanged();
            }
        });
        holder.manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAmount(object);
                notifyDataSetChanged();
            }
        });


    }

    protected void setUpViewHolderLayout(@NonNull ViewHolder holder, T object) {
        if(selectedPositions.containsKey(object)){
            setUpSelectedElements(holder, object);

        } else {
            setUpNotSelectedElements(holder);
        }
    }

    protected void setUpSelectedElements(@NonNull ViewHolder holder, T object) {
        setAddButtonChecked(holder);
        holder.manageButton.setEnabled(true);
        if(selectedPositions.get(object) ==  1){
            holder.manageButton.setBackgroundResource(R.drawable.ic_cancel);
        } else {
            holder.manageButton.setBackgroundResource(R.drawable.ic_remove);
        }


        holder.productAmount.setText(String.valueOf(selectedPositions.get(object)));
    }




    protected void setUpNotSelectedElements(@NonNull ViewHolder holder) {
        holder.manageButton.setChecked(false);
        holder.addButton.setChecked(false);
        holder.addButton.setBackgroundResource(R.drawable.ic_add_with_circle);
        holder.manageButton.setEnabled(false);
        holder.manageButton.setBackgroundResource(0);
    }

    protected void removeAmount(T object) {
        if(selectedPositions.get(object)== 1){

            selectedPositions.remove(object);
        } else {
            selectedPositions.put(object, selectedPositions.get(object) -1);
        }
    }



    protected void addAmount( T object) {
        if(!selectedPositions.containsKey(object)){
            selectedPositions.put(object,1);
        } else {
            selectedPositions.put(object,selectedPositions.get(object) + 1);
            //selectedPositions.put(position, selectedPositions.get(position).getAmount() +1);
        }
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
        public ViewGroup background;


        public ViewHolder(View itemView) {
            super(itemView);
            this.background = itemView.findViewById(R.id.registration_itemCart_mainView);
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
                        if (getName(object).toLowerCase().trim().startsWith(charSequence.toString().toLowerCase().trim())) {
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






}

