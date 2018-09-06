package br.com.stralom.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import br.com.stralom.compras.R;

public class DialogHelper {


    public static void createErrorDialog(Activity activity, int errorTitleRes, int errorMessageRes, int positiveButtonTitleRes, DialogInterface.OnClickListener positiveButton){

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_error_message,null);


        TextView titleView = view.findViewById(R.id.dialog_error_title);
        TextView messageView = view.findViewById(R.id.dialog_error_message);

        Log.d("Teste","xxxx");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        titleView.setText(errorTitleRes);
        messageView.setText(errorMessageRes);
        builder.setView(view)
                .setPositiveButton(positiveButtonTitleRes, positiveButton)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                 });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

}
