package com.example.criminalintent

import android.content.Context
import android.content.Intent
import java.util.UUID
import androidx.fragment.app.Fragment

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID, UUID::class.java) ?: UUID.randomUUID()
        return CrimeFragment.newInstance(crimeId)
    }

    companion object {
        private const val EXTRA_CRIME_ID = "com.example.criminalintent.crime_id"

        fun newIntent(packageContext: Context, crimeId: UUID): Intent {
            return Intent(packageContext, CrimeActivity::class.java).apply {
                putExtra(EXTRA_CRIME_ID, crimeId)
            }
        }
    }
}
