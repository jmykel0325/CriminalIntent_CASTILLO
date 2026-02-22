package com.example.criminalintent

import java.util.UUID

object CrimeLab {
    private val crimes: MutableList<Crime> = mutableListOf()

    fun getCrimes(): List<Crime> = crimes

    fun getCrime(id: UUID): Crime? = crimes.find { it.id == id }

    fun addCrime(crime: Crime) {
        crimes += crime
    }

    fun saveCrime(crime: Crime) {
        val index = crimes.indexOfFirst { it.id == crime.id }
        if (index >= 0) {
            crimes[index] = crime
        } else {
            crimes += crime
        }
    }
}
