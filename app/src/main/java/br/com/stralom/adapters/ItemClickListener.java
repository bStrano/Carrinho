package br.com.stralom.adapters;

import android.view.View;

/**
 * Created by Bruno on 12/02/2018.
 */

public interface ItemClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
