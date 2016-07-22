package com.codepath.flickster.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.codepath.flickster.models.Movie;

public class TestAdapter extends ArrayAdapter<Movie> {
  public TestAdapter(Context context, int resource) {
    super(context, resource);
  }
}
