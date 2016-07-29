package com.mini.yueleme.fragment;

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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mini.yueleme.R;
import com.mini.yueleme.YLMApplication;
import com.mini.yueleme.adapter.MyAppointmentAdapter;
import com.mini.yueleme.data.DateItem;
import com.mini.yueleme.data.NewDateItem;
import com.mini.yueleme.net.NetworkUtil;
import com.mini.yueleme.utils.Constant;
import com.mini.yueleme.widget.SegmentedControlView;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class DatingItemFragment extends Fragment {
    private static final String TAG = "DatingItemFragment";
    public int page = 0;

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private RecyclerView recycleView;
    private SegmentedControlView segmentedControlView;
    private RequestQueue mRequestQueue;
    private Gson mGson;
    private DateItem item;
    private MyAppointmentAdapter adapter;

    /*约单列表*/
    private List<NewDateItem> dateItemList;

    private YLMApplication application;

    private String requestUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointment, null);
        initView(view);
        dateItemList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        mGson = new Gson();
        return view;
    }

    public void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutId);
        recycleView = (RecyclerView) view.findViewById(R.id.myAppointmentList);
        segmentedControlView = (SegmentedControlView)view.findViewById(R.id.segment_control_my_appointment);
        layoutManager = new LinearLayoutManager(getActivity());

        segmentedControlView.setOnSelectionChangedListener(new SegmentedControlView.OnSelectionChangedListener() {
            @Override
            public void newSelection(String identifier, String value) {
                if (value.contains("发布")) {
                    requestUrl = Constant.GET_ME_PUBLISHED + application.userID;
                } else {
                    requestUrl = Constant.GET_ME_JOINED + application.userID;
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });
                updatePublishedDate(requestUrl);
            }
        });
        recycleView.setLayoutManager(layoutManager);
		/*下拉刷新*/
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!application.checkNetWork()) {
                    return;
                }
                updatePublishedDate(Constant.GET_ME_PUBLISHED + application.userID);
            }
        });
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        application = (YLMApplication)getActivity().getApplication();
        requestUrl = Constant.GET_ME_PUBLISHED + application.userID;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                updatePublishedDate(requestUrl);
            }
        });
    }

    private void updatePublishedDate(String url){

        StringRequest mStringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(s);
                            dateItemList = NewDateItem.fromJson2List(jsonObject.getJSONArray("data").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        adapter = new MyAppointmentAdapter(dateItemList);
                        recycleView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage());
            }
        });
        mRequestQueue.add(mStringRequest);
    }
}