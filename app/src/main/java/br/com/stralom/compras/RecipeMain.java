package br.com.stralom.compras;


import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Objects;

import br.com.stralom.adapters.RecipeAdapter;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.entities.Recipe;
import br.com.stralom.helper.SwipeToDeleteCallback;

import static br.com.stralom.helper.BasicViewHelper.setUpEmptyListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeMain extends Fragment {

    public RecipeMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_main, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list_recipe);
        RecipeDAO recipeDAO = new RecipeDAO(getContext());

        ObservableArrayList<Recipe> recipes = (ObservableArrayList<Recipe>) recipeDAO.getAll();
        setUpEmptyListView(view,recipes,R.id.recipe_emptyList,R.drawable.ic_cake,R.string.recipe_emptyList_title,R.string.recipe_emptyList_description);

        RecipeAdapter adapter = new RecipeAdapter(recipes, Objects.requireNonNull(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
