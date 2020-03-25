package com.uzair.pokemonnewspaperapp.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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

public class AnimeActivity extends AppCompatActivity {

    public static final String url = "https://www.pokemonnewspaper.com.au/wp-json/wp/v2/posts?categories=861&per_page=100";
    private Toolbar mToolbar;
    private RecyclerView newsPaperRvList;
    private GridLayoutManager layoutManager;
    private Gson gson;
    private List<MyNewsPaperPosts> postList = new ArrayList<>();
    private AdapterForPostRecyclerView adapterForPostRecyclerView;
    private ProgressDialog progressDialog;
    private ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        initViews();

        if (isConnected()) {

            getJsonResponseData();
        }
       else
        {
            snackBar("Sorry no internet connection");
        }

    }

    private void initViews()
    {
       layout = findViewById(R.id.containerInAnime);

        mToolbar = findViewById(R.id.animeToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gson = new Gson();

        // recycler view
        newsPaperRvList = findViewById(R.id.animePapersRvList);
        layoutManager = new GridLayoutManager(this , 1);
        newsPaperRvList.setLayoutManager(layoutManager);

        adapterForPostRecyclerView = new AdapterForPostRecyclerView(this, postList);

        progressDialog = new ProgressDialog(this);
    }


    private void getJsonResponseData()
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
                    Toast.makeText(AnimeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "onErrorResponse: " + error);
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
}
