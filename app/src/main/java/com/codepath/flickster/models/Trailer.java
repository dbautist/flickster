package com.codepath.flickster.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Trailer implements JSONSerializable, Serializable {
  private String id;
  private String key;
  private String name;
  private String site;
  private String type;

  @Override
  public void configureFromJSON(JSONObject jsonObject) throws JSONException {
    this.id = jsonObject.getString("id");
    this.key = jsonObject.getString("key");
    this.name = jsonObject.getString("name");
    this.site = jsonObject.getString("site");
    this.type = jsonObject.getString("type");
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
