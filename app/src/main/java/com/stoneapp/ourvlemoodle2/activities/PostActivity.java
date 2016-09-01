/*
 * Copyright 2016 Matthew Stone and Romario Maxwell.
 *
 * This file is part of OurVLE.
 *
 * OurVLE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OurVLE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OurVLE.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.stoneapp.ourvlemoodle2.activities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


import com.activeandroid.query.Select;
import com.stoneapp.ourvlemoodle2.adapters.PostListAdapter;
import com.stoneapp.ourvlemoodle2.models.MoodlePost;
import com.stoneapp.ourvlemoodle2.tasks.PostSync;
import com.stoneapp.ourvlemoodle2.R;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("FieldCanBeLocal")
public class PostActivity  extends AppCompatActivity
        implements OnRefreshListener {
    private String discussionid;
    private List<MoodlePost> posts;
    private PostListAdapter padapter;
    private String discussionname;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        discussionid = extras.getString("discussionid");
        discussionname = extras.getString("discussionname");
        token = extras.getString("token");

        setContentView(R.layout.activity_post);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        txt_notpresent = (TextView) findViewById(R.id.txt_notpresent);
        img_notpresent = (ImageView) findViewById(R.id.no_posts);
        postList = (RecyclerView)findViewById(R.id.postList);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        progressBar = (ProgressBar)findViewById(R.id.progressBarPost);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(discussionname);
        }

        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // get posts from database
        getPostsFromDatabase();

        //Order posts by time created
        Collections.sort(posts, new Comparator<MoodlePost>() {
            @Override
            public int compare(MoodlePost lhs, MoodlePost rhs) {
                if (lhs.getCreated() > rhs.getCreated())
                    return 1;
                else
                    return -1;
            }
        });

        //if there are no posts
        if (posts.size() > 0) {
            txt_notpresent.setVisibility(View.GONE);
            img_notpresent.setVisibility(View.GONE);
        }

        postList.setLayoutManager(new LinearLayoutManager(this));
        padapter = new PostListAdapter(this, posts);
        postList.setAdapter(padapter);

        new LoadPostsTask(discussionid, token, this).execute(""); // refresh posts
    }

    private void getPostsFromDatabase()
    {
        posts = new Select().from(MoodlePost.class).where("discussionid = ?",discussionid).execute();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class LoadPostsTask extends AsyncTask<String, Integer, Boolean> {
        PostSync psync;
        String discussionid;
        String token;
        Context context;

        public LoadPostsTask(String discussionid, String token, Context context) {
            this.discussionid = discussionid;
            this.token = token;
            this.context = context;
            psync = new PostSync(token);
        }

        @Override
        protected void onPreExecute() {
            txt_notpresent.setVisibility(View.GONE);
            img_notpresent.setVisibility(View.GONE);

            if (posts.size() == 0)
                progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean sync = psync.syncPosts(discussionid); // syncs posts
            if (sync) {
                getPostsFromDatabase(); //gets the posts from database

                //Order posts by time created
                Collections.sort(posts,new Comparator<MoodlePost>() {
                    @Override
                    public int compare(MoodlePost lhs, MoodlePost rhs) {
                        if (lhs.getCreated()>rhs.getCreated())
                            return 1;
                        else
                            return -1;
                    }
                });
            }

            return sync;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            padapter.updatePosts(posts);
            mSwipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if (result) {
            } else {
                if (posts.size() == 0) {
                    txt_notpresent.setVisibility(View.VISIBLE);
                    img_notpresent.setVisibility(View.VISIBLE);
                } else
                    Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onRefresh() {
        new LoadPostsTask(discussionid, token, this).execute(""); // refresh posts
    }

    private Toolbar toolbar;
    private TextView txt_notpresent;
    private ImageView img_notpresent;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private RecyclerView postList;
}
