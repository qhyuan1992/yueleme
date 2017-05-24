package com.mini.yueleme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mini.yueleme.R;
import com.mini.yueleme.data.HotDateItem;
import com.mini.yueleme.data.NewDateItem;
import com.mini.yueleme.utils.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 首页所有约单信息的适配器
 * Created by weiersyuan on 2016/7/25.
 */
public class DateItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<? extends DataSupport> dateItems;

    public  DateItemAdapter(Context context, List<? extends DataSupport> dateItems) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.dateItems = dateItems;
    }

    /**
     * 全局更新
     * @param dateItems
     */
    public void updateAll(List<? extends DataSupport> dateItems){
        this.dateItems = dateItems;
        notifyDataSetChanged();
    }

    /**
     * 局部更新
     * @param position
     */
    public void updateLocal(int position){
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return dateItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View partyItemView = layoutInflater.inflate(R.layout.item_homepage_2, null);
        ViewHolder viewHolder = new ViewHolder(partyItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            DataSupport supportItem = dateItems.get(position);
            if (onItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        onItemClickLitener.onItemClick(pos);
                    }
                });
            }
            if (supportItem instanceof NewDateItem) {
                NewDateItem newItem = (NewDateItem) supportItem;
                setNewDateItem(holder, newItem);
            } else if (supportItem instanceof HotDateItem) {
                HotDateItem hotItem = (HotDateItem) supportItem;
                setHotDateItem(holder, hotItem);
            }
        }
    }

    private void setNewDateItem(RecyclerView.ViewHolder holder, NewDateItem newItem) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ImageLoader.getInstance().displayImage(newItem.getImage_url(),viewHolder.userImage);
        viewHolder.userName.setText(newItem.getUser_name());
        viewHolder.subject.setText(newItem.getSubject());
        viewHolder.dateDescription.setText(newItem.getDescription() + "...");
        viewHolder.payMethon.setText(fromPay(newItem.getPay()));
        viewHolder.department.setText(newItem.getGroup_name());
        viewHolder.agree.setText(newItem.getDate_join_num() + "");
        viewHolder.remark.setText(newItem.getRemark_num() + "");
        viewHolder.datePosition.setText(newItem.getDate_location());
        viewHolder.dateTime.setText(TimeUtil.long2DateString(newItem.getDate_time()));
        viewHolder.createTime.setText(/*"发布于  " */"" +  TimeUtil.getTimeAgo(newItem.getCreate_time()));
        viewHolder.sexNeed.setText(formSexNeed(newItem.getDate_target().getBoy(), newItem.getDate_target().getGirl()));
    }

    private void setHotDateItem(RecyclerView.ViewHolder holder, HotDateItem hotItem) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ImageLoader.getInstance().displayImage(hotItem.getImage_url(),viewHolder.userImage);
        viewHolder.userName.setText(hotItem.getUser_name());
        viewHolder.subject.setText(hotItem.getSubject());
        viewHolder.dateDescription.setText(hotItem.getDescription() + "...");
        viewHolder.payMethon.setText(fromPay(hotItem.getPay()));
        viewHolder.department.setText(hotItem.getGroup_name());
        viewHolder.agree.setText(hotItem.getDate_join_num() + "");
        viewHolder.remark.setText(hotItem.getRemark_num() + "");
        viewHolder.datePosition.setText(hotItem.getDate_location());
        viewHolder.dateTime.setText(TimeUtil.long2DateString(hotItem.getDate_time()));
        viewHolder.createTime.setText("发布于  " + TimeUtil.getTimeAgo(hotItem.getCreate_time()));
        viewHolder.sexNeed.setText(formSexNeed(hotItem.getDate_target().getBoy(), hotItem.getDate_target().getGirl()));
    }

    public static String formSexNeed(int boy, int girl) {
        return "男" + boy + "女" + girl;
    }

    public static String fromPay(int type) {
        String result = "";
        switch (type) {
            case 0:
                result = "我请客";
                break;
            case 1:
                result = "AA";
                break;
            case 2:
                result = "求请客";
                break;
        }
        return result;
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

    // RecycleView没有setOnItemClickListener接口，自定义回调接口
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.onItemClickLitener = mOnItemClickLitener;
    }

    private OnItemClickLitener onItemClickLitener;//点击 RecyclerView 中的 Item

    public interface OnItemClickLitener {
        void onItemClick(int position);
    }
}
