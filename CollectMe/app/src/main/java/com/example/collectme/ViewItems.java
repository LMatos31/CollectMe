package com.example.collectme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//References: Tutlane.com. 2021. Android ListView with Examples - Tutlane. [online] Available at: <https://www.tutlane.com/tutorial/android/android-listview-with-examples> [Accessed 26 May 2021].
//References: Stack Overflow. 2021. take a picture and save it using android studio. [online] Available at: <https://stackoverflow.com/questions/40117332/take-a-picture-and-save-it-using-android-studio> [Accessed 26 May 2021].
//References: Stack Overflow. 2021. How to save user input from EditText to a variable to be used on a different Activity. [online] Available at: <https://stackoverflow.com/questions/31090558/how-to-save-user-input-from-edittext-to-a-variable-to-be-used-on-a-different-act> [Accessed 26 May 2021].
//References: Professor DK, 2021. Simple Login App Tutorial Using Android Studio 2.3.3 (NEW). [video] Available at: <https://www.youtube.com/watch?v=lF5m4o_CuNg> [Accessed 26 May 2021].
//References: 2021. CodeWithMazn. [video] Available at: <https://www.youtube.com/c/CodeWithMazn/videos> [Accessed 3 July 2021].
//References: 2021. Educatree. [video] Available at: <https://www.youtube.com/watch?v=iy6WexahCdY> [Accessed 3 July 2021].
//References: Studio, R., Quinter, J., Mamo, A., Berenguer, J. and yash, t., 2021. Retrieve Firebase data to ListView in Android Studio. [online] Stack Overflow. Available at: <https://stackoverflow.com/questions/52167162/retrieve-firebase-data-to-listview-in-android-studio> [Accessed 3 July 2021].

public class ViewItems extends AppCompatActivity {
    private Button display;
    private ListView ltItems;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        ltItems.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Items");

        ltItems = (ListView) findViewById(R.id.lstItems);
        display = (Button) findViewById(R.id.btnDisplay);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                list.add(value);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}