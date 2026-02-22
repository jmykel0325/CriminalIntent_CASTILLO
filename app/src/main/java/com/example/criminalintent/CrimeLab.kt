package com.example.criminalintent

import java.util.UUID

object CrimeLab {
    private val crimes: MutableList<Crime> = mutableListOf()

    init {
        for (i in 0 until 100) {
            crimes += Crime(
                title = "Crime #$i",
                isSolved = i % 2 == 0,
                requiresPolice = i % 5 == 0,
            )
        }
    }

    fun getCrimes(): List<Crime> = crimes

    fun getCrime(id: UUID): Crime? = crimes.find { it.id == id }

    fun addCrime(crime: Crime) {
        crimes += crime
    }
}
