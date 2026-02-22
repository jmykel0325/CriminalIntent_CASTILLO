package com.example.criminalintent

import androidx.fragment.app.Fragment

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment = CrimeFragment()
}
