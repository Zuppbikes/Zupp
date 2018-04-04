package com.zupp.component.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Hari Prasanth A C on 7/21/2015.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 3; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;


    private int mPage = 1;


    public EndlessRecyclerOnScrollListener() {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        onScroll();

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = recyclerView.getLayoutManager().getItemCount();
        firstVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            visibleThreshold = 3;
            // End has been reached

            // Do something
            mPage += 1;

            onLoadMore(mPage);

            loading = true;
        }
    }


    public void reset() {
        loading = true;
        mPage = 1;
        totalItemCount = 0;
        previousTotal = 0;
    }

    public void resetLoading() {
        if(mPage > 1) {
            mPage -= 1;
        }
        loading = false;
        visibleThreshold = 0;
    }

    public abstract void onLoadMore(int current_page);

    public abstract void onScroll();
}
