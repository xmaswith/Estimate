package com.pri.estimate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pri.estimate.Fragment.EditEstimateFragment;
import com.pri.estimate.Fragment.SavedEstimateFragment;

public class SavedEstimateActivity extends AppCompatActivity {

    String saveId;
    String title;
    long time;

    TextView title_tv;
    Button edit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_estimate);

        title_tv = findViewById(R.id.title_tv);
        edit_btn = findViewById(R.id.edit_btn);
        edit_btn.setVisibility(View.VISIBLE);

        Bundle intent = getIntent().getExtras();
        if (intent != null){
            saveId = intent.getString("saveId");
            title = intent.getString("title");
            time = intent.getLong("time");

            title_tv.setText(title);

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("saveId", saveId);
            editor.putLong("time", time);
            editor.putString("title", title);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SavedEstimateFragment()).commit();
        }

        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EditEstimateFragment()).commit();
                edit_btn.setVisibility(View.GONE);
            }
        });
    }
}