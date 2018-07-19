package com.chikeandroid.tutsplus_glide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chike on 2/12/2017.
 */

public class SpaceGalleryActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private RequestQueue queue;
    private static final String TAG = "GalleryActivity";
    private EditText searchEt;
    private Button searchBtn;
    private LinearLayout searchArea;
    private TextView statusText;
    private boolean isWaitingForResponse;
    SpaceGalleryActivity.ImageGalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_gallery);

        queue = Volley.newRequestQueue(this);
        searchEt = (EditText) findViewById(R.id.search_et);
        searchBtn = (Button) findViewById(R.id.search_btn);
        searchArea = (LinearLayout) findViewById(R.id.search_area);
        statusText = (TextView) findViewById(R.id.status_text);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")) {
                    adapter.mSpacePhotos = SpacePhoto.getSpacePhotos();
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "New Dataset: " + adapter.mSpacePhotos.length);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterImages(searchEt.getText().toString());
            }
        });


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SpaceGalleryActivity.ImageGalleryAdapter(this, SpacePhoto.getSpacePhotos());
        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        updateStatusText();
        super.onResume();
    }

    private void updateStatusText() {
        if(SpacePhoto.getSpacePhotos()[0].getTags().size() == 0) {
            statusText.setText("Open Menu and Process Images before searching");
        } else {
            statusText.setText("Search Images by Content");
        }
    }

    private void filterImages(String searchString) {

        String[] tokens = searchString.split(",");
        if(searchString.equals("")) {
            adapter.mSpacePhotos = SpacePhoto.getSpacePhotos();
            adapter.notifyDataSetChanged();
            return;
        }

        SpacePhoto[] photos = SpacePhoto.getSpacePhotos();
        List<SpacePhoto> filteredPhotos = new ArrayList<>();
        for (int i=0; i<photos.length-1; i++) {
            List<String> tags = photos[i].getTags();
            for(String tag: tags) {
                for(int j=0; j<tokens.length; j++) {
                    if (tag.contains(tokens[j])) {
                        filteredPhotos.add(photos[i]);
                    }
                }

            }
        }
        SpacePhoto[] latestPhotos = new SpacePhoto[filteredPhotos.size()];
        for(int i=0; i<filteredPhotos.size(); i++) {
            latestPhotos[i] = filteredPhotos.get(i);
        }

        Log.d(TAG, "New Dataset: " + searchString + "= "+ latestPhotos.length);
        adapter.mSpacePhotos = latestPhotos;
        adapter.notifyDataSetChanged();
    }

    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the layout
            View photoView = inflater.inflate(R.layout.item_photo, parent, false);

            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) photoView.getLayoutParams();
            lp.height = parent.getMeasuredHeight() / 4;
            photoView.setLayoutParams(lp);

            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

            SpacePhoto spacePhoto = mSpacePhotos[position];
            ImageView imageView = holder.mPhotoImageView;

            Glide.with(mContext)
                    .load(spacePhoto.getUrl())
                    .placeholder(R.drawable.ic_cloud_off_red)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mSpacePhotos.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    SpacePhoto spacePhoto = mSpacePhotos[position];

                    Intent intent = new Intent(mContext, SpacePhotoActivity.class);
                    intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    startActivity(intent);
                }
            }
        }

        private SpacePhoto[] mSpacePhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context, SpacePhoto[] spacePhotos) {
            mContext = context;
            mSpacePhotos = spacePhotos;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.process:
                isWaitingForResponse = true;
                makeRequest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void makeRequest() {
        String url = "http://ec2-13-57-248-216.us-west-1.compute.amazonaws.com:5000/todo/api/v1/results";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        showLoading(false);
                        onSuccess();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        showLoading(false);
                        onFailure();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("url", getImageUrlString());
                return params;
            }

        };

        queue.add(putRequest);
        Log.d(TAG, putRequest.toString());
        showLoading(true);
    }


    private String getImageUrlString() {
        SpacePhoto[] photos = SpacePhoto.getSpacePhotos();
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<photos.length-1; i++) {
            builder.append(photos[i].getUrl());
            builder.append(",");
        }
        builder.append(photos[photos.length-1].getUrl());
        return builder.toString();
    }


    private void showLoading(boolean isShow) {

        if(dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setMessage("Processing Images...");
        }

        if(!isShow && dialog.isShowing()) {
            dialog.cancel();
            return;
        }

        if(!dialog.isShowing() && isShow) {
            dialog.show();
            return;
        }

    }



    private void onSuccess() {

        Log.d(TAG, "Tags set:");
        SpacePhoto[] originalPhotos = SpacePhoto.getSpacePhotos();
        for(int i=0; i<originalPhotos.length; i++) {
            SpacePhoto photo = originalPhotos[i];
            List<String> tags = new ArrayList<>();
            tags.add(photo.getTitle());
            photo.setTags(tags);
        }
        updateStatusText();
        isWaitingForResponse = false;

    }


    private void onFailure() {

        Log.d(TAG, "Tags set:");
        SpacePhoto[] originalPhotos = SpacePhoto.getSpacePhotos();
        for(int i=0; i<originalPhotos.length; i++) {
            SpacePhoto photo = originalPhotos[i];
            List<String> tags = new ArrayList<>();
            tags.add(photo.getTitle());
            photo.setTags(tags);
        }
        updateStatusText();
        isWaitingForResponse = false;
    }
}
