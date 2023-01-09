package f54148.infm308.getwellsoon;

import android.app.PendingIntent;

import java.util.ArrayList;
import java.util.Date;

public class Medicine {


    private int id;
    private String name;
    private int days;
    private boolean multiple;
    private int timesADay;
    private int hoursBetweenIntakes;
    private Date deleted;
    ///A walkaround so I don't have to deal with one to many relationship in the DB
    private String pendingIntentsIdsString;


    public Medicine(int id, String name, int days, int timesADay, int hoursBetweenIntakes, boolean multiple) {
        this.id = id;
        this.name = name;
        this.days = days;
        this.timesADay = timesADay;
        this.hoursBetweenIntakes = hoursBetweenIntakes;
        deleted = null;
        pendingIntentsIdsString = "";
        this.multiple = multiple;
    }

    public Medicine(int id, String name, int days, int timesADay, int hoursBetweenIntakes, String pendingIntentsIdsString, boolean multiple) {
        this.id = id;
        this.name = name;
        this.days = days;
        this.timesADay = timesADay;
        this.hoursBetweenIntakes = hoursBetweenIntakes;
        this.deleted = null;
        this.pendingIntentsIdsString = pendingIntentsIdsString;
        this.multiple = multiple;
    }

    public Medicine(int id, String name, int days, boolean multiple, int timesADay, int hoursBetweenIntakes, Date deleted, String pendingIntentsIdsString) {
        this.id = id;
        this.name = name;
        this.days = days;
        this.multiple = multiple;
        this.timesADay = timesADay;
        this.hoursBetweenIntakes = hoursBetweenIntakes;
        this.deleted = deleted;
        this.pendingIntentsIdsString = pendingIntentsIdsString;
    }

    public Medicine(String name, int days, int totalIntakesADay, int hoursBetweenIntakes, String pendingIntentsString, boolean multipleTimesADay) {
        this.name = name;
        this.days = days;
        this.multiple = multipleTimesADay;
        this.timesADay = totalIntakesADay;
        this.hoursBetweenIntakes = hoursBetweenIntakes;
        this.deleted = null;
        this.pendingIntentsIdsString = pendingIntentsString;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getTimesADay() {
        return timesADay;
    }

    public void setTimesADay(int timesADay) {
        this.timesADay = timesADay;
    }

    public int getHoursBetweenIntakes() {
        return hoursBetweenIntakes;
    }

    public void setHoursBetweenIntakes(int hoursBetweenIntakes) {
        this.hoursBetweenIntakes = hoursBetweenIntakes;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public String getPendingIntentsIdsString() {
        return pendingIntentsIdsString;
    }

    public void setPendingIntentsIdsString(String pendingIntentsIdsString) {
        this.pendingIntentsIdsString = pendingIntentsIdsString;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", days=" + days +
                ", multiple=" + multiple +
                ", timesADay=" + timesADay +
                ", hoursBetweenIntakes=" + hoursBetweenIntakes +
                ", deleted=" + deleted +
                ", pendingIntentsIdsString=" + pendingIntentsIdsString +
                '}';
    }
}
