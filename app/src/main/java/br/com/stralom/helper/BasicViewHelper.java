package br.com.stralom.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.stralom.adapters.CartAdapter;
import br.com.stralom.adapters.ItemClickListener;
import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.interfaces.EditMenuInterface;
import br.com.stralom.listeners.ListChangeListener;
import br.com.stralom.listeners.RecyclerTouchListener;

/**
 * Created by Bruno on 02/02/2018.
 */

public abstract class BasicViewHelper<T> extends Fragment {
    public boolean editMode = false;
    protected RecyclerView listView;
    protected View managementMenu;
    protected FloatingActionButton fab;
    protected ObservableArrayList<T> itemCartList;

    public abstract void initializeSuperAtributes();

    public AlertDialog createDialog(View view, DialogInterface.OnClickListener listenerPositive, DialogInterface.OnClickListener listenerNegative, int titleId){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle(titleId);
        builder.setPositiveButton(R.string.save,  listenerPositive);
        builder.setNegativeButton(R.string.cancel, listenerNegative);
        return builder.create();
    }

    public void loadSpinner(Spinner spinner, List content){
        ArrayAdapter arrayAdapter = new ArrayAdapter<T>(getContext(), android.R.layout.simple_list_item_1, content);
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


    /**
     * Setup the managament menu
     * @param mainLayoutId    The R.id of the root layout in the ViewHolder clicked.
     * @param editMenuInterface An interface containing an implementation of buttons Edit and Remove of the managementMenu
     */
    public void setUpManagementMenu(final int mainLayoutId, final EditMenuInterface editMenuInterface ){


        listView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), listView, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(editMode){
                    ViewGroup mainLayout = view.findViewById(mainLayoutId);
                    if(!((CartAdapter)listView.getAdapter()).changeItemBackgroundColor(mainLayout,position)){
                        closeEditModeMenu();
                    }

                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (!editMode) {
                    ViewGroup mainLayout = view.findViewById(mainLayoutId);
                    ((CartAdapter)listView.getAdapter()).changeItemBackgroundColor(mainLayout,position);
                    //changeItemBackgroundColor(mainLayout, position);
                    showEditModeMenu(editMenuInterface);

                }
            }
        }));
    }

    public  void showEditModeMenu(final EditMenuInterface editMenuInterface){
        editMode = true;
        fab.hide();
        managementMenu.setVisibility(View.VISIBLE);

        Button cancel = managementMenu.findViewById(R.id.editMode_cancel);
        final Button edit = managementMenu.findViewById(R.id.editMode_edit);
        final Button delete = managementMenu.findViewById(R.id.editMode_delete);




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.getAdapter().notifyDataSetChanged();
                closeEditModeMenu();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMenuInterface.edit();
                closeEditModeMenu();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMenuInterface.remove();
                closeEditModeMenu();

            }
        });
    }

    private  void closeEditModeMenu() {
        ((CartAdapter)listView.getAdapter()).cleanBackGroundColor();
        managementMenu.setVisibility(View.GONE);
        editMode = false;
        fab.show();
    }














}



