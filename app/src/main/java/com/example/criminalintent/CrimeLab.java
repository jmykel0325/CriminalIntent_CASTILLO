package com.example.criminalintent;

import android.content.Context;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private final List<Crime> mCrimes;

    private CrimeLab() {
        mCrimes = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setDate(new Date());
            crime.setSolved(i % 1 == 0);
            crime.setRequiresPolice(i % 3 == 0);
            mCrimes.add(crime);
        }
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab();
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void saveCrime(Crime crime) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crime.getId())) {
                mCrimes.set(i, crime);
                return;
            }
        }
        mCrimes.add(crime);
    }
}
