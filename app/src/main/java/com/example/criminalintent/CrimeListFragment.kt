package com.example.criminalintent

import android.os.Bundle
import android.view.Menu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.fragment_crime_list, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.new_crime -> {
                            val crime = Crime()
                            startActivity(CrimeActivity.newIntent(requireContext(), crime.id))
                            true
                        }

                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val crimes = CrimeLab.getCrimes()
        if (adapter == null) {
            adapter = CrimeAdapter(crimes)
            crimeRecyclerView.adapter = adapter
        } else {
            adapter?.crimes = crimes
            adapter?.notifyDataSetChanged()
        }
    }

    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var crime: Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = crime.title
            dateTextView.text = crime.date.toString()

            solvedImageView.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
            if (crime.isSolved) {
                solvedImageView.setImageResource(R.drawable.handcuffs)
            }
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} clicked!", Toast.LENGTH_SHORT).show()
            startActivity(CrimeActivity.newIntent(requireContext(), crime.id))
        }
    }

    private inner class CrimePoliceHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var crime: Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val contactPoliceButton: Button = itemView.findViewById(R.id.contact_police_button)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = crime.title
            dateTextView.text = crime.date.toString()

            solvedImageView.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
            if (crime.isSolved) {
                solvedImageView.setImageResource(R.drawable.handcuffs)
            }
            contactPoliceButton.setOnClickListener {
                Toast.makeText(
                    context,
                    "Contacting police for ${crime.title}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} clicked!", Toast.LENGTH_SHORT).show()
            startActivity(CrimeActivity.newIntent(requireContext(), crime.id))
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int = crimes.size

        override fun getItemViewType(position: Int): Int {
            return if (crimes[position].requiresPolice) VIEW_TYPE_POLICE else VIEW_TYPE_NORMAL
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return when (viewType) {
                VIEW_TYPE_POLICE -> {
                    val view = layoutInflater.inflate(R.layout.list_item_crime_police, parent, false)
                    CrimePoliceHolder(view)
                }

                else -> {
                    val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                    CrimeHolder(view)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val crime = crimes[position]
            when (holder) {
                is CrimePoliceHolder -> holder.bind(crime)
                is CrimeHolder -> holder.bind(crime)
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_NORMAL = 0
        private const val VIEW_TYPE_POLICE = 1
    }
}
