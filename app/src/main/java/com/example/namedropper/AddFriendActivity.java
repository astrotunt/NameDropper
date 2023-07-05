package com.example.namedropper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.example.namedropper.R;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import java.util.Calendar;
public class AddFriendActivity extends AppCompatActivity {

    private EditText friendNameInput;
    private EditText friendPhoneInput;
    private Button saveFriendButton;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase myDb;
    private EditText frequencyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        friendNameInput = findViewById(R.id.friend_name_input);
        friendPhoneInput = findViewById(R.id.friend_phone_input);
        frequencyEditText = findViewById(R.id.frequencyEditText);
        saveFriendButton = findViewById(R.id.saveFriendButton);
        // Initialize DatabaseHelper and SQLiteDatabase
        dbHelper = new DatabaseHelper(this);
        myDb = dbHelper.getWritableDatabase();
        // get the frequency EditText
        frequencyEditText = findViewById(R.id.frequencyEditText);

        Button saveButton = findViewById(R.id.saveFriendButton);

        // Check if in "edit mode" or "add mode"
        Intent intent = getIntent();
        boolean isEditMode = intent.getBooleanExtra("isEditMode", false);
        if (isEditMode) {
            // Pre-fill the EditTexts with the friend's current details
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            int frequency = intent.getIntExtra("frequency", 6);

            friendNameInput.setText(name);
            friendPhoneInput.setText(phone);
            frequencyEditText.setText(String.valueOf(frequency));

            // Change the save button text to "Update"
            saveButton.setText("Update");
        } else {
            // Default frequency to 6
            frequencyEditText.setText("6");

            // Change the save button text to "Save"
            saveButton.setText("Save");
        }
        saveFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int frequency = Integer.parseInt(frequencyEditText.getText().toString());
                String friendName = friendNameInput.getText().toString();
                String friendPhone = friendPhoneInput.getText().toString();
                // check if we have the permission to send SMS
                if (ContextCompat.checkSelfPermission(AddFriendActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    // if not, request it
                    ActivityCompat.requestPermissions(AddFriendActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                }
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COL_2, friendName);
                values.put(DatabaseHelper.COL_3, friendPhone);
                values.put(DatabaseHelper.COL_4, frequency);

                myDb.insert(DatabaseHelper.TABLE_FRIENDS, null, values);
                // get the id of the new row
                Cursor cursor = myDb.rawQuery("SELECT last_insert_rowid()", null);
                cursor.moveToFirst();
                int friendId = cursor.getInt(0);
                cursor.close();

                // schedule the automatic text message
                Intent intent = new Intent(AddFriendActivity.this, SmsSender.class);
                intent.putExtra("phone", friendPhone);
                intent.putExtra("message", friendName + "."); // replace with your own message
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddFriendActivity.this, friendId, intent, 0);

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, frequency);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                finish();
            }
        });

    }

}
