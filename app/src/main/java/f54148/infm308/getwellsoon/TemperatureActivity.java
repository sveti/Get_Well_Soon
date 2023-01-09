package f54148.infm308.getwellsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class TemperatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_temperature);

        Button temperatureListButton = findViewById(R.id.temperatureListButton);
        Button addTemperatureButton = findViewById(R.id.addTemperatureButton);

        temperatureListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new TemperatureListFragment());
            }
        });

        addTemperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AddTemperatureFragment());
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.frameLayout, new TemperatureListFragment()).commit();
        }
    }


    private void replaceFragment(Fragment fragment) {

        getSupportFragmentManager().
                beginTransaction().replace(R.id.frameLayout, fragment).commit();

    }
}