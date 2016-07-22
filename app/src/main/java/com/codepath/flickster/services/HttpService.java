package com.codepath.flickster.services;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HttpService {
  private static final String TAG = HttpService.class.getSimpleName();

  public interface HttpResponseListner {
    void onRequestFinished(HttpResponse response);
  }

  private HttpResponseListner listener;

  public HttpService(HttpResponseListner listener) {
    this.listener = listener;
  }

  public void getResponse(String url) {
    AsyncHttpClient client = new AsyncHttpClient();
    client.get(url, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        if (response == null) {
          fireFailureResponse("Response is NULL");
        } else {
          fireSuccessResponse(response.toString());
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        fireFailureResponse(responseString);
      }
    });
  }

  private void fireSuccessResponse(String responseStr) {
    Log.d(TAG, "fireSuccessResponse: " + responseStr);
    if (listener != null) {
      HttpResponse response = new HttpResponse();
      response.setStatus(HttpResponse.Status.Success);
      response.setResponse(responseStr);
      listener.onRequestFinished(response);
    }
  }

  private void fireFailureResponse(String failureStr) {
    Log.d(TAG, "fireFailureResponse: " + failureStr);
    if (listener != null) {
      HttpResponse response = new HttpResponse();
      response.setStatus(HttpResponse.Status.Failed);
      response.setFailureMessage(failureStr);
      listener.onRequestFinished(response);
    }
  }
}
