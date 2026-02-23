package com.example.criminalintent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    public static final String REQUEST_KEY_DATE = "requestKeyDate";
    public static final String BUNDLE_KEY_DATE = "bundleKeyDate";

    private static final String ARG_DATE = "argDate";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) requireArguments().getSerializable(ARG_DATE);
        if (date == null) {
            date = new Date();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(
            requireContext(),
            (view, year, month, dayOfMonth) -> {
                Calendar resultCalendar = Calendar.getInstance();
                resultCalendar.set(Calendar.YEAR, year);
                resultCalendar.set(Calendar.MONTH, month);
                resultCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date resultDate = resultCalendar.getTime();

                Bundle result = new Bundle();
                result.putSerializable(BUNDLE_KEY_DATE, resultDate);
                getParentFragmentManager().setFragmentResult(REQUEST_KEY_DATE, result);
            },
            initialYear,
            initialMonth,
            initialDay
        );
    }

    public static DatePickerFragment newInstance(Date date) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }
}
