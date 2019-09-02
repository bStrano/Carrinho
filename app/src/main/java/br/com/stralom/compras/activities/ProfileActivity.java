package br.com.stralom.compras.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.adapters.ProfileItemAdapter;
import br.com.stralom.compras.entities.Profile;

public class ProfileActivity extends AppCompatActivity {
    private RecyclerView profileRecyclerView;

    private ProfileItemAdapter profileItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.profileRecyclerView = findViewById(R.id.list_profiles);

        Intent intent = getIntent();
        ArrayList<Profile> profiles = intent.getParcelableArrayListExtra("profiles");

        Log.d("Teste", profiles.toString());
        this.profileItemAdapter = new ProfileItemAdapter(profiles, this);
        this.profileRecyclerView.setAdapter(profileItemAdapter);
        this.profileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.profileRecyclerView.setHasFixedSize(true);
    }
}
