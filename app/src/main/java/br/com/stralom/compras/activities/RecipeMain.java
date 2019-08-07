package br.com.stralom.compras.activities;


import android.content.Intent;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import br.com.stralom.compras.adapters.RecipeAdapter;
import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.RecipeDAO;
import br.com.stralom.compras.entities.Recipe;
import br.com.stralom.compras.helper.BasicViewHelper;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeMain extends BasicViewHelper<Recipe>{
    private static final int REGISTER_RECIPE_REQUEST = 1;

    @Override
    public void initializeSuperAttributes() {
        listView = mainView.findViewById(R.id.list_recipe);
        managementMenu = mainView.findViewById(R.id.recipe_management_list);
        fab = mainView.findViewById(R.id.btn_newRecipe);
    }

    @Override
    public boolean callChangeItemBackgroundColor(View view, int position) {
        ViewGroup background = view.findViewById(R.id.recipe_view_foreground);
        return ((RecipeAdapter) listView.getAdapter()).changeItemBackgroundColor(background,position);

    }



    public RecipeMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_recipe_main, container, false);

        initializeSuperAttributes();

        RecipeDAO recipeDAO = new RecipeDAO(getContext());

        list = (ObservableArrayList<Recipe>) recipeDAO.getAll();
        setUpEmptyListView(mainView,list,R.id.recipe_emptyList,R.drawable.ic_cake,R.string.recipe_emptyList_title,R.string.recipe_emptyList_description);

        RecipeAdapter adapter = new RecipeAdapter(list, Objects.requireNonNull(getActivity()));
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(adapter);

        setUpManagementMenu(adapter);

//        FloatingActionButton fab = getActivity().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),RecipeRegistration.class);
                startActivityForResult(intent,REGISTER_RECIPE_REQUEST);

            }
        });

        return mainView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if( requestCode == REGISTER_RECIPE_REQUEST ){
            if(resultCode == RESULT_OK){
                Recipe recipe = data.getParcelableExtra("recipe");
                list.add(recipe);
                listView.getAdapter().notifyDataSetChanged();
            }
        }
    }


}
