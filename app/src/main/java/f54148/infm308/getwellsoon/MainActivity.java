package f54148.infm308.getwellsoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView generatedJokeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //disable dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        generatedJokeText = findViewById(R.id.generatedJokeText);
        generatedJokeText.setVisibility(View.GONE);

        ///set "menu"
        configureAddMedicine();
        configureMedicineList();
        configureTemperature();

        new GetJokeOfTheDay().execute();

    }

    public void configureAddMedicine() {

        Button toAddMedicineActivityButton = findViewById(R.id.toAddMedicineActivity);
        toAddMedicineActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddMedicineActivity();
            }
        });
    }

    public void configureMedicineList() {
        Button toMedicineListButton = findViewById(R.id.toMedicineList);
        toMedicineListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMedicineListActivity();
            }
        });
    }

    public void configureTemperature() {

        Button toTemperaturesButton = findViewById(R.id.toTemperatures);
        toTemperaturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTemperaturesActivity();
            }
        });
    }


    public void openAddMedicineActivity() {
        Intent intent = new Intent(this, AddMedicineActivity.class);
        startActivity(intent);
    }

    public void openMedicineListActivity() {
        Intent intent = new Intent(this, MedicineListActivity.class);
        startActivity(intent);
    }

    public void openTemperaturesActivity() {
        Intent intent = new Intent(this, TemperatureActivity.class);
        startActivity(intent);
    }


    private class GetJokeOfTheDay extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://v2.jokeapi.dev/joke/Any?type=single&lang=en&safe-mode";
            String jsonStr = handler.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting the joke
                    return jsonObj.getString("joke");

                } catch (final JSONException e) {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json грешка: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "В момента не могат да бъдат заредени шеги!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            generatedJokeText.setVisibility(View.VISIBLE);
            generatedJokeText.setText(result);
        }


    }
}