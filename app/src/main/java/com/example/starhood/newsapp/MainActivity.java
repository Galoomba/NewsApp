package com.example.starhood.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.starhood.newsapp.RecycleView.DividerItemDecoration;
import com.example.starhood.newsapp.RecycleView.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<List<News>> {


    private TextView empView;
    private ProgressBar indicator;
    private RecyclerView list;

    private final String API_URL = "http://content.guardianapis.com/search";
    private String searchFor = null;

    private NewsAdapter mAdapter;
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        empView = (TextView) findViewById(R.id.empty_view);
        indicator = (ProgressBar) findViewById(R.id.loading_indicator);
        list = (RecyclerView) findViewById(R.id.Recycle_view);


        list.setVisibility(View.GONE);
        empView.setVisibility(View.GONE);
        indicator.setVisibility(View.VISIBLE);

        initListView(new ArrayList<News>());

        LoaderManager loaderManager = null;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, MainActivity.this);

            empView.setVisibility(View.GONE);
            indicator.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);

        } else {
            indicator.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
            empView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "No Internert Conection", Toast.LENGTH_LONG).show();
        }

    }

    void initListView(ArrayList<News> data) {

        mAdapter = new NewsAdapter(data, this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        list.setAdapter(mAdapter);
        list.setLayoutManager(mLayoutManager);

        //the divider line
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        //onClick Event
        list.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                News news = mAdapter.getItem(position);
                String url = news.getaURL();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        indicator.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);

        Uri baseUri = Uri.parse(API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", "73e36ccd-37b7-44bf-8f2c-ef676a8d84b7");

        if (searchFor != null)
            uriBuilder.appendQueryParameter("q", searchFor);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        indicator.setVisibility(View.GONE);
        mAdapter.clear();


        if (data != null && !data.isEmpty()) {
            list.setVisibility(View.VISIBLE);
            empView.setVisibility(View.GONE);
            mAdapter.addAll(data);
        } else {
            list.setVisibility(View.GONE);
            empView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "No Data to be displayed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchFor = query;
        getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setOnQueryTextListener(this);
        return true;
    }


}
