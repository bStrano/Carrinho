package br.com.stralom.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import br.com.stralom.adapters.ItemClickListener;

/**
 * Created by Bruno on 21/02/2018.
 */

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

    private ItemClickListener clicklistener;
    private GestureDetector gestureDetector;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ItemClickListener clicklistener) {
        this.clicklistener = clicklistener;
        this.gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e){
                View child = recyclerView.findChildViewUnder(e.getX(),e.getY());

                if ( (child != null)  && (clicklistener != null) ){
                    clicklistener.onLongClick(child,recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child=rv.findChildViewUnder(e.getX(),e.getY());
        if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
            clicklistener.onClick(child,rv.getChildAdapterPosition(child));
        }

        return false;
    }


    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}