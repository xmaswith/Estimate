package com.pri.estimate.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pri.estimate.AirActivity;
import com.pri.estimate.BusActivity;
import com.pri.estimate.DatabaseHelper;
import com.pri.estimate.DriverActivity;
import com.pri.estimate.FoodActivity;
import com.pri.estimate.GuideActivity;
import com.pri.estimate.HotelActivity;
import com.pri.estimate.MainActivity;
import com.pri.estimate.Model.EstimateModel;
import com.pri.estimate.R;
import com.pri.estimate.TicketActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EditEstimateFragment extends Fragment {

    public static final int REQUEST_HOTEL = 100;
    public static final int REQUEST_AIR = 200;
    public static final int REQUEST_BUS = 300;
    public static final int REQUEST_FOOD = 400;
    public static final int REQUEST_TICKET = 500;
    public static final int REQUEST_GUIDE = 600;
    public static final int REQUEST_DRIVER = 700;
    public static final int REQUEST_DATE = 800;


    public SwitchCompat dateOpenSwitch;

    public ScrollView scrollView;
    public RelativeLayout hotel_bar, air_bar, transport_bar, food_bar, ticket_bar,
            guide_bar, driver_bar;

    Button saveAs_btn, save_btn;

    public EditText person_et, foc_et, guide_et, discount_et;
    public TextView date_tv, hotel_price_tv, air_price_tv, bus_price_tv, food_price_tv, ticket_price_tv,
            guide_price_tv, driver_price_tv, symbol_tv, total_tv, title_tv, exchangeTotal_tv, symbolGoal_tv;

    public int personVal=0, focVal=0, guideVal=0, discount=0;

    int hotelPrice;
    int airPrice;
    int busPrice;
    int foodPrice;
    int ticketPrice;
    int guidePrice;
    int driverPrice;
    int totalPrice;
    double currencyRates = 0.0;

    public DatabaseHelper db;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;

    String saveId, selectedDate, tempSaveId;
    String savedTitle;
    long savedTime;
    Calendar calendar;

    private DecimalFormat df;

    public EditEstimateFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        df = new DecimalFormat("###,###");

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        saveId = prefs.getString("saveId", "none");
        savedTime = prefs.getLong("time", 0);
        savedTitle = prefs.getString("title", "none");

        tempSaveId = saveId;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("estimate").child(firebaseUser.getUid());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_estimate, container, false);
        db = new DatabaseHelper(getContext());

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
        total_tv = view.findViewById(R.id.total_tv);
        title_tv = view.findViewById(R.id.title_tv);

        saveAs_btn = view.findViewById(R.id.saveAs_btn);
        save_btn = view.findViewById(R.id.save_btn);

        calendar = Calendar.getInstance();

        setData();


        dateOpenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    DialogFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.setTargetFragment(EditEstimateFragment.this, REQUEST_DATE);
                    datePickerFragment.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "datePicker");
                } else {
                    date_tv.setVisibility(View.GONE);
                    date_tv.setText("none");
                }
            }
        });

        person_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= 1){
                    personVal = Integer.parseInt(s.toString());
                    edittextToFirebase(personVal, focVal, guideVal);
                }
            }
        });

        foc_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= 1){
                    focVal = Integer.parseInt(s.toString());
                    edittextToFirebase(personVal, focVal, guideVal);
                }
            }
        });

        guide_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= 1){
                    guideVal = Integer.parseInt(s.toString());
                    edittextToFirebase(personVal, focVal, guideVal);
                }
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(saveId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean input = false;
                        switch (v.getId()){
                            case R.id.hotel_bar:
                                if(snapshot.hasChild("HotelPrice")){
                                    input = true;
                                }
                                putIntent(HotelActivity.class, input, REQUEST_HOTEL, "HotelPrice");
                                break;
                            case R.id.air_bar:
                                if(snapshot.hasChild("AirPrice")){
                                    input = true;
                                }
                                putIntent(AirActivity.class, input, REQUEST_AIR, "AirPrice");
                                break;
                            case R.id.transport_bar:
                                if(snapshot.hasChild("BusPrice")){
                                    input = true;
                                }
                                putIntent(BusActivity.class, input, REQUEST_BUS, "BusPrice");
                                break;
                            case R.id.food_bar:
                                if(snapshot.hasChild("FoodPrice")){
                                    input = true;
                                }
                                putIntent(FoodActivity.class, input, REQUEST_FOOD, "FoodPrice");
                                break;
                            case R.id.ticket_bar:
                                if(snapshot.hasChild("TicketPrice")){
                                    input = true;
                                }
                                putIntent(TicketActivity.class, input, REQUEST_TICKET, "TicketPrice");
                                break;
                            case R.id.guide_bar:
                                if(snapshot.hasChild("GuidePrice")){
                                    input = true;
                                }
                                putIntent(GuideActivity.class, input, REQUEST_GUIDE, "GuidePrice");
                                break;
                            case R.id.driver_bar:
                                if(snapshot.hasChild("DriverPrice")){
                                    input = true;
                                }
                                putIntent(DriverActivity.class, input, REQUEST_DRIVER, "DriverPrice");
                                break;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };


        hotel_bar.setOnClickListener(onClickListener);
        air_bar.setOnClickListener(onClickListener);
        transport_bar.setOnClickListener(onClickListener);
        food_bar.setOnClickListener(onClickListener);
        ticket_bar.setOnClickListener(onClickListener);
        guide_bar.setOnClickListener(onClickListener);
        driver_bar.setOnClickListener(onClickListener);


        final EditText title_et = new EditText(getContext());
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("덮어 씌우겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveData();
                            }
                        })
                        .setNeutralButton("취소", null)
                        .create().show();
            }
        });

        saveAs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("제목을 입력하십시오")
                        .setView(title_et)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title;
                                if(title_et.getText().toString().equals("")){
                                    title = "sorry";
                                } else {
                                    title = title_et.getText().toString();
                                }
                                newSaveData(title);
                            }
                        })
                        .setNeutralButton("취소", null)
                        .create().show();
            }
        });

        discount_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(discount_et.getText().length()>0){
                    discount = Integer.parseInt(discount_et.getText().toString());
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_HOTEL:
                    hotelPrice = Integer.parseInt(data.getExtras().getString("totalPrice"));
                    String hotelPriceDecimal = df.format(hotelPrice);
                    hotel_price_tv.setText(hotelPriceDecimal);
                    break;
                case REQUEST_AIR:
                    airPrice = Integer.parseInt(data.getExtras().getString("totalPrice"));
                    String airPriceDecimal = df.format(airPrice);
                    air_price_tv.setText(airPriceDecimal);
                    break;
                case REQUEST_BUS:
                    busPrice = Integer.parseInt(data.getExtras().getString("totalPrice"));
                    String busPriceDecimal = df.format(busPrice);
                    bus_price_tv.setText(busPriceDecimal);
                    break;
                case REQUEST_FOOD:
                    foodPrice = Integer.parseInt(data.getExtras().getString("totalPrice"));
                    String foodPriceDecimal = df.format(foodPrice);
                    food_price_tv.setText(foodPriceDecimal);
                    break;
                case REQUEST_TICKET:
                    ticketPrice = Integer.parseInt(data.getExtras().getString("totalPrice"));
                    String ticketPriceDecimal = df.format(ticketPrice);
                    ticket_price_tv.setText(ticketPriceDecimal);
                    break;
                case REQUEST_GUIDE:
                    guidePrice = Integer.parseInt(data.getExtras().getString("totalPrice"));
                    String guidePriceDecimal = df.format(guidePrice);
                    guide_price_tv.setText(guidePriceDecimal);
                    break;
                case REQUEST_DRIVER:
                    driverPrice = Integer.parseInt(data.getExtras().getString("totalPrice"));
                    String driverPriceDecimal = df.format(driverPrice);
                    driver_price_tv.setText(driverPriceDecimal);
                case REQUEST_DATE:
                    selectedDate = data.getStringExtra("selectedDate");
                    try{
                        SimpleDateFormat format = new SimpleDateFormat("yyyy / MM / dd", Locale.KOREA);
                        Date tempDate = format.parse(selectedDate);
                        calendar.setTime(tempDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    date_tv.setText(selectedDate);
                    date_tv.setVisibility(View.VISIBLE);
            }
            getTotal();
        }

    }

    public void putIntent(Class activityName, boolean input, int request, String adapterName){
        Intent intent = new Intent(getContext(), activityName);
        intent.putExtra("saveId", saveId);
        intent.putExtra("input", input);
        intent.putExtra("adapterName", adapterName);
        startActivityForResult(intent, request);
    }

    public void edittextToFirebase(int personVal, int focVal, int guideVal) throws NumberFormatException{
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("person", personVal);
        hashMap.put("foc", focVal);
        hashMap.put("guide", guideVal);

        reference.child(saveId).child("SetNumber").updateChildren(hashMap);
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
                        symbol_tv.setText(estimateModel.getSymbol());
                        symbolGoal_tv.setText(estimateModel.getSymbolGoal());
                        currencyRates = estimateModel.getCurrency();
                        hotel_price_tv.setText(estimateModel.getHotelPrice());
                        air_price_tv.setText(estimateModel.getAirPrice());
                        bus_price_tv.setText(estimateModel.getBusPrice());
                        food_price_tv.setText(estimateModel.getFoodPrice());
                        ticket_price_tv.setText(estimateModel.getTicketPrice());
                        guide_price_tv.setText(estimateModel.getGuidePrice());
                        driver_price_tv.setText(estimateModel.getDriverPrice());
                        total_tv.setText(estimateModel.getTotalPrice());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getTotal(){
        totalPrice = (hotelPrice + airPrice + busPrice + foodPrice + ticketPrice + guidePrice + driverPrice - discount) /
                Integer.parseInt(person_et.getText().toString());
        String totalPriceDecimal = df.format(totalPrice);
        total_tv.setText(totalPriceDecimal);
        String exchangeTotalDecimal = df.format((int)(totalPrice * currencyRates));
        exchangeTotal_tv.setText(exchangeTotalDecimal);
    }

    private void saveData(){

        try{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lists").child(firebaseUser.getUid())
                    .child(saveId);

            HashMap<String, Object> hashMap2 = new HashMap<>();
            hashMap2.put("date", date_tv.getText().toString());
            hashMap2.put("time", savedTime);
            hashMap2.put("person", person_et.getText().toString());
            hashMap2.put("foc", foc_et.getText().toString());
            hashMap2.put("guide", guide_et.getText().toString());
            hashMap2.put("hotelPrice", hotel_price_tv.getText().toString());
            hashMap2.put("airPrice", air_price_tv.getText().toString());
            hashMap2.put("busPrice", bus_price_tv.getText().toString());
            hashMap2.put("foodPrice", food_price_tv.getText().toString());
            hashMap2.put("ticketPrice", ticket_price_tv.getText().toString());
            hashMap2.put("guidePrice", guide_price_tv.getText().toString());
            hashMap2.put("driverPrice", driver_price_tv.getText().toString());
            hashMap2.put("totalPrice", total_tv.getText().toString());
            hashMap2.put("saveId", saveId);
            hashMap2.put("title", savedTitle);
            hashMap2.put("symbol", symbol_tv.getText().toString());

            reference.updateChildren(hashMap2)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "모두 입력해 주십시오!", Toast.LENGTH_SHORT).show();
        }
    }

    private void newSaveData(String title){
        long time = System.currentTimeMillis();

        try{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lists").child(firebaseUser.getUid())
                    .push();

            HashMap<String, Object> hashMap2 = new HashMap<>();
            hashMap2.put("date", date_tv.getText().toString());
            hashMap2.put("time", time);
            hashMap2.put("person", person_et.getText().toString());
            hashMap2.put("foc", foc_et.getText().toString());
            hashMap2.put("guide", guide_et.getText().toString());
            hashMap2.put("hotelPrice", hotel_price_tv.getText().toString());
            hashMap2.put("airPrice", air_price_tv.getText().toString());
            hashMap2.put("busPrice", bus_price_tv.getText().toString());
            hashMap2.put("foodPrice", food_price_tv.getText().toString());
            hashMap2.put("ticketPrice", ticket_price_tv.getText().toString());
            hashMap2.put("guidePrice", guide_price_tv.getText().toString());
            hashMap2.put("driverPrice", driver_price_tv.getText().toString());
            hashMap2.put("totalPrice", total_tv.getText().toString());
            hashMap2.put("saveId", saveId);
            hashMap2.put("title", title);
            hashMap2.put("symbol", symbol_tv.getText().toString());

            reference.updateChildren(hashMap2)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "모두 입력해 주십시오!", Toast.LENGTH_SHORT).show();
        }
    }

}