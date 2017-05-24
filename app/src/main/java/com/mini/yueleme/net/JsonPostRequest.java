package com.mini.yueleme.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by weiersyuan on 2016/7/24.
 */
public class JsonPostRequest extends JsonObjectRequest {

    private HashMap params;
    public JsonPostRequest(String url, HashMap<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, null, listener, errorListener);
        this.params = params;
    }

    public JsonPostRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, null, listener, errorListener);
        params = new HashMap<String, String>();
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.params;
    }

}
