package com.jonathan.coordinapp.data.remote.datasource;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.jonathan.coordinapp.data.remote.model.ScanModel;
import com.jonathan.coordinapp.data.remote.network.NetworkConstants;
import com.jonathan.coordinapp.utils.NetworkException;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.inject.Singleton;

@Singleton
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
            JSONObject body;
            try {
                body = new JSONObject().put("data", base64);
            } catch (JSONException je) {
                emitter.onError(new NetworkException(je));
                return;
            }

            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.POST,
                    NetworkConstants.VALIDATE_URL,
                    body,
                    res -> {
                        try {
                            emitter.onSuccess(gson.fromJson(res.toString(), ScanModel.class));
                        } catch (JsonParseException jpe) {
                            emitter.onError(new NetworkException(jpe));
                        }
                    },
                    err -> emitter.onError(new NetworkException(err))
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
