package com.example.user.smures;

import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private String URL = UserInfo.getUrl()+"login.php";
    private Map<String, String> parameters;

    public LoginRequest(String uid, String passwd, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("ID", uid);
        parameters.put("PASS", passwd);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
