package com.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Trailer implements Serializable{
  private String id;
  private String key;
  private String name;
  private String site;
  private String type;

  private Trailer(JSONObject jsonObject) throws JSONException {
    this.id = jsonObject.getString("id");
    this.key = jsonObject.getString("key");
    this.name = jsonObject.getString("name");
    this.site = jsonObject.getString("site");
    this.type = jsonObject.getString("type");
  }

  public static ArrayList<Trailer> fromJSONArray(JSONArray jsonArray) {
    ArrayList<Trailer> trailerList = new ArrayList<>();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
        Trailer trailer = new Trailer(jsonArray.getJSONObject(i));
        if (trailer.getSite().compareToIgnoreCase("YouTube") == 0) {
          // Just grab the first 'YouTube' trailer
          trailerList.add(trailer);
          return trailerList;
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return trailerList;
  }

  public String getId() {
    return id;
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public String getSite() {
    return site;
  }

  public String getType() {
    return type;
  }
}
