package com.pri.estimate.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pri.estimate.Adapter.SavedAdapter;
import com.pri.estimate.Events.EventCalendar;
import com.pri.estimate.Events.SaturdaySetColor;
import com.pri.estimate.Events.SelectCalendar;
import com.pri.estimate.Events.SundaySetColor;
import com.pri.estimate.Events.TodayCalendar;
import com.pri.estimate.Model.EstimateModel;
import com.pri.estimate.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CalendarFragment extends Fragment {

    MaterialCalendarView calendarView;
    LinearLayout list_ln;

    TextView date_tv;

    FirebaseUser firebaseUser;

    private RecyclerView recycler_view;
    private SavedAdapter savedAdapter;
    private List<EstimateModel> estimateModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(layoutManager);

        estimateModels = new ArrayList<>();
        savedAdapter = new SavedAdapter(getContext(), estimateModels);
        recycler_view.setAdapter(savedAdapter);

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.addDecorators(new SundaySetColor(), new SaturdaySetColor(), new SelectCalendar(getActivity()),
                new TodayCalendar(getActivity()));

        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                getSelectedDay(date);
            }
        });

        getDate();

        return view;
    }

    private void getDate(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lists").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    EstimateModel estimateModel = dataSnapshot.getValue(EstimateModel.class);
                    if(!estimateModel.getDate().equals("none")){
                        String[] time = estimateModel.getDate().trim().split("/");

                        int year = Integer.parseInt(time[0]);
                        int month = Integer.parseInt(time[1]);
                        int dayy = Integer.parseInt(time[2]);

                        calendarView.addDecorator(new EventCalendar(Color.RED, Collections.singleton(CalendarDay.from(year, month-1, dayy))));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSelectedDay(CalendarDay date){
        int dayy = date.getDay();
        int month = date.getMonth()+1;
        int year = date.getYear();

        searchedDates(year, month, dayy);
    }

    private void searchedDates (int year, int month, int day){
        Query query = FirebaseDatabase.getInstance().getReference("Lists").child(firebaseUser.getUid())
                .orderByChild("date")
                .equalTo(year+"/"+month+"/"+day);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                estimateModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    EstimateModel estimateModel = dataSnapshot.getValue(EstimateModel.class);
                    estimateModels.add(estimateModel);
                }
                savedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}