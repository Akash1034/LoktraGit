package com.akash.git;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akash.git.Models.Repository;
import com.akash.git.Utils.AppController;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Akash Srivastava on 29-04-2017.
 */

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.MyViewHolder> {

    private String TAG = "PostAdapter";
    private static ArrayList<Repository> repositoryList;
    private Context context;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public BookMarkAdapter(ArrayList<Repository> repositoryList, Context context) {
        this.repositoryList = repositoryList;
        this.context=context;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookmarkcard,null);
        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        Repository repository = repositoryList.get(position);
      /*  Log.d(TAG, repository.getSha());
        Log.d(TAG, repository.getCommit().getMessage());
        Log.d(TAG, repository.getCommit().getAuthor().getName());
*/
        holder.name.setText(repository.getCommit().getAuthor().getName());
        holder.sha.setText("Commit:"+repository.getSha());
        if(repository.getCommit().getMessage()!=null)
            holder.message.setText("Commit:"+repository.getCommit().getMessage());
        holder.profilePic.setImageUrl(repository.getAuthor().getAvatarUrl(), imageLoader);
        holder.repository=repository;

    }





    @Override
    public int getItemCount() {
        return repositoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView sha;
        TextView message;
        NetworkImageView profilePic;
        Repository repository;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            sha = (TextView) itemView
                    .findViewById(R.id.sha);
            message = (TextView) itemView
                    .findViewById(R.id.message);

            profilePic = (NetworkImageView) itemView
                    .findViewById(R.id.profilePic);
        }
    }
}
