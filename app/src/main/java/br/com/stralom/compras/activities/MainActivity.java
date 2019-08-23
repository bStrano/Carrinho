package br.com.stralom.compras.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;


import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import br.com.stralom.compras.adapters.TabFragmentPagerAdapter;
import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.helper.DataViewModel;
import br.com.stralom.compras.listerners.FirebaseGetDataListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    public ArrayList<Product> productList;
    private static final int NUMBER_ASYNCTASK = 1;
    private static int counter = 0;
    private HashMap<String, ArrayList> data;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = this.mAuth.getCurrentUser();

        SharedPreferences sharedPreferences = getSharedPreferences("profiles", Context.MODE_PRIVATE);

        String result = sharedPreferences.getString(getString(R.string.sharedPreferences_selectedProfile), "-1");
        if (Objects.equals(result, "-1")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("selectedProfile", "1_" + user.getUid());
            editor.apply();
        }



        Log.d(TAG,user.getUid());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();

        data = new HashMap<>();
        mTabLayout = findViewById(R.id.mTabLayout);
        mViewPager = findViewById(R.id.mViewPager);

        mViewPager.setOffscreenPageLimit(2);


        final ProductDAO productDAO = new ProductDAO(this);


        productDAO.getAllOrderedByName(sharedPreferences.getString(getString(R.string.sharedPreferences_selectedProfile),""),new FirebaseGetDataListener() {
            @Override
            public void handleListData(List objects) {
               productList = (ArrayList<Product>) objects;
               Log.d(TAG, String.valueOf(productList));
                asyncTaskCompleted("products", (ArrayList) objects);
            }

            @Override
            public void onHandleListDataFailed() {
            }

            @Override
            public void getObject() {
            }
        });




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Teste", Toast.LENGTH_LONG).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public synchronized void asyncTaskCompleted(String key, ArrayList value) {
        counter++;
        data.put(key, value);
        if (counter == NUMBER_ASYNCTASK) {
            mViewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tab_titles), data));
            mTabLayout.setupWithViewPager(mViewPager);
        }


        //start new activity
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_camera:
                // Handle the camera action
                break;
            case R.id.nav_gallery:

                break;
            case R.id.nav_slideshow:

                break;
            case R.id.nav_manage:

                break;
            case R.id.nav_share:

                break;
            case R.id.nav_logout:

                mAuth.signOut();
                for (UserInfo user : user.getProviderData()) {
                    if (user.getProviderId().equals("facebook.com")) {
                        LoginManager.getInstance().logOut();
                    } else if (user.getProviderId().equals("google.com")) {
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .build();
                        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                        mGoogleSignInClient.signOut();
                    }
                }


                Intent logoutIntent = new Intent(this, LoginActivity.class);
                startActivity(logoutIntent);
                finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

