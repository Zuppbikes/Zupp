package in.zuppbikes.activity.customer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener mListener;
    private long mTimeInMillis;

    public void setDate(long iMillis) {
        this.mTimeInMillis = iMillis;
    }

    public void setCallback(DatePickerDialog.OnDateSetListener iListener) {
        mListener = iListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year;
        int month;
        int day;
        if (mTimeInMillis == 0) {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(mTimeInMillis);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog aDialog = new DatePickerDialog(getActivity(), mListener, year, month, day);
        aDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        // Create a new instance of DatePickerDialog and return it
        return aDialog;
    }
}