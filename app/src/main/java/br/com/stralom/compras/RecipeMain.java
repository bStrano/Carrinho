package br.com.stralom.compras;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.stralom.adapters.RecipeAdapter;
import br.com.stralom.dao.DBHelper;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.entities.Recipe;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeMain extends Fragment {
    private RecipeDAO recipeDAO;

    public RecipeMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_main, container, false);
        ListView list_recipe = view.findViewById(R.id.list_recipe);
        recipeDAO = new RecipeDAO(getContext());

        ArrayList<Recipe> recipes = recipeDAO.getAll();

        RecipeAdapter adapter = new RecipeAdapter(getContext(),recipes);
        list_recipe.setAdapter(adapter);


//        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        Button btn_newRecipe = view.findViewById(R.id.btn_newRecipe);
        btn_newRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SecundaryActivity.class);
                String fragment = RecipeRegistration.class.getSimpleName();
                intent.putExtra(fragment,fragment);
                startActivity(intent);

            }
        });

        return view;
    }

}
