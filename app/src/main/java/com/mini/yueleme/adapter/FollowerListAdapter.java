package com.mini.yueleme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bumblebee on 16/7/23.
 */
public class FollowerListAdapter extends RecyclerView.Adapter<FollowerListAdapter.FollowerListHolder> {

    @Override
    public FollowerListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FollowerListHolder holder, int position) {

    }

    /*等后台有数据后,会根据UserInfo中*/
    @Override
    public int getItemCount() {
        return 2;
    }

    class FollowerListHolder extends RecyclerView.ViewHolder {

        public FollowerListHolder(View itemView) {
            super(itemView);
        }
    }
}
