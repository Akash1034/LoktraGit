package com.akash.git;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.akash.git.Models.Repository;
import com.akash.git.Utils.Constants;
import com.akash.git.Utils.NetworkCalls;
import com.akash.git.Utils.Responses;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private List<Repository> repositoryList = new ArrayList<>();
    private List<Repository> repositoryList1 = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        postAdapter = new PostAdapter(repositoryList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postAdapter);
        getPosts();

    }

    public void getPosts() {
        NetworkCalls.fetchData(new Responses() {
            @Override
            public void onSuccessResponse(JSONArray response) {

                Log.d(TAG, "post response" + response.toString());
                parseJsonFeed(response);
            }

            @Override
            public void onSuccessResponse(String response) {

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, Constants.GET_URL + "repos/rails/rails/commits");

    }

    private void parseJsonFeed(JSONArray response) {

        Gson gson = new Gson();

        Type listType = new TypeToken<List<Repository>>() {
        }.getType();
        repositoryList = gson.fromJson(response.toString(), listType);
        for (Repository r : repositoryList) {
            r.setBookmarked(false);
        }

        Log.i("LISTSIZE->", repositoryList.size() + "");
        recyclerView.setAdapter(new PostAdapter(repositoryList, getApplication()));
        // postAdapter.notifyDataSetChanged();
//        postAdapter.addItems(repositoryList);

    }

        /*@Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.search,menu);
            SearchView searchView=(SearchView)findViewById(R.id.action_search);
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            return true;
        }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d(TAG, "Submit " + query);
                repositoryList1.addAll(repositoryList);
                for (int i = 0; i < repositoryList.size(); i++) {
                    Repository repository = repositoryList.get(i);
                    String s = repository.getCommit().getAuthor().getName();
                    if (s.equals(query)) {
                        Log.d(TAG, repository.getSha());

                        repositoryList.clear();
                        repositoryList.add(repository);
                        recyclerView.setAdapter(new PostAdapter(repositoryList,getApplicationContext()));

                        break;
                    }
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bookmark:
                List<Repository> s=new ArrayList<>();
                Set<Repository> book=new HashSet<>();
                s.addAll(PostAdapter.getList());
                for(Repository r: s)
                {
                    if(r.isBookmarked())
                        book.add(r);
                }

                for(Repository q : book)
                    Log.d(TAG, "A"+String.valueOf(q.getSha()));

                Bundle extras=new Bundle();
                extras.putSerializable("Set", (Serializable) book);
                Intent i=new Intent(MainActivity.this,BookMarkActivity.class);
                i.putExtras(extras);
                startActivity(i);
            break;
        }
        return true;
    }
}
