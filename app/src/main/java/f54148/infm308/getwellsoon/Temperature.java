package f54148.infm308.getwellsoon;

import java.util.Date;

public class Temperature {
    private int id;
    private Double value;
    private Date added;

    public Temperature(int id, Double value, Date added) {
        this.id = id;
        this.value = value;
        this.added = added;
    }

    public Temperature(Double value, Date added) {

        this.value = value;
        this.added = added;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "id=" + id +
                ", value=" + value +
                ", added=" + added +
                '}';
    }
}
