package br.com.stralom.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import br.com.stralom.compras.R;

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


}
