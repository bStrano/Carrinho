package br.com.stralom.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableArrayList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.stralom.adapters.ItemClickListener;
import br.com.stralom.compras.R;
import br.com.stralom.interfaces.EditMenuInterface;
import br.com.stralom.listeners.ListChangeListener;
import br.com.stralom.listeners.RecyclerTouchListener;

/**
 * Created by Bruno on 02/02/2018.
 */

public abstract class BasicViewHelper<T> extends Fragment {
    private boolean editMode = false;

    protected abstract boolean callChangeItemBackgroundColor(View view, int position);



    protected View mainView;
    protected RecyclerView listView;
    protected View managementMenu;
    protected FloatingActionButton fab;
    protected ObservableArrayList<T> list = new ObservableArrayList<>();

    public abstract void initializeSuperAttributes();



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

     * @param editMenuInterface An interface containing an implementation of buttons Edit and Remove of the managementMenu
     */
    public void setUpManagementMenu(final EditMenuInterface editMenuInterface ){


        listView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), listView, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                onClickManagementMenu(view, position);
            }

            @Override
            public void onLongClick(View view, int position) {
                onLongClickManagementMenu(view, position, editMenuInterface);
            }
        }));
    }

    protected void onLongClickManagementMenu(View view, int position, EditMenuInterface editMenuInterface) {
        if (!editMode) {
            callChangeItemBackgroundColor(view,  position);
            showEditModeMenu(editMenuInterface);

        }
    }

    protected void onClickManagementMenu(View view, int position) {
        if(editMode){
            if(!callChangeItemBackgroundColor(view, position)){
                closeEditModeMenu();
            }

        }
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

        managementMenu.setVisibility(View.GONE);
        editMode = false;
        fab.show();
    }


    private boolean isListEmpty(){
        if(list.size() == 0 ){
            return true;
        } else {
            return false;
        }
    }











}



