package com.mini.yueleme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mini.yueleme.R;
import com.mini.yueleme.data.DateItem;
import com.mini.yueleme.data.MessageItem;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by weiersyuan on 16/7/28.
 */
public class MessageCenterAdapter extends RecyclerView.Adapter<MessageCenterAdapter.MessageCenterHolder> {

    private List<MessageItem> messageList;

    public MessageCenterAdapter(List<MessageItem> messageList) {
        this.messageList = messageList;
    }

    public void updateAll(List<MessageItem> messageList){
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @Override
    public MessageCenterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null);
        return new MessageCenterHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageCenterHolder holder, int position) {
        MessageItem msgItem = messageList.get(position);
        holder.msgView.setText(msgItem.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MessageCenterHolder extends RecyclerView.ViewHolder {

        private TextView msgView;
        public MessageCenterHolder(View itemView) {
            super(itemView);
            msgView = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }
}
