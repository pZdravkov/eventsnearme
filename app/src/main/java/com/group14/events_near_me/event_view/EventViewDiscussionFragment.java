package com.group14.events_near_me.event_view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.group14.events_near_me.Comment;
import com.group14.events_near_me.EventsApplication;
import com.group14.events_near_me.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Ben on 05/03/2018.
 *
 * this fragment displays a list of comments for the given event ID
 * also giving the user the ability to add a new comment
 */

public class EventViewDiscussionFragment extends ListFragment implements ChildEventListener {
    private ArrayList<Comment> comments = new ArrayList<>();
    String eventID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new CommentListAdapter(getContext(), R.layout.event_discussion_list_line, comments));
        // get the event ID from the activity
        eventID = ((EventViewActivity)getActivity()).getEventID();
        // set a listener for comments with our event ID
        ((EventsApplication)getActivity().getApplication()).getFirebaseController()
                .getRoot().child("comments").orderByChild("eventID")
                .equalTo(eventID).addChildEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_view_discussion, null);

        // set up the floating action button, which allows the user to add a new comment
        view.findViewById(R.id.discussionAddFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText commentInput = new EditText(getContext());

                // create an alert prompting the user to enter a comment
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Enter comment:");
                alert.setView(commentInput);
                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // if they click confirm get what they entered and send it to submitComment()
                        submitComment(commentInput.getText().toString());
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // if they clicked cancel don't submit a comment; do nothing
                    }
                });
                alert.show();
            }
        });
        return view;
    }

    private void submitComment(String text) {
        // get a reference to the comments
        DatabaseReference ref = ((EventsApplication)getActivity().getApplication()).getFirebaseController()
                .getRoot().child("comments");
        // generate a new comment
        String key = ref.push().getKey();

        String userID = ((EventsApplication)getActivity().getApplication()).getFirebaseController().getCurrentUserId();

        // generate timestamp of current time. getInstance will give a calendar of current time
        Calendar calendar = Calendar.getInstance();
        long timestamp = calendar.getTimeInMillis();

        // create and set the contents of the comment
        Comment comment = new Comment(userID, eventID, text, timestamp);
        ref.child(key).setValue(comment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // remove listener
        ((EventsApplication)getActivity().getApplication())
                .getFirebaseController().getRoot().child("comments").orderByChild("eventID")
                .equalTo(eventID).removeEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("MyDebug", "EventViewDiscussionFragment: onChildAdded:" + dataSnapshot.getKey());

        // add new comment to list of comments then update listView
        Comment comment = dataSnapshot.getValue(Comment.class);
        comments.add(comment);
        getListView().invalidateViews();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("MyDebug", "EventViewDiscussionFragment: onChildChanged:" + dataSnapshot.getKey());
        // TODO find previous child and update it
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.d("MyDebug", "EventViewDiscussionFragment: onChildRemoved:" + dataSnapshot.getKey());

        Comment comment = dataSnapshot.getValue(Comment.class);
        // find that comment in the list and remove it
        for (int x = 0; x < comments.size(); x++) {
            if (comments.get(x).equals(comment)) {
                comments.remove(x);
            }
        }
        getListView().invalidateViews();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("MyDebug", "EventViewDiscussionFragment: onChildMoved:" + dataSnapshot.getKey());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w("MyDebug", "EventViewDiscussionFragment: onCancelled", databaseError.toException());
    }
}
