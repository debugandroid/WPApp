package com.nplix.wpapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.List;

import static com.nplix.wpapp.ListFragment.TAG;

/**
 * Created by PK on 1/10/2018.
 */

public class PostModel extends AndroidViewModel {

    @Nullable
    private JsonLiveData postsList;
    private int index;

    public MutableLiveData getRefresh() {
        return refresh;
    }


    private MutableLiveData<Integer> refresh=new MutableLiveData<>();

    public PostModel(@NonNull Application application) {
        super(application);
        if(postsList==null)
            postsList=new JsonLiveData(application);

    }

    public MutableLiveData<List<Posts>> getPostsList() {
        return  postsList;
    }

    public int getChangeIndex(){
        return index;
    }
    public void RefreshData(){
        refresh.setValue(0);
        postsList=new JsonLiveData(this.getApplication());
    }
/*
    private void setPostsList(@Nullable List<Posts> postsList) {
        Log.d("ViewModel", "Setting data to ViewModel");
        this.postsList = postsList;


    }*/


    public class JsonLiveData extends MutableLiveData<List<Posts>>{
        private List<Posts> mPosts=new ArrayList<Posts>();
        private final Context context;
        private int page=1;

        public JsonLiveData(Context context){
            this.context=context;
            LoadData();
        }

        private void LoadData() {

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, Config.base_url+"wp/v2/posts/?page="+page, null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response) {
                            // display response
                            Log.d(TAG, response.toString() + "Size: "+response.length());
                            for(int i=0;i<response.length();i++){
                                final Posts post=new Posts();
                                try {
                                    Log.d(TAG,"Object at " + i+ response.get(i));
                                    JSONObject obj=response.getJSONObject(i);
                                    post.setId(obj.getInt("id"));
                                    post.setCreatedAt(obj.getString("date"));
                                    post.setPostURL(obj.getString("link"));
                                    JSONObject titleObj=obj.getJSONObject("title");
                                    post.setTitle(titleObj.getString("rendered"));
                                    //Get excerpt
                                    JSONObject exerptObj=obj.getJSONObject("excerpt");
                                    post.setExcerpt(exerptObj.getString("rendered"));
                                    // Get content
                                    JSONObject contentObj=obj.getJSONObject("content");
                                    post.setContent(exerptObj.getString("rendered"));


                                    // getting URL of the Post fetured Image
                                    JSONObject featureImage=obj.getJSONObject("_links");
                                    JSONArray featureImageUrl=featureImage.getJSONArray("wp:featuredmedia");
                                    JSONObject featureImageObj=featureImageUrl.getJSONObject(0);
                                    String fiurl=featureImageObj.getString("href");
                                    if(fiurl!=null) {
                                        //  post.setPostImg(fiurl);
                                        Log.d(TAG, featureImageObj.getString("href"));

                                        JsonObjectRequest getMedia = new JsonObjectRequest(Request.Method.GET,
                                                fiurl, null,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            // JSONObject obj=response.getJSONObject(0);
                                                            Log.d(TAG, response.getString("source_url"));
                                                            post.setPostImg(response.getString("source_url"));
                                                            index=mPosts.indexOf(post);
                                                            postValue(mPosts);

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                       //  post.setPostImg();


                                                    }


                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d(TAG, error.toString());
                                            }
                                        }

                                        );
                                        requestQueue.add(getMedia);

                                    }

                                    mPosts.add(post);



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            setValue(mPosts);
                            refresh.postValue(1);

                        }

                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, error.toString());
                        }
                    }
            ) ;

            requestQueue.add(getRequest);

        }
    }



}
