package com.example.deliverable3_android_php_api.network;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static volatile VolleySingleton instance;
    private final RequestQueue requestQueue;

    // Private constructor to prevent instantiation
    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    // Thread-safe singleton with double-checked locking
    public static VolleySingleton getInstance(Context context) {
        if (instance == null) {
            synchronized (VolleySingleton.class) {
                if (instance == null) {
                    instance = new VolleySingleton(context);
                }
            }
        }
        return instance;
    }

    // Generic method to add request to queue
    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

    // Optional: expose the RequestQueue if needed
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
