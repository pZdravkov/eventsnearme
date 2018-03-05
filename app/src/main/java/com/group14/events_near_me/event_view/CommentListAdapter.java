package com.group14.events_near_me.event_view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.group14.events_near_me.Comment;
import com.group14.events_near_me.Event;
import com.group14.events_near_me.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Ben on 04/03/2018.
 */

public class CommentListAdapter extends ArrayAdapter<Comment> {
    private Context context;
    private ArrayList<Comment> comments;
    private int layoutResourceId;

    public CommentListAdapter(Context context, int layoutResourceId, ArrayList<Comment> comments) {
        super(context, layoutResourceId, comments);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.comments = comments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View row;
        if (convertView != null) {
            row = convertView;
        } else {
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        Comment comment = comments.get(position);


        ((TextView)row.findViewById(R.id.discussionName)).setText(comment.userID);
        ((TextView)row.findViewById(R.id.discussionText)).setText(comment.body);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.UK);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(comment.timestamp);
        ((TextView)row.findViewById(R.id.discussionTimestamp)).setText(sdf.format(calendar.getTime()));

        return row;
    }
}