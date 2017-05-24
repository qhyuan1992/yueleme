package com.mini.yueleme.fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mini.yueleme.DateItemDetailActivity;
import com.mini.yueleme.MainActivity;
import com.mini.yueleme.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mini.yueleme.YLMApplication;
import com.mini.yueleme.adapter.DateItemAdapter;
import com.mini.yueleme.data.HotDateItem;
import com.mini.yueleme.data.NewDateItem;
import com.mini.yueleme.utils.Constant;
import com.mini.yueleme.utils.DataBaseUtil;
import com.mini.yueleme.widget.SegmentedControlView;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment implements DateItemAdapter.OnItemClickLitener{
	private static final String TAG = "HomeFragment";
	private YLMApplication application;
	private int segmentPosition = 0;
	private String requestUrl = Constant.GET_NEW_DATE_ITEMS_URL + "?op=0&page=" + 1;

	private View homeView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private LinearLayoutManager layoutManager;
	private RecyclerView recycleView;
	private SegmentedControlView segmentControl;
	private DateItemAdapter dateItemAdapter;

	// 分页加载
	private int page = 1;

	private List<? extends DataSupport> dateItems = new ArrayList<NewDateItem>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		homeView = inflater.inflate(R.layout.fragment_home, null);
		swipeRefreshLayout = (SwipeRefreshLayout) homeView.findViewById(R.id.homeSwipeRefreshLayout);
		recycleView = (RecyclerView) homeView.findViewById(R.id.homeRecyclerView);
		segmentControl = (SegmentedControlView) homeView.findViewById(R.id.segment_control);
		dateItemAdapter = new DateItemAdapter(getActivity(),dateItems);
		dateItemAdapter.setOnItemClickLitener(this);
		return homeView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		application = (YLMApplication) activity.getApplication();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 从数据库加在数据
		segmentControl.setOnSelectionChangedListener(new SegmentedControlView.OnSelectionChangedListener(){
			@Override
			public void newSelection(String identifier, String value) {
				if (value.contains("新")) {
					segmentPosition = 0;
					requestUrl = Constant.GET_NEW_DATE_ITEMS_URL + "?op=0&page=" + page;
					dateItems = DataBaseUtil.queryAllDateItems(0);
					sortByTimeNew((List<NewDateItem>) dateItems);
					dateItemAdapter.updateAll(dateItems);
				} else if (value.contains("热")) {
					segmentPosition = 1;
					requestUrl = Constant.GET_HOT_DATE_ITEMS_URL + "?op=1&page=" + page;
					dateItems = DataBaseUtil.queryAllDateItems(1);
					sortByTimeHot((List<HotDateItem>) dateItems);
					dateItemAdapter.updateAll(dateItems);
				}
			}
		});
		layoutManager = new LinearLayoutManager(getActivity());
		recycleView.setLayoutManager(layoutManager);
		swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
				android.R.color.holo_red_light, android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		swipeRefreshLayout.setOnRefreshListener(new RefreshListener());
		recycleView.setAdapter(dateItemAdapter);

		dateItems = DataBaseUtil.queryAllDateItems(0);
		dateItemAdapter.updateAll(dateItems);
		// 从数据库拉不到数据就从网络获取
		if (dateItems.size() <= 0) {
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					swipeRefreshLayout.setRefreshing(true);
					getDateItemsFromNet();
				}
			});
		}
	}

	// ITEM的点击事件
	@Override
	public void onItemClick(int position) {
		String dateID = "";
		if (segmentPosition == 0) {
			// 最新
			List<NewDateItem> newDateItems = (List<NewDateItem>)dateItems;
			dateID = newDateItems.get(position).getDate_id();
		} else if (segmentPosition == 1) {
			// 最热
			List<HotDateItem> hotDateItems = (List<HotDateItem>)dateItems;
			dateID = hotDateItems.get(position).getDate_id();
		}

		if (dateID != null && !dateID.equals("")) {
			// 跳转到详情页面
			Intent detailDateIntent = new Intent(getActivity(), DateItemDetailActivity.class);
			detailDateIntent.putExtra("date_id", dateID);
			startActivity(detailDateIntent);
		}
	}

	private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
		@Override
		public void onRefresh()  {
			getDateItemsFromNet();
		}
	}

	// 网络请求最新/最热的约会信息
	private void getDateItemsFromNet() {
		JsonObjectRequest getDateItemsPostRequest = new JsonObjectRequest(requestUrl, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						swipeRefreshLayout.setRefreshing(false);
						updateDateItemsWithNet(jsonObject);
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				swipeRefreshLayout.setRefreshing(false);
				application.showToastMsg("服务器异常...");
			}
		}
		);
		application.requestQueue.add(getDateItemsPostRequest);
	}

	// 通过网络获取新的条目更新界面和数据库
	private void updateDateItemsWithNet(JSONObject jsonObject) {
		try {
			if (jsonObject.getInt(Constant.ERROR_CODE) == Constant.ERROR_CODE_OK) {
				if (segmentPosition == 0) {
					dateItems = NewDateItem.fromJson2List(jsonObject.getJSONArray(Constant.DATA).toString());
					sortByTimeNew((List<NewDateItem>) dateItems);
					Log.i(TAG, dateItems.toString());
				} else if (segmentPosition == 1) {
					dateItems = HotDateItem.fromJson2List(jsonObject.getJSONArray(Constant.DATA).toString());
					sortByTimeHot((List<HotDateItem>) dateItems);
				}
				dateItemAdapter.updateAll(dateItems);
				// 写入数据库，扔到线程池处理
				application.executorService.execute(new Runnable() {
					@Override
					public void run() {
						if (dateItems.size() > 0) {
							// 写入数据库
							DataBaseUtil.cacheDateItems(dateItems);
						}
					}
				});
			}
		} catch (JSONException e) {
			Log.i(TAG, "JSON解析出错");
			e.printStackTrace();
		}
	}

	/**
	 * 按照创建时间对约单进行排序
	 * @param items
     */
	private void sortByTimeNew(List<NewDateItem> items) {
		Collections.sort(items, new Comparator<NewDateItem>() {
			@Override
			public int compare(NewDateItem lhs, NewDateItem rhs) {
				return (int)(rhs.getCreate_time() - lhs.getCreate_time());
			}
		});
	}

	private void sortByTimeHot(List<HotDateItem> items) {
		Collections.sort(items, new Comparator<HotDateItem>() {
			@Override
			public int compare(HotDateItem lhs, HotDateItem rhs) {
				return (int)(rhs.getCreate_time() - lhs.getCreate_time());
			}
		});
	}

}