package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import java.util.UUID;
import androidx.fragment.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity {
    
    private static final String EXTRA_CRIME_ID = "com.example.criminalintent.crime_id";

    @Override
    public Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        if (crimeId == null) {
            crimeId = UUID.randomUUID();
        }
        return CrimeFragment.newInstance(crimeId);
    }

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
}
