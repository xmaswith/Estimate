package com.pri.estimate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pri.estimate.Fragment.CalendarFragment;
import com.pri.estimate.Fragment.EstimateFragment;
import com.pri.estimate.Fragment.InputFragment;
import com.pri.estimate.Fragment.ListFragment;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private Fragment fList, fEst, fInput, fCal;

    public FirebaseAuth auth;
    public ProgressDialog dialog;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        auth = FirebaseAuth.getInstance();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait");
        dialog.setCanceledOnTouchOutside(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        fEst = new EstimateFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fEst).commit();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Save");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(auth.getCurrentUser().getUid())){
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("person", 10);
                    hashMap.put("foc", 0);
                    hashMap.put("guide", 0);
                    hashMap.put("hotelCount", 5);
                    hashMap.put("dayCount", 4);
                    hashMap.put("currency", 1);
                    hashMap.put("symbol", "USD ($)");
                    hashMap.put("symbolGoal", "USD ($)");

                    reference.child(auth.getCurrentUser().getUid()).updateChildren(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_estimate:
                            if(fEst == null){
                                fEst = new EstimateFragment();
                                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                                        fEst).commit();
                            }
                            showHide(fEst, fList, fInput, fCal);
                            break;
                        case R.id.nav_list:
                            if(fList == null){
                                fList = new ListFragment();
                                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                                        fList).commit();
                            }
                            showHide(fList, fEst, fInput, fCal);
                            break;
                        case R.id.nav_calendar:
                            if(fCal == null){
                                fCal = new CalendarFragment();
                                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                                        fCal).commit();
                            }
                            showHide(fCal, fEst, fList, fInput);
                            break;
                        case R.id.nav_input:
                            if(fInput == null){
                                fInput = new InputFragment();
                                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                                        fInput).commit();
                            }
                            showHide(fInput, fEst, fList, fCal);
                            break;
                    }
                    return true;
                }
            };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showHide(Fragment fa, Fragment fb, Fragment fc, Fragment fd){
        if(fa != null) getSupportFragmentManager().beginTransaction().show(fa).commit();
        if(fb != null) getSupportFragmentManager().beginTransaction().hide(fb).commit();
        if(fc != null) getSupportFragmentManager().beginTransaction().hide(fc).commit();
        if(fd != null) getSupportFragmentManager().beginTransaction().hide(fd).commit();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce){
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }
}