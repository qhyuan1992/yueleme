package com.mini.yueleme.fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mini.yueleme.R;
import com.mini.yueleme.YLMApplication;
import com.mini.yueleme.adapter.MessageCenterAdapter;
import com.mini.yueleme.data.MessageItem;
import com.mini.yueleme.utils.Constant;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment{
	private View messageView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private LinearLayoutManager layoutManager;
	private RecyclerView recycleView;
	private YLMApplication application;
	List<MessageItem> msgItems = new ArrayList<MessageItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		messageView = inflater.inflate(R.layout.message_center, null);
		initView(messageView);
		return messageView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		application = (YLMApplication) getActivity().getApplication();
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
				updateJoinMessage();
			}
		});
	}


	public void initView(View view) {
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.messageSwipeRefreshLayout);
		recycleView = (RecyclerView) view.findViewById(R.id.messageRecyclerView);
		layoutManager = new LinearLayoutManager(getActivity());
		recycleView.setLayoutManager(layoutManager);
		recycleView.setAdapter(adapter);
		swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
				android.R.color.holo_red_light, android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				updateJoinMessage();
			}
		});
	}
	private MessageCenterAdapter adapter = new MessageCenterAdapter(msgItems);

	private void updateJoinMessage() {
		// 获取参加你发起的约单的报名信息
		JsonObjectRequest getJoinMessageRequest = new JsonObjectRequest(Constant.GET_JOIN_MESSAGE + "?user_id=" + application.userID, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						swipeRefreshLayout.setRefreshing(false);
						updateMessageUI(jsonObject);
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				swipeRefreshLayout.setRefreshing(false);
				application.showToastMsg("服务器异常...");
			}
		}
		);
		application.requestQueue.add(getJoinMessageRequest);
	}

	// 根据从网络拉取的消息更新UI
	private void updateMessageUI(JSONObject jsonObject) {
		try {
			if (jsonObject.getInt(Constant.ERROR_CODE) == 0) {
				// 获取成功
				msgItems = MessageItem.fromJson2List(jsonObject.getJSONArray(Constant.DATA).toString());
				adapter.updateAll(msgItems);
			}
		} catch (JSONException E){
		}


	}
}
