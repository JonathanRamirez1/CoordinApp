package com.jonathan.coordinapp.data.remote.datasource;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.jonathan.coordinapp.data.remote.model.ScanModel;
import com.jonathan.coordinapp.data.remote.network.NetworkConstants;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RemoteScanDataSource {

    private final RequestQueue queue;
    private final Gson gson;

    @Inject
    public RemoteScanDataSource(RequestQueue q, Gson g) {
        this.queue = q;
        this.gson = g;
    }

    public Single<ScanModel> validateBase64(String base64) {
        return Single.<ScanModel>create(emitter -> {

            JSONObject body = new JSONObject().put("data", base64);

            Log.d("VOLLEY-REQ", "POST " + NetworkConstants.VALIDATE_URL +
                    " body=" + body);

            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.POST,
                    NetworkConstants.VALIDATE_URL,
                    body,
                    res -> {
                        Log.d("VOLLEY-RES", res.toString());
                        emitter.onSuccess(gson.fromJson(res.toString(), ScanModel.class));
                    },
                    err -> {
                        Log.e("VOLLEY-ERR", "Error", err);
                        emitter.onError(err);
                    }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            queue.add(req);
        }).subscribeOn(Schedulers.io());
    }
}
