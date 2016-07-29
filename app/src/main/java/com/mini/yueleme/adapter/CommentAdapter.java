package com.mini.yueleme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mini.yueleme.R;
import com.mini.yueleme.data.RemarkItem;

import java.util.List;

/**
 * Created by bumblebee on 16/7/28.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<RemarkItem> remarkItems;

    public CommentAdapter(List<RemarkItem> remarkItems) {
        this.remarkItems = remarkItems;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, null);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder commentHolder, int i) {
        commentHolder.txUsername.setText(remarkItems.get(i).getUser_name());
        commentHolder.txComment.setText(remarkItems.get(i).getContent());
    }



    @Override
    public int getItemCount() {
        return remarkItems.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {

        private TextView txUsername, txComment;

        public CommentHolder(View itemView) {
            super(itemView);

            txUsername = (TextView)itemView.findViewById(R.id.tx_username);
            txComment = (TextView)itemView.findViewById(R.id.tx_comment);
        }
    }
}
