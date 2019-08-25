package br.com.stralom.compras.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import br.com.stralom.compras.R;
import br.com.stralom.compras.adapters.RecipeAdapter;
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

        list.addAll(((MainActivity) getActivity()).recipeList);

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
                intent.putParcelableArrayListExtra("products",((MainActivity) getActivity()).productList);
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
                boolean receitaExistente = false;
                ArrayList<Recipe> recipes = ((MainActivity) getActivity()).recipeList;
                for(int i = 0 ; i < recipes.size() ; i++){
                    if(recipe.getName().equals(recipes.get(i).getName())){
                        recipes.set(i,recipe);
                        Toast.makeText(getContext(),"Receita Atualizada", Toast.LENGTH_LONG).show();
                        receitaExistente = true;
                        break;
                    }
                }
                if(!receitaExistente){
                    Toast.makeText(getContext(),"Receita adicionada", Toast.LENGTH_LONG).show();
                    recipes.add(recipe);
                }

                list.clear();
               list.addAll( ((MainActivity) getActivity()).recipeList);

                listView.getAdapter().notifyDataSetChanged();
            }
        }
    }


}
