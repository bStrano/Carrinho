package br.com.stralom.compras.UI;

import android.app.Activity;

import br.com.stralom.compras.R;

/**
 * Created by Bruno Strano on 19/07/2018.
 */
public class UIHelper {

    public static String getTab(Activity activity, int number){
        return activity.getResources().getStringArray(R.array.tab_titles)[number];
    }
}
