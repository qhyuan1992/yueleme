package com.mini.yueleme.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * 信鸽推送的广播接收者，用于处理收到通知或者消息
 */
public class MessageReceiver extends XGPushBaseReceiver {
	public static final String LogTag = "TPushReceiver";

	/**
	 * 用于存储推送通知
	 * @param context
	 * @param notifiShowedRlt
     */
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		// 存储消息到数据库
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
	}

	@Override
	public void onDeleteTagResult(Context context, int i, String s) {
	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {

	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// ommit
		}

	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
	}

	/*
	* 处理消息透传
	 */
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
