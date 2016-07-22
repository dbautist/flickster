package com.codepath.flickster.models;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONSerializable {
  void configureFromJSON(JSONObject jsonObject) throws JSONException;
}