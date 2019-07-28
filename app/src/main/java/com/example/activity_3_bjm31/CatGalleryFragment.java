package com.example.activity_3_bjm31;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activity_3_bjm31.models.GalleryItem;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatGalleryFragment extends Fragment {

    private static final String TAG = "CatGalleryFragment";

    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>>
    {
        @Override
        protected List<GalleryItem> doInBackground(Void... params)
        {
            return new CatFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items)
        {
            mItems = items;
            setupAdapter();
        }

    }

    private class CatHolder extends RecyclerView.ViewHolder {

        private ImageView mItemImageView;

        public CatHolder(View itemView)
        {
            super(itemView);

            mItemImageView = (ImageView) itemView;
        }

        public void bindDrawable(Drawable drawable)
        {
            mItemImageView.setImageDrawable(drawable);
        }
    }

    private class CatAdapter extends RecyclerView.Adapter<CatHolder> {
        private List<GalleryItem> mGalleryItems;

        public CatAdapter(List<GalleryItem> galleryItems)
        {
            mGalleryItems = galleryItems;
        }

        @Override
        public CatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           LayoutInflater inflater = LayoutInflater.from(getActivity());
           View view = inflater.inflate(R.layout.list_item_gallery, parent, false);
           return new CatHolder(view);
        }

        @Override
        public void onBindViewHolder(CatHolder catHolder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            Drawable placeholder = getResources().getDrawable(R.drawable.missing_image);
            catHolder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(catHolder, galleryItem.getUrl());
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    private RecyclerView mCatRecyclerView;

    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<CatHolder> mThumbnailDownloader;

    public static CatGalleryFragment newInstance() {
        return new CatGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<CatHolder>() {
                    @Override
                    public void onThumbnailDownloaded(CatHolder target, Bitmap thumbnail) {
                        Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                        target.bindDrawable(drawable);
                    }
                }
        );
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cat_gallery, container, false);
        mCatRecyclerView = (RecyclerView) v.findViewById(R.id.cat_recycler_view);
        mCatRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        setupAdapter();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    private void setupAdapter() {
        if(isAdded())
        {
            mCatRecyclerView.setAdapter(new CatAdapter(mItems));
        }

    }
}
