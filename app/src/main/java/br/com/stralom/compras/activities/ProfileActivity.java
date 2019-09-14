package br.com.stralom.compras.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.adapters.ProfileItemAdapter;
import br.com.stralom.compras.entities.Profile;

public class ProfileActivity extends AppCompatActivity {
    private RecyclerView profileRecyclerView;
    private RecyclerView profileSharedView;
    private TextView emptySharedProfiles;
    private TextView shareCodeView;
    private ProfileItemAdapter profileItemAdapter;
    private FirebaseAuth fb;
    private Toolbar toolbar;
    public ArrayList<Profile> profiles;
    public ArrayList<Profile> sharedProfiles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));

        this.profileRecyclerView = findViewById(R.id.list_profiles);
        this.toolbar = findViewById(R.id.profile_toolbar);
        this.profileSharedView = findViewById(R.id.profile_share_list);
        this.emptySharedProfiles = findViewById(R.id.profile_shareprofile_empty);

        toolbar.setTitle("Gerenciar Perfil");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fb = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        profiles = intent.getParcelableArrayListExtra("profiles");
        sharedProfiles = intent.getParcelableArrayListExtra("sharedProfiles");

//        Log.d("Teste", profiles.toString());
        this.profileItemAdapter = new ProfileItemAdapter(profiles, this);
        this.profileRecyclerView.setAdapter(profileItemAdapter);
        this.profileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.profileRecyclerView.setHasFixedSize(true);


    }


}
