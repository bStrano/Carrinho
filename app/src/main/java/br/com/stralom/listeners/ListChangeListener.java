package br.com.stralom.listeners;

import android.databinding.ObservableList;
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

    }

    @Override
    public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
        if(sender.size() == 1 ){
            layout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {

    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
        if(sender.size() == 0){
            layout.setVisibility(View.VISIBLE);
        }
    }


}
