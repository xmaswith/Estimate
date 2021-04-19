package com.pri.estimate.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pri.estimate.Fragment.EstimateFragment;
import com.pri.estimate.Model.InputModel;
import com.pri.estimate.Model.SaveModel;
import com.pri.estimate.Model.SetNumber;
import com.pri.estimate.R;
import com.pri.estimate.TextChanged;

import java.util.List;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder>{

    private Context mContext;
    private List<InputModel> mInputModels;
    private String saveId;
    private TextChanged textChanged;
    private String adapterName;

    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;

    EstimateFragment estimateFragment = new EstimateFragment();


    public PriceAdapter(Context mContext, List<InputModel> mInputModels, String saveId, String adapterName, TextChanged textChanged) {
        this.mContext = mContext;
        this.mInputModels = mInputModels;
        this.saveId = saveId;
        this.adapterName = adapterName;
        this.textChanged = textChanged;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.et_input2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        final InputModel inputModel = mInputModels.get(position);

        holder.price_et.setSelection(holder.price_et.length());
        holder.count_et.setSelection(holder.count_et.length());


        getText(inputModel.getNumber(), holder.price_et, holder.count_et);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                getResult(holder, position);
            }
        };

        holder.price_et.addTextChangedListener(textWatcher);
        holder.count_et.addTextChangedListener(textWatcher);
    }

    @Override
    public int getItemCount() {
        return mInputModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView result_tv;
        EditText price_et, count_et;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            result_tv = itemView.findViewById(R.id.result_tv);
            price_et = itemView.findViewById(R.id.price_et);
            count_et = itemView.findViewById(R.id.count_et);
        }
    }

    int price = 0, countPrice = 0;

    public void getResult(ViewHolder viewHolder, int position) throws  NumberFormatException{
        Editable val = viewHolder.price_et.getText();
        Editable val2 = viewHolder.count_et.getText();

        if (val2 != null && val2.length() >= 1){
            if(val.length() >= 1){
                price = Integer.parseInt(val.toString());
                countPrice = Integer.parseInt(val2.toString());
                int result = price * countPrice;
                textChanged.onChanged(R.id.result_tv, position, ""+price, ""+countPrice);
                viewHolder.result_tv.setText(""+result);
            } else {
                price = 0;
                countPrice = Integer.parseInt(val2.toString());
                textChanged.onChanged(R.id.result_tv, position, ""+price, ""+countPrice);
                viewHolder.result_tv.setText("총액");
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    private void getText(int number, EditText price_et, EditText count_et){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("estimate").child(firebaseUser.getUid())
                .child(saveId).child(adapterName).child(""+number);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                InputModel inputModel = snapshot.getValue(InputModel.class);
                if (inputModel != null){
                    price_et.setText(""+ inputModel.getPrice(), TextView.BufferType.EDITABLE);
                    count_et.setText(""+ inputModel.getCount(), TextView.BufferType.EDITABLE);
                } else {
                    setCountPrice(count_et);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCountPrice(EditText count_et){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("estimate").child(firebaseUser.getUid())
                .child(saveId).child("SetNumber");

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Save")
                .child(firebaseUser.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SetNumber setNumber = snapshot.getValue(SetNumber.class);
                switch (adapterName){
                    case "HotelPrice":
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                SaveModel saveModel = snapshot.getValue(SaveModel.class);
                                count_et.setText(""+saveModel.getHotelCount());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    case "AirPrice":
                    case "FoodPrice":
                    case "TicketPrice":
                        countPrice = setNumber.getPerson() + setNumber.getFoc();
                        count_et.setText(""+countPrice, TextView.BufferType.EDITABLE);
                        break;
                    case "BusPrice":
                        countPrice = setNumber.getGuide();
                        count_et.setText(""+countPrice, TextView.BufferType.EDITABLE);
                        break;
                    case "GuidePrice":
                    case "DriverPrice":
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                SaveModel saveModel = snapshot.getValue(SaveModel.class);
                                count_et.setText(""+saveModel.getDayCount());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
