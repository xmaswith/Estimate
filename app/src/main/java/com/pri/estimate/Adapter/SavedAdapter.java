package com.pri.estimate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pri.estimate.Model.EstimateModel;
import com.pri.estimate.R;
import com.pri.estimate.SavedEstimateActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder>{

    private final int limit = 50;

    private Context mContext;
    private List<EstimateModel> estimateModels;

    private FirebaseUser firebaseUser;

    public SavedAdapter(Context mContext, List<EstimateModel> estimateModels) {
        this.mContext = mContext;
        this.estimateModels = estimateModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new SavedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        EstimateModel estimateModel = estimateModels.get(position);

        String title = estimateModel.getTitle();
        long time = estimateModel.getTime();
        String date = estimateModel.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String saveTime = dateFormat.format(time);

        holder.date_tv.setText(date);
        holder.title_tv.setText(title);
        holder.saveDate_tv.setText(saveTime);
        holder.number_tv.setText(""+(position+1));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SavedEstimateActivity.class);
                intent.putExtra("saveId", estimateModel.getSaveId());
                intent.putExtra("time", estimateModel.getTime());
                intent.putExtra("title", estimateModel.getTitle());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(estimateModels.size() > limit){
            return limit;
        } else {
            return estimateModels.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date_tv;
        public TextView title_tv;
        public TextView saveDate_tv;
        public TextView number_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_tv = itemView.findViewById(R.id.date_tv);
            title_tv = itemView.findViewById(R.id.title_tv);
            saveDate_tv = itemView.findViewById(R.id.saveDate_tv);
            number_tv = itemView.findViewById(R.id.number_tv);
        }
    }
}
