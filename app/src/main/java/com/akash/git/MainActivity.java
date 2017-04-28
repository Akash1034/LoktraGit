package com.akash.git;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.akash.git.Models.Repository;
import com.akash.git.Utils.Constants;
import com.akash.git.Utils.NetworkCalls;
import com.akash.git.Utils.Responses;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private List<Repository> repositoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        postAdapter = new PostAdapter(repositoryList,getApplicationContext());
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

        Type listType = new TypeToken<List<Repository>>() {}.getType();
        repositoryList = gson.fromJson(response.toString(), listType);
        Log.i("LISTSIZE->", repositoryList.size() +"");
        recyclerView.setAdapter(new PostAdapter(repositoryList,getApplication()));
       // postAdapter.notifyDataSetChanged();
//        postAdapter.addItems(repositoryList);

    }


}
