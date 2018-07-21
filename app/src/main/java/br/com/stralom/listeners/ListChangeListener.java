package br.com.stralom.listeners;

import android.databinding.ObservableList;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


public class ListChangeListener<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    private LinearLayout layout;


    public ListChangeListener( LinearLayout layout, int initialSize) {
        this.layout = layout;
        updateIfNecessary(initialSize);
    }

    @Override
    public void onChanged(ObservableList<T> sender) {
        Log.e("DEBUG","onchanged");
        updateIfNecessary(sender.size());
    }

    private void updateIfNecessary(int size) {
        if(size == 0){
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
        Log.e("DEBUG","onItemRangeChanged");
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
        Log.e("DEBUG","onItemRangeInserted");
        Log.e("DEBUG","onItemRangeInserted: " + sender.size());

        if(sender.size() >= 1 ){
            layout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
        Log.e("DEBUG","onItemRangeMoved");
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
        Log.e("DEBUG","onItemRangeRemoved");
        if(sender.size() == 0){
            layout.setVisibility(View.VISIBLE);
        }
    }


}
