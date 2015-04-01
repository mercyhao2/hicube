package cloudteam.hicube.utils;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class OtherUtil {
    public static ProgressDialog pd;
    public static boolean isRun;
    public static int stopPDCount = 0;//总数
    public static int stopCount = 0;//已经的总数

    public static void creatPD(Context context, String msg) {
        if (!isRun) {
            pd = ProgressDialog.show(context, "提示", msg);

            isRun = true;
        }
    }

    public static void creatPD(Context context) {
        if (!isRun) {
            pd = ProgressDialog.show(context, "提示", "加载中...");
            isRun = true;
        }
    }

    public static void stopPD() {
        if (pd != null) {
            stopCount += 1;
            if (stopCount >= stopPDCount) {
                pd.dismiss();
                isRun = false;
                stopPDCount = 0;
                stopCount = 0;
            }
        }
    }

    public static Dialog createDatePickDialog(Context context, final TextView view) {

        String str = view.getText().toString();
        Calendar localCalendar = Calendar.getInstance();
        int year = localCalendar.get(Calendar.YEAR);
        int month = localCalendar.get(Calendar.MONTH);
        int date = localCalendar.get(Calendar.DATE);
        if (!(str.equals(""))) {
            String[] arrayOfString = str.split("-");
            year = Integer.parseInt(arrayOfString[0]);
            month = -1 + Integer.parseInt(arrayOfString[1]);
            date = Integer.parseInt(arrayOfString[2]);
        }
        return new DatePickerDialog(context, new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                view.setText(year + "-"
                        + TimeUtil.Timezerofill(Integer.valueOf(month + 1))
                        + "-" + TimeUtil.Timezerofill(Integer.valueOf(date)));
            }
        }, year, month, date);
    }


}
