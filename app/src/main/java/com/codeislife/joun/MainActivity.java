package com.codeislife.joun;


import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String DATABASE_CONFIG = "Diary";
    private static final String TAG = "MAIN_ACTIVITY";
    FloatingActionButton floatingActionButton;
    ProgressBar progressBar;
    DiaryEntry entry;
    RecyclerView entryList;
    String user;
    int count = 0;
    CustomAdapter adapter;
    FirebaseDatabase db;
    private List<DiaryEntry> entries;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth auth;
    LinearLayoutManager linearLayoutManager;

    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                getStarted();
    }

    private void getStarted() {

        setBaseGround();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        entryList = findViewById(R.id.recyclerViewTasks);
        entryList.setLayoutManager(linearLayoutManager);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        entries = new ArrayList<>();
        entry = new DiaryEntry();
        getEntries();
        setClicks();
        entryList.setAdapter(adapter);
        mShimmerViewContainer.stopShimmerAnimation();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (auth.getCurrentUser() == null){

                    startActivity(new Intent(MainActivity.this, Login.class));
                }
            }
        };


    }

    private void setClicks() {
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewEntry.class);
                startActivity(intent);
            }
        });
        adapter = new CustomAdapter(entries, new CustomAdapter.AdapterClickListener() {
            @Override
            public void onEntryClicked(DiaryEntry entry) {
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra(DetailActivity.ENTRY_ARGS, entry);
                startActivity(i);
            }
        });
    }



    private void setBaseGround() {
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser().getUid();
    }

    private void getEntries() {
        DatabaseReference entryRef = db.getReference(DATABASE_CONFIG);
        entryRef.addValueEventListener(eventListener);

    }



    private ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            entries.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                entry = snapshot.getValue(DiaryEntry.class);
                entry.setCid(count);
                entries.add(entry);
                count++;
                adapter.notifyDataSetChanged();
                if (count >= dataSnapshot.getChildrenCount()) {
                    adapter.replaceData(entries);
                    count = 0;
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(MainActivity.this, "Error Occurred",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.menu, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }

    // COMPLETED (6) When the "Settings" menu item is pressed, open SettingsActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
           auth.signOut();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(mAuthListener);
    }
    //    @Override
//    protected void onPause() {
//        mShimmerViewContainer.stopShimmerAnimation();
//        super.onPause();
//    }
}
