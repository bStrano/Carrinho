package br.com.stralom.compras.adapters;

import android.app.Activity;
import android.databinding.ObservableArrayList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import br.com.stralom.compras.R;
import br.com.stralom.compras.interfaces.EditMenuInterface;

/**
 * Created by Bruno Strano on 06/07/2018.
 */
public abstract class BaseAdapter<T extends RecyclerView.ViewHolder, U> extends RecyclerView.Adapter<T> implements EditMenuInterface {
    protected HashMap<Integer,U> selectedElements;
    protected HashMap<Integer,U> selectedElementsClone;
    protected ObservableArrayList<U> list;
    private Snackbar snackbar;
    protected  Activity activity;



    public BaseAdapter(ObservableArrayList<U> list, Activity activity) {
        this.selectedElements = new HashMap<>();
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public abstract T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) ;

    @Override
    public abstract void onBindViewHolder(@NonNull T holder, int position) ;

    @Override
    public abstract int getItemCount() ;
    public abstract void removePermanently(U item);

    public int getItemPosition(int position){
        return position;
    }

    /**
     * Change the background when the item is Selected or Deselected
     * @param mainLayout The background of the element that was clicked;
     * @param position  Position of the element that was clicked.
     * @return If the list have any selected elements.
     */
    public boolean changeItemBackgroundColor(ViewGroup mainLayout , int position) {
        position = getItemPosition(position);
    Log.d("DEBUG", String.valueOf(position));
        ColorDrawable colorDrawable = (ColorDrawable) mainLayout.getBackground();
        int defaultColor  = Color.parseColor("#FAFAFA");
        // F8F8FF
        int selectedColor = Color.parseColor("#FFCCCC");
        Log.e("DEBUG", String.valueOf(colorDrawable.getColor() == defaultColor));

        if(colorDrawable.getColor() == defaultColor){
            mainLayout.setBackgroundColor(selectedColor);
            selectedElements.put(position,list.get(position));
        } else {
            mainLayout.setBackgroundColor(defaultColor);
            selectedElements.remove(position);

            if(selectedElements.isEmpty()){
                return false;
                // closeEditModeMenu(managementMenu);
            }

        }
        return true;
    }




    @Override
    public void remove() {
        selectedElementsClone = (HashMap<Integer, U>) selectedElements.clone();
        removeTemporally();
        String itemsRemoved;
        if(selectedElements.size() == 1) {
             itemsRemoved = activity.getResources().getString(R.string.list_oneRemovedItems,selectedElements.size());
        } else {
             itemsRemoved = activity.getResources().getString(R.string.list_removedItems,selectedElements.size());
        }

        createUndoSnakbar(itemsRemoved ,null);
        // Remover Definitivamente
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if(selectedElementsClone != null){
                    for (Map.Entry<Integer, U> hash : selectedElementsClone.entrySet()) {
                        removePermanently(hash.getValue());
                    }
                }


            }
        });


        snackbar.show();


    }


    protected void removeTemporally(){

        int removedElements = 0;
        for (Map.Entry<Integer, U> hash : selectedElements.entrySet()) {
            int index = hash.getKey() - removedElements;
            list.remove(index);
            removedElements++;
            notifyItemRemoved(hash.getKey());
            notifyItemRangeRemoved(hash.getKey(),getItemCount());
        }
    }





    private void createUndoSnakbar(String message, final TextView itemCartNameAmount){
        snackbar = Snackbar.make(activity.findViewById(R.id.main_frame), message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.BLUE);
        // Desfazer remoção
            snackbar.setAction(R.string.snackbar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoRemove();

                if( itemCartNameAmount != null) {
                    itemCartNameAmount.setPaintFlags( (itemCartNameAmount.getPaintFlags()) & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                selectedElements = new HashMap<>();
                selectedElementsClone = null;
            }
        });
    }

    protected void undoRemove() {
        for (Map.Entry<Integer,U> hash: selectedElementsClone.entrySet()) {
            int index = hash.getKey();
            list.add(index,  hash.getValue());
            notifyItemInserted(index);
            notifyDataSetChanged();
        }
    }


    public void dismissSnackbar(){
        if(snackbar != null){
            snackbar.dismiss();
        }

    }
}
