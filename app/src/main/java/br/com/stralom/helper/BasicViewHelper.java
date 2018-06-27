package br.com.stralom.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableArrayList;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.listeners.ListChangeListener;

/**
 * Created by Bruno on 02/02/2018.
 */

public class BasicViewHelper {
    private final Context context;

    public BasicViewHelper(Context context) {
        this.context = context;
    }

    public AlertDialog createDialog(View view, DialogInterface.OnClickListener listenerPositive, DialogInterface.OnClickListener listenerNegative, int titleId){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle(titleId);
        builder.setPositiveButton(R.string.save,  listenerPositive);
        builder.setNegativeButton(R.string.cancel, listenerNegative);
        return builder.create();
    }

    public void loadSpinner(Spinner spinner, List content){
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, content);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    public static void hideSoftKeyBoard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Fill the view information and sets a Listener to update the Visibility when the list update.
     * @param view The main View of the fragment
     *  @param  layoutId Id of the Linear Layout that contains the views of the Empty View Layout
     *
     */
    public static <E> void setUpEmptyListView(View view, ObservableArrayList<E> list, int layoutId, int imageDrawable, int titleStr,  int descriptionStr  ){
        ImageView image = view.findViewById(R.id.emptyList_image);
        TextView title = view.findViewById(R.id.emptyList_title);
        TextView description = view.findViewById(R.id.emptyList_description);
        image.setImageResource(imageDrawable);
        title.setText(titleStr);
        description.setText(descriptionStr);
        LinearLayout layout = view.findViewById(layoutId);
        list.addOnListChangedCallback(new ListChangeListener(layout,list.size()));
    }


}
