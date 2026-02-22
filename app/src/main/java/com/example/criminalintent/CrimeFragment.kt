package com.example.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.util.UUID

class CrimeFragment : Fragment() {

    private lateinit var crime: Crime

    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val crimeId = requireArguments().getSerializable(ARG_CRIME_ID, UUID::class.java) ?: UUID.randomUUID()
        val existingCrime = CrimeLab.getCrime(crimeId)
        if (existingCrime != null) {
            crime = existingCrime
        } else {
            crime = Crime(id = crimeId)
        }

        childFragmentManager.setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE,
            this,
        ) { _, bundle ->
            val selectedDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE, java.util.Date::class.java)
            if (selectedDate != null) {
                crime.date = selectedDate
                updateDate()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        titleField = view.findViewById(R.id.crime_title)
        dateButton = view.findViewById(R.id.crime_date)
        solvedCheckBox = view.findViewById(R.id.crime_solved)
        saveButton = view.findViewById(R.id.crime_save)

        titleField.setText(crime.title)
        titleField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = s?.toString().orEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        solvedCheckBox.isChecked = crime.isSolved
        solvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            crime.isSolved = isChecked
        }

        dateButton.setOnClickListener {
            DatePickerFragment
                .newInstance(crime.date)
                .show(childFragmentManager, DIALOG_DATE)
        }
        updateDate()

        saveButton.setOnClickListener {
            CrimeLab.saveCrime(crime)
            requireActivity().finish()
        }

        return view
    }

    private fun updateDate() {
        dateButton.text = crime.date.toString()
    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"

        fun newInstance(crimeId: java.util.UUID): CrimeFragment {
            return CrimeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CRIME_ID, crimeId)
                }
            }
        }
    }
}
