package com.example.criminalintent;

import android.os.Bundle;
import android.view.Menu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView crimeRecyclerView;
    private CrimeAdapter adapter;

    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_POLICE = 1;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(
            new MenuProvider() {
                @Override
                public void onCreateMenu(Menu menu, MenuInflater menuInflater) {
                    menuInflater.inflate(R.menu.fragment_crime_list, menu);
                }

                @Override
                public boolean onMenuItemSelected(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.new_crime) {
                        Crime crime = new Crime();
                        startActivity(CrimeActivity.newIntent(requireContext(), crime.getId()));
                        return true;
                    }
                    return false;
                }
            },
            getViewLifecycleOwner(),
            Lifecycle.State.RESUMED
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        List<Crime> crimes = CrimeLab.getCrimes();
        if (adapter == null) {
            adapter = new CrimeAdapter(crimes);
            crimeRecyclerView.setAdapter(adapter);
        } else {
            adapter.setCrimes(crimes);
            adapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Crime crime;
        private final TextView titleTextView;
        private final TextView dateTextView;
        private final ImageView solvedImageView;

        public CrimeHolder(View view) {
            super(view);
            titleTextView = itemView.findViewById(R.id.crime_title);
            dateTextView = itemView.findViewById(R.id.crime_date);
            solvedImageView = itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime) {
            this.crime = crime;
            titleTextView.setText(crime.getTitle());
            dateTextView.setText(crime.getDate().toString());

            if (crime.isSolved()) {
                solvedImageView.setVisibility(View.VISIBLE);
                solvedImageView.setImageResource(R.drawable.handcuffs);
            } else {
                solvedImageView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), crime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            startActivity(CrimeActivity.newIntent(requireContext(), crime.getId()));
        }
    }

    private class CrimePoliceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Crime crime;
        private final TextView titleTextView;
        private final TextView dateTextView;
        private final Button contactPoliceButton;
        private final ImageView solvedImageView;

        public CrimePoliceHolder(View view) {
            super(view);
            titleTextView = itemView.findViewById(R.id.crime_title);
            dateTextView = itemView.findViewById(R.id.crime_date);
            contactPoliceButton = itemView.findViewById(R.id.contact_police_button);
            solvedImageView = itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime) {
            this.crime = crime;
            titleTextView.setText(crime.getTitle());
            dateTextView.setText(crime.getDate().toString());

            if (crime.isSolved()) {
                solvedImageView.setVisibility(View.VISIBLE);
                solvedImageView.setImageResource(R.drawable.handcuffs);
            } else {
                solvedImageView.setVisibility(View.GONE);
            }

            contactPoliceButton.setOnClickListener(v -> 
                Toast.makeText(
                    getContext(),
                    "Contacting police for " + crime.getTitle(),
                    Toast.LENGTH_SHORT
                ).show()
            );
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), crime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            startActivity(CrimeActivity.newIntent(requireContext(), crime.getId()));
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Crime> crimes;

        public CrimeAdapter(List<Crime> crimes) {
            this.crimes = crimes;
        }

        public void setCrimes(List<Crime> crimes) {
            this.crimes = crimes;
        }

        @Override
        public int getItemCount() {
            return crimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            return crimes.get(position).isRequiresPolice() ? VIEW_TYPE_POLICE : VIEW_TYPE_NORMAL;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            if (viewType == VIEW_TYPE_POLICE) {
                View view = layoutInflater.inflate(R.layout.list_item_crime_police, parent, false);
                return new CrimePoliceHolder(view);
            } else {
                View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
                return new CrimeHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime = crimes.get(position);
            if (holder instanceof CrimePoliceHolder) {
                ((CrimePoliceHolder) holder).bind(crime);
            } else if (holder instanceof CrimeHolder) {
                ((CrimeHolder) holder).bind(crime);
            }
        }
    }
}
