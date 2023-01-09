package f54148.infm308.getwellsoon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.Objects;

public class AddTemperatureFragment extends Fragment {

    public AddTemperatureFragment() {
        // Required empty public constructor
    }

    EditText temperature;
    Button submitNewTemperature;
    SQLightManager sqLightManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_temperature, container, false);

        temperature = view.findViewById(R.id.temperature);
        submitNewTemperature = view.findViewById(R.id.submitNewTemperature);
        sqLightManager = SQLightManager.instanceOfDatabase(requireActivity());

        submitNewTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double inputTemperature;
                if (temperature.getText().toString().equals("")) {
                    inputTemperature = 37.5;
                } else {
                    inputTemperature = Double.parseDouble(temperature.getText().toString());
                }
                
                if (inputTemperature < 35 || inputTemperature > 42) {
                    Toast.makeText(requireActivity(), R.string.invalidData, Toast.LENGTH_SHORT).show();
                    return;
                }

                Temperature temperature = new Temperature(inputTemperature, new Date());
                sqLightManager.saveTemperatureToDB(temperature);
                Toast.makeText(requireActivity(), R.string.temperatureSet, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}