package com.akash.git.Utils;

import com.android.volley.VolleyError;

import org.json.JSONArray;

/**
 * Created by Akash Srivastava on 28-04-2017.
 */

public interface Responses {


    void onSuccessResponse(JSONArray response);

    void onSuccessResponse(String response);

    void onErrorResponse(VolleyError error);
}
