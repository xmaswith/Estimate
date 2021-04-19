package com.pri.estimate.Events;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.pri.estimate.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class SelectCalendar implements DayViewDecorator {

    private final Drawable drawable;

    public SelectCalendar(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.selector_square);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
