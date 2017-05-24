package com.mini.yueleme.utils;

import com.mini.yueleme.data.HotDateItem;
import com.mini.yueleme.data.MessageItem;
import com.mini.yueleme.data.NewDateItem;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by weiersyuan on 2016/7/25.
 * 缓存约会信息/关注的用户信息/个人用户信息
 */
public class DataBaseUtil {

    // 从数据库获取缓存的约会信息
    public static List<? extends DataSupport> queryAllDateItems(int type) {
        if (type == 0) {
            return DataSupport.findAll(NewDateItem.class,true);
        } else if (type == 1) {
            return DataSupport.findAll(HotDateItem.class,true);
        } else{
            return DataSupport.findAll(NewDateItem.class,true);
        }
    }

    // 缓存约会信息
    public static void cacheDateItems(List<? extends DataSupport> dateItems) {
        if (dateItems.size() <= 0) {
            return;
        }
        // 删除已有的内容
        Class<?> clazz= dateItems.get(0).getClass();
        DataSupport.deleteAll(clazz);
        // 缓存新的
        // 先存储DateTarget

        for (DataSupport dataSupport : dateItems) {
            if (dataSupport instanceof NewDateItem) {
                ((NewDateItem)dataSupport).getDate_target().save();
            } else  if (dataSupport instanceof HotDateItem) {
                ((HotDateItem)dataSupport).getDate_target().save();
            }
        }
        DataSupport.saveAll(dateItems);
    }

    // 获取消息中心在本地的缓存信息
    public static List<MessageItem> queryMessageItems() {
        return DataSupport.findAll(MessageItem.class);
    }

    // 缓存消息中心的消息
    public static void cacheMessageItems(List<MessageItem> msgItems) {
        DataSupport.deleteAll(MessageItem.class);
        DataSupport.saveAll(msgItems);
    }

}
