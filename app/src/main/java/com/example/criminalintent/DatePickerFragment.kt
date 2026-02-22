package com.example.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.Calendar
import java.util.Date

class DatePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = requireArguments().getSerializable(ARG_DATE, Date::class.java) ?: Date()
        val calendar = Calendar.getInstance().apply { time = date }

        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val resultDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }.time

                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY_DATE,
                    Bundle().apply {
                        putSerializable(BUNDLE_KEY_DATE, resultDate)
                    },
                )
            },
            initialYear,
            initialMonth,
            initialDay,
        )
    }

    companion object {
        const val REQUEST_KEY_DATE = "requestKeyDate"
        const val BUNDLE_KEY_DATE = "bundleKeyDate"

        private const val ARG_DATE = "argDate"

        fun newInstance(date: Date): DatePickerFragment {
            return DatePickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_DATE, date)
                }
            }
        }
    }
}
