package br.com.stralom.compras;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

public class SecundaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secundary);
        Bundle extras = getIntent().getExtras();

        if(Objects.requireNonNull(extras).getString(RecipeRegistration.class.getSimpleName()) != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.secundary_mainFrame, new RecipeRegistration());
            fragmentTransaction.commit();
        }
    }
}
