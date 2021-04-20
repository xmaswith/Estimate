package com.pri.estimate.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
import com.pri.estimate.DriverActivity;
import com.pri.estimate.FoodActivity;
import com.pri.estimate.GuideActivity;
import com.pri.estimate.HotelActivity;
import com.pri.estimate.MainActivity;
import com.pri.estimate.Model.SaveModel;
import com.pri.estimate.R;
import com.pri.estimate.TicketActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EstimateFragment extends Fragment {

    public static final int REQUEST_HOTEL = 100;
    public static final int REQUEST_AIR = 200;
    public static final int REQUEST_BUS = 300;
    public static final int REQUEST_FOOD = 400;
    public static final int REQUEST_TICKET = 500;
    public static final int REQUEST_GUIDE = 600;
    public static final int REQUEST_DRIVER = 700;
    public static final int REQUEST_DATE = 800;

    public static final int MAX_LIST = 50;

    private AdView mAdView;

    public SwitchCompat dateOpenSwitch;

    public ScrollView scrollView;
    public RelativeLayout hotel_bar, air_bar, transport_bar, food_bar, ticket_bar,
            guide_bar, driver_bar;

    Button new_btn, save_btn;

    public EditText person_et, foc_et, guide_et, discount_et;
    public TextView date_tv, hotel_price_tv, air_price_tv, bus_price_tv, food_price_tv, ticket_price_tv,
            guide_price_tv, driver_price_tv, symbol_tv, symbolGoal_tv, total_tv, exchangeTotal_tv;

    public int person=0, foc=0, guide=0, discount=0;

    int hotelPrice;
    int airPrice;
    int busPrice;
    int foodPrice;
    int ticketPrice;
    int guidePrice;
    int driverPrice;
    int totalPrice;
    long time = 0;
    double currencyRates = 0.0;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;

    String saveId, selectedDate, title="noTitle";
    Calendar calendar;

    private DecimalFormat df;

    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        df = new DecimalFormat("###,###");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("estimate").child(firebaseUser.getUid());
        saveId = reference.push().getKey();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estimate, container, false);

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

        new_btn = view.findViewById(R.id.new_btn);
        save_btn = view.findViewById(R.id.save_btn);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        calendar = Calendar.getInstance();

        setData();

        dateOpenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    DialogFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.setTargetFragment(EstimateFragment.this, REQUEST_DATE);
                    datePickerFragment.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "datePicker");
                } else {
                    date_tv.setVisibility(View.GONE);
                    date_tv.setText("none");
                }
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edittextToFirebase();
                getTotal();
            }
        };

        person_et.addTextChangedListener(textWatcher);
        foc_et.addTextChangedListener(textWatcher);
        guide_et.addTextChangedListener(textWatcher);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(saveId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        switch (v.getId()){
                            case R.id.hotel_bar:
                                putIntent(HotelActivity.class, REQUEST_HOTEL, "HotelPrice");
                                break;
                            case R.id.air_bar:
                                putIntent(AirActivity.class, REQUEST_AIR, "AirPrice");
                                break;
                            case R.id.transport_bar:
                                putIntent(BusActivity.class, REQUEST_BUS, "BusPrice");
                                break;
                            case R.id.food_bar:
                                putIntent(FoodActivity.class, REQUEST_FOOD, "FoodPrice");
                                break;
                            case R.id.ticket_bar:
                                putIntent(TicketActivity.class, REQUEST_TICKET, "TicketPrice");
                                break;
                            case R.id.guide_bar:
                                putIntent(GuideActivity.class, REQUEST_GUIDE, "GuidePrice");
                                break;
                            case R.id.driver_bar:
                                putIntent(DriverActivity.class, REQUEST_DRIVER, "DriverPrice");
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

        EditText title_et = new EditText(getContext());
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                if(title_et.getParent()!=null){
                    ((ViewGroup)title_et.getParent()).removeView(title_et);
                }
                builder.setTitle("제목을 입력하십시오")
                        .setView(title_et)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(title_et.getText().toString().equals("")){
                                    title = "sorry";
                                } else {
                                    title = title_et.getText().toString();
                                }
                                saveData(title);
                            }
                        })
                        .setNeutralButton("취소", null)
                        .create();
                builder.show();
            }
        });

        new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("저장되지 않은 데이터는 삭제 됩니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction()
                                        .detach(EstimateFragment.this).attach(EstimateFragment.this).commit();
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
                if(s.length()>0){
                    discount = Integer.parseInt(s.toString());
                    getTotal();
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
                    break;
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
                    break;
            }
            getTotal();
        }

    }

    public void putIntent(Class activityName, int request, String adapterName){
        Intent intent = new Intent(getContext(), activityName);
        intent.putExtra("saveId", saveId);
        intent.putExtra("edit", false);
        intent.putExtra("adapterName", adapterName);
        startActivityForResult(intent, request);
    }

    public void edittextToFirebase() throws NumberFormatException{
        Editable personVal = person_et.getText();
        Editable focVal = foc_et.getText();
        Editable guideVal = guide_et.getText();

        if(personVal.length()>0 && focVal.length()>0 && guideVal.length()>0){
            person = Integer.parseInt(personVal.toString());
            foc = Integer.parseInt(focVal.toString());
            guide = Integer.parseInt(guideVal.toString());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("person", person);
            hashMap.put("foc", foc);
            hashMap.put("guide", guide);
            reference.child(saveId).child("SetNumber").updateChildren(hashMap);
        }

    }

    private void setData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Save").child(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SaveModel saveModel = snapshot.getValue(SaveModel.class);
                if(saveModel!=null){
                    person_et.setText(""+saveModel.getPerson());
                    foc_et.setText(""+saveModel.getFoc());
                    guide_et.setText(""+saveModel.getGuide());
                    symbol_tv.setText(saveModel.getSymbol());
                    symbolGoal_tv.setText(saveModel.getSymbolGoal());
                    currencyRates = saveModel.getCurrency();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("estimate").child(firebaseUser.getUid())
                .child(saveId).child("saved").setValue(false);
    }

    private void getTotal(){
        totalPrice = (hotelPrice + busPrice + foodPrice + ticketPrice + guidePrice + driverPrice) /
                Integer.parseInt(person_et.getText().toString()) - discount;
        int airPricePerPerson = airPrice / Integer.parseInt(person_et.getText().toString());
        String totalPriceDecimal = df.format(totalPrice + (int)(airPricePerPerson/currencyRates));
        total_tv.setText(totalPriceDecimal);
        String exchangeTotalDecimal = df.format((int)(totalPrice * currencyRates) + airPricePerPerson);
        exchangeTotal_tv.setText(exchangeTotalDecimal);
    }

    private void saveData(String title){
        time = System.currentTimeMillis();

        try{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lists").child(firebaseUser.getUid());

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
            hashMap2.put("exchangeTotalPrice", exchangeTotal_tv.getText().toString());
            hashMap2.put("saveId", saveId);
            hashMap2.put("title", title);
            hashMap2.put("symbol", symbol_tv.getText().toString());
            hashMap2.put("symbolGoal", symbolGoal_tv.getText().toString());
            hashMap2.put("discount", discount_et.getText().toString());
            hashMap2.put("currency", currencyRates);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getChildrenCount()<MAX_LIST){
                        reference.child(saveId).updateChildren(hashMap2)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseDatabase.getInstance().getReference("estimate").child(firebaseUser.getUid())
                                                .child(saveId).child("saved").setValue(true);
                                        Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), MainActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(context, "최대 50개까지만 저장 가능합니다.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "모두 입력해 주십시오!", Toast.LENGTH_SHORT).show();
        }
    }

    private void noSave(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("estimate").child(firebaseUser.getUid())
                .child(saveId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("saved").getValue().equals(false)){
                    reference.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noSave();
    }

}