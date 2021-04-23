package com.pri.estimate.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.pri.estimate.LocaleHelper;
import com.pri.estimate.Model.SaveModel;
import com.pri.estimate.R;

import java.util.HashMap;


public class InputFragment extends Fragment {


    EditText person_et, foc_et, guide_et, hotelCount_et, dayCount_et, currency_et;
    Button symbol_btn, symbolGoal_btn, save_btn, currencySearch_btn, language_btn;

    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    public String currencySymbol, currencyGoalSymbol;
    public String countryCode, countryGoalCode;

    private String languageKo = "ko";
    private String languageEn = "en";
    private String languageTw = "tw";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        person_et = view.findViewById(R.id.person_et);
        foc_et = view.findViewById(R.id.foc_et);
        guide_et = view.findViewById(R.id.guide_et);
        hotelCount_et = view.findViewById(R.id.hotelCount_et);
        dayCount_et = view.findViewById(R.id.dayCount_et);
        currency_et = view.findViewById(R.id.currency_et);
        symbol_btn = view.findViewById(R.id.symbol_btn);
        save_btn = view.findViewById(R.id.save_btn);
        symbolGoal_btn = view.findViewById(R.id.symbolGoal_btn);
        currencySearch_btn = view.findViewById(R.id.currencySearch_btn);
        language_btn = view.findViewById(R.id.language_btn);


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadData();

        symbol_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        // Implement your code here
                        currencySymbol = symbol;
                        countryCode = code;
                        symbol_btn.setText(code+" (" + symbol + ")");
                        picker.dismiss();
                    }
                });
                picker.show(getFragmentManager(), "CURRENCY_PICKER");

            }
        });

        symbolGoal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        // Implement your code here
                        currencyGoalSymbol = symbol;
                        countryGoalCode = code;
                        symbolGoal_btn.setText(code+" (" + symbol + ")");
                        picker.dismiss();
                    }
                });
                picker.show(getFragmentManager(), "CURRENCY_PICKER");
            }
        });

        currencySearch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countryCode!=null&&countryGoalCode!=null){
                    Intent urlintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://finance.yahoo.com/quote/"+countryCode+countryGoalCode+"=X?p="+
                            countryCode+countryGoalCode+"=X&.tsrc=fin-srch"));
                    startActivity(urlintent);
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.currencyAlert_tst), Toast.LENGTH_SHORT).show();
                }

            }
        });

        language_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"한국어", "ENGLISH", "中文(繁體)"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                LocaleHelper.setLocale(getContext(), languageKo);
                                break;
                            case 1:
                                LocaleHelper.setLocale(getContext(), languageEn);
                                break;
                            case 2:
                                LocaleHelper.setLocale(getContext(), languageTw);
                                break;
                        }
                        dialog.dismiss();
                        getActivity().recreate();
                    }
                }).create().show();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

        return view;
    }

    public void inputData(){

        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Save").child(firebaseUser.getUid());

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("person", Integer.parseInt(person_et.getText().toString()));
            hashMap.put("foc", Integer.parseInt(foc_et.getText().toString()));
            hashMap.put("guide", Integer.parseInt(guide_et.getText().toString()));
            hashMap.put("hotelCount", Integer.parseInt(hotelCount_et.getText().toString()));
            hashMap.put("dayCount", Integer.parseInt(dayCount_et.getText().toString()));
            hashMap.put("currency", Double.valueOf(currency_et.getText().toString()));
            hashMap.put("symbol", symbol_btn.getText().toString());
            hashMap.put("symbolGoal", symbolGoal_btn.getText().toString());

            progressDialog.setMessage("저장중...");
            progressDialog.show();

            ref.updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), getContext().getString(R.string.save_tst), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e){
            Toast.makeText(getContext(), getContext().getString(R.string.saveAlert_tst), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Save").child(firebaseUser.getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SaveModel save = snapshot.getValue(SaveModel.class);
                if (save != null){
                    person_et.setText(""+save.getPerson(), TextView.BufferType.EDITABLE);
                    foc_et.setText(""+save.getFoc(), TextView.BufferType.EDITABLE);
                    guide_et.setText(""+save.getGuide(), TextView.BufferType.EDITABLE);
                    hotelCount_et.setText(""+save.getHotelCount(), TextView.BufferType.EDITABLE);
                    dayCount_et.setText(""+save.getDayCount(), TextView.BufferType.EDITABLE);
                    currency_et.setText(""+save.getCurrency(), TextView.BufferType.EDITABLE);
                    symbol_btn.setText(save.getSymbol());
                    symbolGoal_btn.setText(save.getSymbolGoal());

                    countryCode = save.getSymbol().substring(0, 3);
                    countryGoalCode = save.getSymbolGoal().substring(0, 3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}