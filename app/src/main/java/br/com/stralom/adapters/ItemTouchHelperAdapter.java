package br.com.stralom.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Bruno Strano on 31/01/2018.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
    View getForegroundView(RecyclerView.ViewHolder viewHolder);
}
