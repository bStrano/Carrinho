package br.com.stralom.compras.listerners;

import android.databinding.ObservableList;
import android.support.constraint.Group;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class ListChangeListener<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    private LinearLayout emptyListLayout;

    public ListChangeListener(LinearLayout emptyListLayout, int initialSize) {
        this.emptyListLayout = emptyListLayout;
        updateIfNecessary(initialSize);
    }


    private void showEmptyList(){
        emptyListLayout.setVisibility(View.VISIBLE);
    }

    private void hideEmptyList(){
        emptyListLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onChanged(ObservableList<T> sender) {
        updateIfNecessary(sender.size());
    }

    private void updateIfNecessary(int size) {
        if(size == 0){
            showEmptyList();
        } else {
            hideEmptyList();
        }
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
        if(sender.size() >= 1 ){
            hideEmptyList();
        }
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
        if(sender.size() == 0){
            showEmptyList();
        }
    }


}
