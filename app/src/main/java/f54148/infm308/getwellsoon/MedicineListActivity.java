package f54148.infm308.getwellsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Objects;

public class MedicineListActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_medicine_list);

        SQLightManager sqLightManager = SQLightManager.instanceOfDatabase(this);

        listView = findViewById(R.id.medicineList);
        MedicineAdapter medicineAdapter = new MedicineAdapter(getApplicationContext(), sqLightManager.loadMedicines());
        listView.setAdapter(medicineAdapter);

    }

    public MedicineListActivity getPackageContext() {
        return MedicineListActivity.this;
    }
}