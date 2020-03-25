package com.uzair.pokemonnewspaperapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.uzair.pokemonnewspaperapp.Adapters.AdapterForPostRecyclerView;
import com.uzair.pokemonnewspaperapp.Models.MyNewsPaperPosts;
import com.uzair.pokemonnewspaperapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private Toolbar mToolbar;
    private NavigationView navigationView;
    private RecyclerView newsPaperRvList;
    private GridLayoutManager layoutManager;
    private Gson gson;
    private String url = "https://www.pokemonnewspaper.com.au/wp-json/wp/v2/posts?categories=2&per_page=100";
    private List<MyNewsPaperPosts> postList = new ArrayList<>();
    private AdapterForPostRecyclerView adapterForPostRecyclerView;
    private ConstraintLayout layout;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        if(isConnected()) {
            getJsonResponse();
        }
        else
        {
            snackBar("Sorry no internet connection");
        }

    }


    private void initViews() {

        layout = findViewById(R.id.container);
        gson = new Gson();

        progressDialog = new ProgressDialog(this);

        mToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);

        navigationView = findViewById(R.id.navigationMenu);

        Menu menu = navigationView.getMenu();

        MenuItem tools= menu.findItem(R.id.gamesNew);
        MenuItem tit  = menu.findItem(R.id.tvNews);

        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);

        SpannableString s2 = new SpannableString(tit.getTitle());
        s2.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s2.length(), 0);
        tit.setTitle(s2);


        navigationView.setNavigationItemSelectedListener(this);

        //create the reference of drawer layout
        DrawerLayout layout = findViewById(R.id.drawer_layout);
        //create the object of action bar drawer class and pass the drawer layout to it and also open and close operation
        toggle = new ActionBarDrawerToggle(this, layout, R.string.open, R.string.close);
        layout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // recycler view
        newsPaperRvList = findViewById(R.id.newsPapersRvList);
        layoutManager = new GridLayoutManager(this , 1);
        newsPaperRvList.setLayoutManager(layoutManager);

        adapterForPostRecyclerView = new AdapterForPostRecyclerView(this, postList);


    }

    private void getJsonResponse()
    {


        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {

                    Log.d("Reqeust", "onResponse: " + response);
                    JSONArray jArray = new JSONArray(response);

                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++){

                        JSONObject json_data = jArray.getJSONObject(i);

                        MyNewsPaperPosts rvdata = gson.fromJson(String.valueOf(json_data), MyNewsPaperPosts.class);
                        postList.add(rvdata);
                        newsPaperRvList.setAdapter(adapterForPostRecyclerView);
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "onErrorResponse: " + error);
                progressDialog.dismiss();
            }
        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);


    }

    private void snackBar(String message) {
        Snackbar snackbar = Snackbar.make(layout,
                message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();

    }

    //check internet connection
    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean hasConnect = (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable());

        return hasConnect;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //on toggle button the navigation drawer action
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.anime) {
            startActivity(new Intent(this , AnimeActivity.class));


        } else if (item.getItemId() == R.id.pokemonMerch) {

            startActivity(new Intent(this , PokemonMerchActivity.class));


        } else if (item.getItemId() == R.id.pokemonTcg)
        {

            startActivity(new Intent(this , PokemonGameActivity.class));


        } else if (item.getItemId() == R.id.allNews) {

            startActivity(new Intent(this , MainActivity.class));

        }
        else if(item.getItemId() == R.id.pokemoneEvents)
        {
             startActivity(new Intent(this , PokemonEventActivity.class));
        }
        else if(item.getItemId() == R.id.nintendoSwitch)
        {
          startActivity(new Intent(this , NintendoSwitchActivity.class));
        }

        else if(item.getItemId() == R.id.pokemonGo)
        {
          startActivity(new Intent(this , PokemonGoActivity.class));

        }


        else if(item.getItemId() == R.id.mobile)
        {
         startActivity(new Intent(this , MobilesActivity.class));

        }

        else if(item.getItemId() == R.id.englishSubAnime)
        {
            startActivity(new Intent(this , EnglishSubAnimeActivity.class));

        }

        else if(item.getItemId() == R.id.movie)
        {
          startActivity(new Intent(this , MoviesActivity.class));
        }


        return false;
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setMessage("Do you want to close the app?");
        alert.setCancelable(false);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MainActivity.this.finish();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();


    }
}
