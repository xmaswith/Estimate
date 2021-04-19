package com.pri.estimate.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog;
import com.pri.estimate.R;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements SupportedDatePickerDialog.OnDateSetListener{

    private static final String TAG = "DatePickerFragment";
    final Calendar c = Calendar.getInstance();

    EstimateFragment estimateFragment;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        estimateFragment = new EstimateFragment();
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        SupportedDatePickerDialog pickerDialog = new SupportedDatePickerDialog(getActivity(), R.style.SpinnerDatePickerDialogTheme,
                this, year, month, day);
        pickerDialog.setCanceledOnTouchOutside(true);

        return pickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String strDate = year + "/" + (month + 1) + "/" + dayOfMonth;
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                new Intent().putExtra("selectedDate", strDate)
        );
    }


}