package com.example.criminalintent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private Crime crime;
    private EditText titleField;
    private Button dateButton;
    private CheckBox solvedCheckBox;
    private Button saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID) requireArguments().getSerializable(ARG_CRIME_ID);
        if (crimeId == null) {
            crimeId = UUID.randomUUID();
        }

        Crime existingCrime = CrimeLab.getCrime(crimeId);
        if (existingCrime != null) {
            crime = existingCrime;
        } else {
            crime = new Crime(crimeId);
        }

        getChildFragmentManager().setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE,
            this,
            (requestKey, result) -> {
                java.util.Date selectedDate = (java.util.Date) result.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE);
                if (selectedDate != null) {
                    crime.setDate(selectedDate);
                    updateDate();
                }
            }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        titleField = view.findViewById(R.id.crime_title);
        dateButton = view.findViewById(R.id.crime_date);
        solvedCheckBox = view.findViewById(R.id.crime_solved);
        saveButton = view.findViewById(R.id.crime_save);

        titleField.setText(crime.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s != null ? s.toString() : "");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        solvedCheckBox.setChecked(crime.isSolved());
        solvedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> crime.setSolved(isChecked));

        dateButton.setOnClickListener(v -> 
            DatePickerFragment
                .newInstance(crime.getDate())
                .show(getChildFragmentManager(), DIALOG_DATE)
        );
        updateDate();

        saveButton.setOnClickListener(v -> {
            CrimeLab.saveCrime(crime);
            requireActivity().finish();
        });

        return view;
    }

    private void updateDate() {
        dateButton.setText(crime.getDate().toString());
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        CrimeFragment fragment = new CrimeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        fragment.setArguments(args);
        return fragment;
    }
}
