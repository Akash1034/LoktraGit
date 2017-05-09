package com.akash.git;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.akash.git.Models.Repository;
import com.akash.git.Utils.InternalStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class BookMarkActivity extends AppCompatActivity {

    HashSet<Repository> s = new HashSet<>();
    ArrayList<Repository> repositories = new ArrayList<>();
    String TAG = "Bookmark activity";
    String KEY = "Models";
    private RecyclerView recyclerView;
    private BookMarkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
            s = (HashSet<Repository>) bundle.getSerializable("Set");
        if (!s.isEmpty()) {
            Log.d(TAG, "IF");
            repositories.addAll(s);
            try {
                InternalStorage.writeObject(this, KEY, repositories);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            try {
                // Save the list of entries to internal storage
                // Retrieve the list from internal storage
                repositories = (ArrayList<Repository>) InternalStorage.readObject(this, KEY);

                // Display the items from the list retrieved.
                Log.d(TAG, "A" + repositories.toString());

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (ClassNotFoundException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new BookMarkAdapter(repositories, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void finish() {
        super.finish();
        s.clear();
        repositories.clear();
    }
}
