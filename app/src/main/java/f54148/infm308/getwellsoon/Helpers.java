package f54148.infm308.getwellsoon;

import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helpers {
    public static int extractFomEditTextOrUseDefault(EditText editText, int def) {
        if (editText.getText().toString().equals("")) {
            return def;
        } else {
            return Integer.parseInt(editText.getText().toString());
        }
    }

    public static String getStringFromDate(Date date, DateFormat dateFormat) {
        if (date == null)
            return null;
        return dateFormat.format(date);
    }

    public static Date getDateFromString(String string, DateFormat dateFormat) {

        try {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }

}
