package br.com.stralom.compras.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by Bruno Strano on 31/01/2018.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
    View getForegroundView(RecyclerView.ViewHolder viewHolder);
}
