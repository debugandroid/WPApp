package com.nplix.wpapp;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends BackHandledFragment  {
public static String TAG="postFrag";
    public List<Posts> mPosts;
    public Button btnGetPost;
    public RecyclerView recyclerView;
    public PostAdapter postAdapter;
    public int page=1;
    public SwipeRefreshLayout swipeRefreshLayout;
    public File file;

    public ListFragment() {
        // Required empty public constructor
    }

    private PostModel postModel;
    private  Observer<List<Posts>> postsObserver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_list, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView= (RecyclerView) getActivity().findViewById(R.id.recyclerHome);
        postAdapter=new PostAdapter(mPosts,getContext(),false,false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout= (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeRefreshLayout);
        //swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.accent_green,R.color.md_red_800,R.color.md_blue_500,R.color.purple);
        mPosts = new ArrayList<Posts>();
        recyclerView.setAdapter(postAdapter);
        postModel= ViewModelProviders.of(getActivity()).get(PostModel.class);
        swipeRefreshLayout.setEnabled(true);
        postModel.getPostsList().observe(this, new Observer<List<Posts>>() {
            @Override
            public void onChanged(@Nullable List<Posts> posts) {
                postAdapter.setData(posts);
                postAdapter.notifyItemChanged(postModel.getChangeIndex());
                Log.d(TAG,"On Changed method called");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        postModel.getRefresh().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(@Nullable Integer integer) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                postModel.RefreshData();


            }
        });


    }


    public void setData(List<Posts> posts){
        recyclerView.setAdapter(postAdapter);
        postAdapter.setData(mPosts);
        swipeRefreshLayout.setRefreshing(false);
        postAdapter.notifyDataSetChanged();
    }
    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public void RefreshLayout() {

    }


}
