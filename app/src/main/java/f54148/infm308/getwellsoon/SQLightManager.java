package f54148.infm308.getwellsoon;

import static f54148.infm308.getwellsoon.Helpers.getDateFromString;
import static f54148.infm308.getwellsoon.Helpers.getStringFromDate;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLightManager extends SQLiteOpenHelper {
    private static SQLightManager manager;

    private static final String DB_NAME = "GetWellSoonDB";
    private static final int DB_VERSION = 1;
    private static final String COUNTER = "Counter";

    private static final String MEDICINE_TABLE = "Medicine";
    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";
    private static final String DAYS_FIELD = "days";
    private static final String MULTIPLE_FIELD = "multiple";
    private static final String TIMES_A_DAY_FIELD = "timesADay";
    private static final String HOURS_BETWEEN_INTAKES_FIELD = "hoursBetweenIntakes";
    private static final String DELETED_FIELD = "deleted";
    private static final String INTENTS_FIELD = "pendingIntents";

    private static final String NOTIFICATION_TABLE = "Notifications";
    private static final String ID_FIELD_NOTIFICATIONS = "id";

    private static final String TEMPERATURE_TABLE = "Temperature";
    private static final String ID_FIELD_TEMPERATURE = "id";
    private static final String VALUE_FIELD = "value";
    private static final String ADDED_FIELD = "added";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public SQLightManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //singleton
    public static SQLightManager instanceOfDatabase(Context context) {
        if (manager == null) {
            manager = new SQLightManager(context);
        }
        return manager;
    }

    @Override

    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        StringBuilder sql;

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(MEDICINE_TABLE)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(NAME_FIELD)
                .append(" TEXT, ")
                .append(DAYS_FIELD)
                .append(" INTEGER, ")
                .append(MULTIPLE_FIELD)
                .append(" INTEGER, ")
                .append(TIMES_A_DAY_FIELD)
                .append(" INTEGER, ")
                .append(HOURS_BETWEEN_INTAKES_FIELD)
                .append(" INTEGER, ")
                .append(DELETED_FIELD)
                .append(" TEXT, ")
                .append(INTENTS_FIELD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(NOTIFICATION_TABLE)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD_NOTIFICATIONS)
                .append(" INT) ");

        sqLiteDatabase.execSQL(sql.toString());

        sql = new StringBuilder("INSERT INTO " + NOTIFICATION_TABLE + "(" + ID_FIELD_NOTIFICATIONS + ") VALUES (1)");
        sqLiteDatabase.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TEMPERATURE_TABLE)
                .append("(")
                .append(ID_FIELD_TEMPERATURE)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(VALUE_FIELD)
                .append(" REAL, ")
                .append(ADDED_FIELD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public void saveMedicineInDB(Medicine medicine) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_FIELD, medicine.getName());
        contentValues.put(DAYS_FIELD, medicine.getDays());
        contentValues.put(MULTIPLE_FIELD, medicine.isMultiple() ? 1 : 0);
        contentValues.put(TIMES_A_DAY_FIELD, medicine.getTimesADay());
        contentValues.put(HOURS_BETWEEN_INTAKES_FIELD, medicine.getHoursBetweenIntakes());
        contentValues.put(DELETED_FIELD, getStringFromDate(medicine.getDeleted(), dateFormat));
        contentValues.put(INTENTS_FIELD, medicine.getPendingIntentsIdsString());

        sqLiteDatabase.insert(MEDICINE_TABLE, null, contentValues);
    }


    public void deleteMedicineFromDB(Medicine medicine) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, medicine.getId());
        contentValues.put(NAME_FIELD, medicine.getName());
        contentValues.put(DAYS_FIELD, medicine.getDays());
        contentValues.put(MULTIPLE_FIELD, medicine.isMultiple() ? 1 : 0);
        contentValues.put(TIMES_A_DAY_FIELD, medicine.getTimesADay());
        contentValues.put(HOURS_BETWEEN_INTAKES_FIELD, medicine.getHoursBetweenIntakes());
        contentValues.put(DELETED_FIELD, getStringFromDate(medicine.getDeleted(), dateFormat));
        contentValues.put(INTENTS_FIELD, medicine.getPendingIntentsIdsString());

        sqLiteDatabase.update(MEDICINE_TABLE, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(medicine.getId())});
    }


    public List<Medicine> loadMedicines() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<Medicine> medicines = new ArrayList<>();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + MEDICINE_TABLE + " WHERE " + DELETED_FIELD + " IS  NULL", null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(0);
                    String name = result.getString(1);
                    int days = result.getInt(2);
                    boolean multiple = result.getInt(3) != 0;
                    int timesADay = result.getInt(4);
                    int hoursBetweenIntakes = result.getInt(5);
                    String stringDeleted = result.getString(6);
                    Date deleted = getDateFromString(stringDeleted, dateFormat);
                    String intentIdsString = result.getString(7);
                    Medicine medicine = new Medicine(id, name, days, multiple, timesADay, hoursBetweenIntakes, deleted, intentIdsString);
                    medicines.add(medicine);

                }
            }
        }

        return medicines;

    }

    public int getNextNotificationId() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + NOTIFICATION_TABLE, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(1);
                    return id;
                }
            }
        }
        return -1;
    }

    public void updateNextNotificationId(int oldId, int newValue) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD_NOTIFICATIONS, newValue);
        sqLiteDatabase.update(NOTIFICATION_TABLE, contentValues, ID_FIELD_NOTIFICATIONS + " =? ", new String[]{String.valueOf(oldId)});
    }

    public void saveTemperatureToDB(Temperature temperature) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(VALUE_FIELD, temperature.getValue());
        contentValues.put(ADDED_FIELD, getStringFromDate(temperature.getAdded(), dateFormat));

        sqLiteDatabase.insert(TEMPERATURE_TABLE, null, contentValues);
    }

    public List<Temperature> loadTemperatures() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<Temperature> temperatures = new ArrayList<>();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TEMPERATURE_TABLE + " ORDER BY " + ADDED_FIELD + " DESC", null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(0);
                    double value = result.getDouble(1);
                    String stringAdded = result.getString(2);
                    Date added = getDateFromString(stringAdded, dateFormat);
                    Temperature temperature = new Temperature(id, value, added);

                    temperatures.add(temperature);

                }
            }
        }

        return temperatures;

    }

}
