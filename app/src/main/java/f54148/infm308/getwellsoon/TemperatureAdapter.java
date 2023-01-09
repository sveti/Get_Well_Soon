package f54148.infm308.getwellsoon;

import static f54148.infm308.getwellsoon.Helpers.getStringFromDate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;


public class TemperatureAdapter extends ArrayAdapter<Temperature> {
    SQLightManager sqLightManager;
    Context context;

    public TemperatureAdapter(@NonNull Context context, List<Temperature> resource) {
        super(context, 0, resource);
        sqLightManager = SQLightManager.instanceOfDatabase(context);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Temperature temperature = getItem(position);

        if (convertView == null) {
            ///set View if Not set
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.temperature_cell, parent, false);
        }

        ///bind elements
        TextView temperatureValue = convertView.findViewById(R.id.temperatureValue);
        TextView temperatureDateAdded = convertView.findViewById(R.id.temperatureDateAdded);

        ///set content
        String temp = temperature.getValue().toString() + "°";
        String date = Helpers.getStringFromDate(temperature.getAdded(), new SimpleDateFormat("HH:mm dd.MM.yyyy "));

        temperatureValue.setText(temp);
        temperatureDateAdded.setText(date);

        return convertView;
    }

}
