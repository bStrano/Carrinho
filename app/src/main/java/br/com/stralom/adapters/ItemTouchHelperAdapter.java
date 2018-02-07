package br.com.stralom.adapters;

/**
 * Created by Bruno Strano on 31/01/2018.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
