package com.example.criminalintent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab instance;
    private final List<Crime> crimes;

    private CrimeLab() {
        crimes = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setDate(new Date());
            crime.setSolved(i % 2 == 0);
            crime.setRequiresPolice(i % 3 == 0);
            crimes.add(crime);
        }
    }

    public static CrimeLab getInstance() {
        if (instance == null) {
            instance = new CrimeLab();
        }
        return instance;
    }

    public static List<Crime> getCrimes() {
        return getInstance().crimes;
    }

    public static Crime getCrime(UUID id) {
        for (Crime crime : getInstance().crimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    public static void addCrime(Crime crime) {
        getInstance().crimes.add(crime);
    }

    public static void saveCrime(Crime crime) {
        CrimeLab lab = getInstance();
        for (int i = 0; i < lab.crimes.size(); i++) {
            if (lab.crimes.get(i).getId().equals(crime.getId())) {
                lab.crimes.set(i, crime);
                return;
            }
        }
        lab.crimes.add(crime);
    }
}
