package com.pri.estimate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pri.estimate.Adapter.PriceAdapter;
import com.pri.estimate.Fragment.EstimateFragment;
import com.pri.estimate.Model.InputModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private final static int MAX = 2;

    RecyclerView list_rv;
    Button confirm_btn;
    TextView total;
    ImageButton add_ib;

    Context mContext;

    private int mTotal = 0;

    EstimateFragment estimateFragment;

    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;

    double timeNow;
    String saveId;
    String adapterName;
    int addCount;

    InputModel inputModel;

    private List<InputModel> mInputModels;
    private PriceAdapter priceAdapter;
    private Integer[] results = new Integer[10];
    private Integer[] prices = new Integer[10];
    private Integer[] counts = new Integer[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        timeNow = System.currentTimeMillis();

        inputModel = new InputModel();

        Intent intent = getIntent();
        saveId = intent.getStringExtra("saveId");
        adapterName = intent.getStringExtra("adapterName");

        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(adapterName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list_rv = findViewById(R.id.list_rv);
        confirm_btn = findViewById(R.id.confirm_btn);
        total = findViewById(R.id.total);
        add_ib = findViewById(R.id.add_ib);

        estimateFragment = new EstimateFragment();

        list_rv.setHasFixedSize(true);
        list_rv.setLayoutManager(new LinearLayoutManager(mContext));

        mInputModels = new ArrayList<>();
        priceAdapter = new PriceAdapter(GuideActivity.this, mInputModels, saveId, adapterName, new TextChanged() {
            @Override
            public void onChanged(int result, int position, String priceV, String countV) {
                prices[position] = Integer.parseInt(priceV);
                counts[position] = Integer.parseInt(countV);
                results[position] = prices[position] * counts[position];
                mTotal = grandTotal(results);
                DecimalFormat myFormatter = new DecimalFormat("###,###");
                String mTotalDecimal = myFormatter.format(mTotal);
                total.setText(GuideActivity.this.getString(R.string.total)+" : " + mTotalDecimal);
            }
        });


        addSavedItem();

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });



        add_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addCount < 8){
                    addCount++;
                    mInputModels.add(inputModel);
                    priceAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(GuideActivity.this, GuideActivity.this.getString(R.string.estimateLimit_tst), Toast.LENGTH_SHORT).show();
                }
            }
        });


        list_rv.setAdapter(priceAdapter);

    }

    public void inputData(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("estimate").child(firebaseUser.getUid())
                .child(saveId).child(adapterName);

        for(int i = 0 ; i < addCount; i++) {
            int number = i+1;

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("number", number);
            hashMap.put("price", prices[i]);
            hashMap.put("count", counts[i]);

            ref.child(""+number).updateChildren(hashMap);

        }

        Intent intent = new Intent();
        intent.putExtra("totalPrice", ""+mTotal);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void addSavedItem(){
        if (firebaseUser != null){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("estimate").child(firebaseUser.getUid())
                    .child(saveId).child(adapterName);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mInputModels.clear();
                    if (snapshot.getChildrenCount()==0){
                        for(int i=0;i<MAX;i++) {
                            mInputModels.add(inputModel);
                        }
                        addCount = MAX;
                    } else {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            InputModel input = dataSnapshot.getValue(InputModel.class);
                            mInputModels.add(input);
                            addCount = (int) snapshot.getChildrenCount();
                        }
                    }
                    priceAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public int grandTotal(Integer[] results){

        int totalPrice = 0;
        for(int i = 0 ; i < results.length; i++) {
            if (results[i] != null){
                totalPrice += results[i];
            }
        }
        return totalPrice;
    }




}