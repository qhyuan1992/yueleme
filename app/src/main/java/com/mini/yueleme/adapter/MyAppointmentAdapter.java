package com.mini.yueleme.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mini.yueleme.R;
import com.mini.yueleme.data.DateItem;
import com.mini.yueleme.data.NewDateItem;
import com.mini.yueleme.utils.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bumblebee on 16/7/26.
 */
public class MyAppointmentAdapter extends RecyclerView.Adapter<MyAppointmentAdapter.ViewHolder> {

    private static final String TAG = "MyAppointmentAdapter";
    private List<NewDateItem> dateItemList;

    public MyAppointmentAdapter(List<NewDateItem> dateItemList) {
        this.dateItemList = dateItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homepage_2, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        NewDateItem item= dateItemList.get(position);
        ImageLoader.getInstance().displayImage(item.getImage_url(),viewHolder.userImage);
        viewHolder.userName.setText(item.getUser_name());
        viewHolder.subject.setText(item.getSubject());
        viewHolder.dateDescription.setText(item.getDescription());
        viewHolder.payMethon.setText(DateItemAdapter.fromPay(item.getPay()));
        viewHolder.department.setText(item.getGroup_name());
        viewHolder.agree.setText(item.getDate_join_num() + "");
        viewHolder.remark.setText(item.getRemark_num() + "");
        viewHolder.datePosition.setText(item.getDate_location());
        viewHolder.dateTime.setText(TimeUtil.long2DateString(item.getDate_time()));
        viewHolder.createTime.setText("发布于  " + TimeUtil.getTimeAgo(item.getCreate_time()));
        viewHolder.sexNeed.setText(DateItemAdapter.formSexNeed(item.getDate_target().getBoy(), item.getDate_target().getGirl()));
    }

    @Override
    public int getItemCount() {
        return dateItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView userImage;
        private final TextView userName;
        private final TextView subject;
        private final TextView datePosition;
        private final TextView dateTime;
        private final TextView dateDescription;
        private final TextView payMethon;
        private final TextView department;
        private final TextView agree;
        private final TextView remark;
        private final TextView createTime;
        private final TextView sexNeed;
        public ViewHolder(View itemView) {
            super(itemView);
            createTime = (TextView) itemView.findViewById(R.id.tv_date_createtime);
            userImage = (CircleImageView)itemView.findViewById(R.id.detail_civ_user_image);
            userName = (TextView) itemView.findViewById(R.id.detail_tv_user_name);
            subject = (TextView) itemView.findViewById(R.id.detail_tv_date_subject);
            datePosition = (TextView) itemView.findViewById(R.id.detail_tv_date_position);
            dateTime = (TextView) itemView.findViewById(R.id.tv_date_time);
            dateDescription = (TextView) itemView.findViewById(R.id.detail_tv_date_descripsion);
            payMethon = (TextView) itemView.findViewById(R.id.tv_pay_method);
            department = (TextView) itemView.findViewById(R.id.tv_department);
            agree = (TextView) itemView.findViewById(R.id.detail_tv_agree);
            remark = (TextView) itemView.findViewById(R.id.detail_tv_remark);
            sexNeed = (TextView)itemView.findViewById(R.id.tv_sexneed);
        }
    }
}
