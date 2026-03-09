package com.example.criminalintent;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        return DatePickerFragment.newInstance(date);
    }
}
