package com.pri.estimate.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pri.estimate.Model.EstimateModel;
import com.pri.estimate.R;

import java.util.Calendar;

public class SavedEstimateFragment extends Fragment {

    public SwitchCompat dateOpenSwitch;

    public ScrollView scrollView;
    public RelativeLayout hotel_bar, air_bar, transport_bar, food_bar, ticket_bar,
            guide_bar, driver_bar;

    public EditText person_et, foc_et, guide_et, discount_et;
    public TextView date_tv, hotel_price_tv, air_price_tv, bus_price_tv, food_price_tv, ticket_price_tv,
            guide_price_tv, driver_price_tv, symbol_tv, symbolGoal_tv, total_tv, exchangeTotal_tv;

    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;

    String saveId;
    long savedTime;
    Calendar calendar;

    public SavedEstimateFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        saveId = prefs.getString("saveId", "none");
        savedTime = prefs.getLong("time", 0);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_estimate, container, false);

        dateOpenSwitch = view.findViewById(R.id.dateOpenSwitch);
        scrollView = view.findViewById(R.id.scrollView);

        person_et = view.findViewById(R.id.person_et);
        foc_et = view.findViewById(R.id.foc_et);
        guide_et = view.findViewById(R.id.guide_et);
        discount_et = view.findViewById(R.id.discount_et);

        hotel_bar = view.findViewById(R.id.hotel_bar);
        air_bar = view.findViewById(R.id.air_bar);
        transport_bar = view.findViewById(R.id.transport_bar);
        food_bar = view.findViewById(R.id.food_bar);
        ticket_bar = view.findViewById(R.id.ticket_bar);
        guide_bar = view.findViewById(R.id.guide_bar);
        driver_bar = view.findViewById(R.id.driver_bar);

        date_tv = view.findViewById(R.id.date_tv);
        hotel_price_tv = view.findViewById(R.id.hotel_price_tv);
        air_price_tv = view.findViewById(R.id.air_price_tv);
        bus_price_tv = view.findViewById(R.id.bus_price_tv);
        food_price_tv = view.findViewById(R.id.food_price_tv);
        ticket_price_tv = view.findViewById(R.id.ticket_price_tv);
        guide_price_tv = view.findViewById(R.id.guide_price_tv);
        driver_price_tv = view.findViewById(R.id.driver_price_tv);
        symbol_tv = view.findViewById(R.id.symbol_tv);
        symbolGoal_tv = view.findViewById(R.id.symbolGoal_tv);
        total_tv = view.findViewById(R.id.total_tv);
        exchangeTotal_tv = view.findViewById(R.id.exchangeTotal_tv);

        calendar = Calendar.getInstance();

        setData();

        dateOpenSwitch.setEnabled(false);
        person_et.setEnabled(false);
        foc_et.setEnabled(false);
        guide_et.setEnabled(false);
        discount_et.setEnabled(false);

        return view;
    }

    private void setData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Lists").child(firebaseUser.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    EstimateModel estimateModel = dataSnapshot.getValue(EstimateModel.class);
                    if(estimateModel!=null){

                        person_et.setText(estimateModel.getPerson());
                        foc_et.setText(estimateModel.getFoc());
                        guide_et.setText(estimateModel.getGuide());
                        discount_et.setText(estimateModel.getDiscount());

                        hotel_price_tv.setText(estimateModel.getHotelPrice());
                        air_price_tv.setText(estimateModel.getAirPrice());
                        bus_price_tv.setText(estimateModel.getBusPrice());
                        food_price_tv.setText(estimateModel.getFoodPrice());
                        ticket_price_tv.setText(estimateModel.getTicketPrice());
                        guide_price_tv.setText(estimateModel.getGuidePrice());
                        driver_price_tv.setText(estimateModel.getDriverPrice());
                        total_tv.setText(estimateModel.getTotalPrice());
                        exchangeTotal_tv.setText(estimateModel.getExchangeTotalPrice());
                        symbol_tv.setText(estimateModel.getSymbol());
                        symbolGoal_tv.setText(estimateModel.getSymbolGoal());
                        date_tv.setText(estimateModel.getDate());
                        if(!date_tv.getText().equals("none")){
                            dateOpenSwitch.isChecked();
                            date_tv.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}