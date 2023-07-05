package com.example.namedropper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private DatabaseHelper myDb;  // renamed dbHelper to myDb
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.friends_list);
        myDb = new DatabaseHelper(this);  // renamed dbHelper to myDb
        db = myDb.getReadableDatabase();

        List<Friend> friendList = myDb.getAllFriends(db);  // renamed dbHelper to myDb
        friendAdapter = new FriendAdapter(friendList, this);

        recyclerView.setAdapter(friendAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button addFriendButton = findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                intent.putExtra("isEditMode", false); // we are adding a new friend, not editing an existing one
                startActivity(intent);
            }
        });
    }

    public void editFriend(Friend friend) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        intent.putExtra("isEditMode", true);
        intent.putExtra("id", friend.getId());
        intent.putExtra("name", friend.getName());
        intent.putExtra("phone", friend.getPhoneNumber());
        intent.putExtra("frequency", friend.getFrequency());
        startActivity(intent);
    }

    public void removeFriend(Friend friend) {
        db.delete(DatabaseHelper.TABLE_FRIENDS, DatabaseHelper.COL_1 + " = ?", new String[]{Integer.toString(friend.getId())});
        friendAdapter.setFriendList(myDb.getAllFriends(db));  // renamed dbHelper to myDb
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFriendsFromDatabase();
    }

    private void loadFriendsFromDatabase() {
        try {
            List<Friend> friendList = myDb.getAllFriends(db);  // renamed dbHelper to myDb
            friendAdapter.setFriendList(friendList);
            friendAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


///TODO:Change remove button to message now button
///TODO:Change edit button to have a remove button on the page