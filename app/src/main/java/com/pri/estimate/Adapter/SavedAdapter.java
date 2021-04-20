package com.pri.estimate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
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

        final EstimateModel estimateModel = estimateModels.get(position);

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

        if(position>=0){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, v);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete:
                                    FirebaseDatabase.getInstance().getReference("Lists").child(firebaseUser.getUid())
                                            .child(estimateModel.getSaveId()).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    try{
                                                        estimateModels.remove(position);
                                                        notifyItemRemoved(position);
                                                        notifyItemRangeChanged(position, getItemCount() - position);
                                                        Toast.makeText(mContext, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                                                    } catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                    FirebaseDatabase.getInstance().getReference("estimate").child(firebaseUser.getUid())
                                            .child(estimateModel.getSaveId()).removeValue();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return estimateModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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

//            itemView.setOnCreateContextMenuListener(this);
        }

        /*@Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Delete = menu.add(Menu.NONE, 1001, 1, "삭제");
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 1001:
                        FirebaseDatabase.getInstance().getReference("Lists").child(firebaseUser.getUid())
                                .child(id).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(getAdapterPosition() > 0){
                                            estimateModels.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), estimateModels.size());
                                        }
                                        if (task.isSuccessful()){
                                            Toast.makeText(mContext, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
                return true;
            }
        };*/

    }
}
