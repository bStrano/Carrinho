package br.com.stralom.compras.helper;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

public abstract class BasicFormValidation implements Validator.ValidationListener {
    protected boolean validationSuccessful;
    protected Activity activity;
    private Validator validator;

    public BasicFormValidation(Activity activity) {
        this.activity = activity;
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public abstract void onValidationSucceeded() ;

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        validationSuccessful = false;
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(activity);

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public Validator getValidator() {
        return validator;
    }

    public boolean isValidationSuccessful() {
        return validationSuccessful;
    }
}
