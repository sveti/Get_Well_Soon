package f54148.infm308.getwellsoon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TemperatureListFragment extends Fragment {


    public TemperatureListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temperature_list, container, false);

        SQLightManager sqLightManager = SQLightManager.instanceOfDatabase(requireActivity());
        ListView listView = view.findViewById(R.id.temperatureList);
        TemperatureAdapter temperatureAdapter = new TemperatureAdapter(requireActivity().getApplicationContext(), sqLightManager.loadTemperatures());
        listView.setAdapter(temperatureAdapter);

        return view;
    }
}