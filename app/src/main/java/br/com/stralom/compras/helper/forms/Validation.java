package br.com.stralom.compras.helper.forms;

import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.Pair;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import br.com.stralom.compras.R;


/**
 * Created by Bruno Strano on 18/07/2018.
 */
public class Validation {
    private Activity activity;

    public Validation(Activity activity) {
        this.activity = activity;
    }

    private   boolean isEmpty(TextInputEditText editText){
        Log.d("TESTE", String.valueOf(editText.getText().length() == 0));

        return (editText.getText().length() == 0 );
    }


    @SafeVarargs
    public final boolean validateEmpty(Pair<TextInputLayout, TextInputEditText>... args){
        boolean isValid = true;
            Log.d("TESTE" , String.valueOf(args.length));
        for(Pair<TextInputLayout,TextInputEditText> pair: args){

            if(isEmpty(pair.second)){
                pair.first.setError(activity.getResources().getString(R.string.validation_obrigatoryField));
                isValid = false;
            } else {
                pair.first.setErrorEnabled(false);
            }
        }

        return isValid;
    }
}
