package br.com.stralom.helper;

import android.content.ClipDescription;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;

/**
 * Created by Bruno Strano on 30/01/2018.
 */

public class productsDragEventListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {

        final int action = dragEvent.getAction();

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    view.setBackgroundColor(Color.BLUE);
                    view.invalidate();
                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                view.setBackgroundColor(Color.GREEN);
                view.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                return true;


        }
        return true;
    }
}
