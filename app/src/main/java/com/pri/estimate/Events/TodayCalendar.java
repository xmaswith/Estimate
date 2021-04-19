package com.pri.estimate.Events;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.pri.estimate.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class TodayCalendar implements DayViewDecorator {

    private CalendarDay date;

    private final Drawable drawable;

    public TodayCalendar(Activity context) {
        date = CalendarDay.today();
        drawable = context.getResources().getDrawable(R.drawable.button_background);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.setBackgroundDrawable(drawable);
        view.addSpan(new ForegroundColorSpan(Color.BLACK));
    }
}
