package core.library.kreasiqu.widget.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.annotation.ColorInt;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import core.library.kreasiqu.R;

public class KreasiquDatePicker extends LinearLayout {
  private KreasiquNumberPicker yearNumberPicker;
  private KreasiquNumberPicker monthNumberPicker;
  private KreasiquNumberPicker dayNumberPicker;
  private TextView dateTv, monthTv, yearTv;
  private String monthValue, dayValue, yearValue;
  private DatePickerListener listener;
  private boolean isLeapYear = false;

  private int dividerColor;

  public void setOnDatePickerListener(DatePickerListener l) {
    this.listener = l;
  }

  public KreasiquDatePicker(Context context) {
    this(context, null);
  }

  public KreasiquDatePicker(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public KreasiquDatePicker(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    // ----- inflate view
    View view = LayoutInflater.from(context).inflate(R.layout.widget_date_picker, this, true);

    dateTv = view.findViewById(R.id.showDateTv);
    monthTv = view.findViewById(R.id.showMonthTv);
    yearTv = view.findViewById(R.id.showYearTv);

    yearNumberPicker = view.findViewById(R.id.yearNumberPicker);
    monthNumberPicker = view.findViewById(R.id.monthNumberPicker);
    dayNumberPicker = view.findViewById(R.id.dayNumberPicker);

    updateViewData();
  }

  private void updateViewData() {
    if (dividerColor > 0) {
      setDividerColor(yearNumberPicker, dividerColor);
      setDividerColor(monthNumberPicker, dividerColor);
      setDividerColor(dayNumberPicker, dividerColor);
    }

    Locale locale = Locale.getDefault();
    String[] months = new DateFormatSymbols(locale).getMonths();

    // DateFormatSymbols.getMonths() return 13 elemen di beberapa locale (index 12 kosong),
    // jadi aman potong jadi 12:
    String[] months12 = Arrays.copyOf(months, 12);

    Calendar calendar = Calendar.getInstance();
    int yearNow = calendar.get(Calendar.YEAR);
    int monthNow = calendar.get(Calendar.MONTH); // 0–11
    int dayNow = calendar.get(Calendar.DAY_OF_MONTH);

    // ----- Tanggal
    dayNumberPicker.setMinValue(1);
    dayNumberPicker.setMaxValue(31);
    dayNumberPicker.setWrapSelectorWheel(true);

    // ----- Bulan
    monthNumberPicker.setDisplayedValues(null); // reset dulu
    monthNumberPicker.setMinValue(0);
    monthNumberPicker.setMaxValue(months12.length - 1);
    monthNumberPicker.setDisplayedValues(months12);
    monthNumberPicker.setWrapSelectorWheel(true);

    // ----- Tahun
    yearNumberPicker.setMinValue(2020);
    yearNumberPicker.setMaxValue(yearNow);
    yearNumberPicker.setWrapSelectorWheel(true);

    // >>> Set nilai default sesuai tanggal sekarang
    yearNumberPicker.setValue(yearNow);
    monthNumberPicker.setValue(monthNow);
    dayNumberPicker.setValue(dayNow);

    // >>> Simpan juga di variabel supaya getSelectedDate() langsung terisi
    yearValue = String.valueOf(yearNow);
    monthValue = months[monthNow];
    dayValue = String.valueOf(dayNow);

    dateTv.setText(dayValue);
    monthTv.setText(monthValue);
    yearTv.setText(yearValue);

    // listener untuk day
    dayNumberPicker.setOnValueChangedListener(
        (picker, oldVal, newVal) -> {
          dayValue = String.valueOf(newVal);
          dateTv.setText(dayValue);
          if (listener != null) listener.onDay(dayValue);
        });

    // listener untuk month
    monthNumberPicker.setOnValueChangedListener(
        (picker, oldVal, newVal) -> {
          setLastDay(newVal);
          if (yearValue != null) leapYearCheck(Integer.parseInt(yearValue));
          monthValue = months[newVal];
          monthTv.setText(monthValue);
          if (listener != null) listener.onMonth(monthValue);
        });

    // listener untuk year
    yearNumberPicker.setOnValueChangedListener(
        (picker, oldVal, newVal) -> {
          leapYearCheck(newVal);
          yearValue = String.valueOf(newVal);
          yearTv.setText(yearValue);
          if (listener != null) listener.onYear(yearValue);
        });

    if (listener != null) {
      listener.onPickupDate(getSelectedDate());
    }
  }

  public void setDividerColor(@ColorInt int color) {
    this.dividerColor = color;
    updateViewData();
  }

  private void setDividerColor(NumberPicker picker, int color) {
    java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
    for (java.lang.reflect.Field pf : pickerFields) {
      if (pf.getName().equals("mSelectionDivider")) {
        pf.setAccessible(true);
        try {
          ColorDrawable colorDrawable = new ColorDrawable(color);
          pf.set(picker, colorDrawable);
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (Resources.NotFoundException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
        break;
      }
    }
  }

  private void setLastDay(int newVal) {
    switch (newVal) {
      case 1:
        if (isLeapYear) {
          setFebruaryValue(29);
        } else {
          setFebruaryValue(28);
        }
        break;
      case 3:
        setMaxValueMonth();
        break;
      case 5:
        setMaxValueMonth();
        break;
      case 8:
        setMaxValueMonth();
        break;
      case 10:
        setMaxValueMonth();
        break;
      default:
        dayNumberPicker.setMaxValue(31);
        break;
    }
  }

  private void setFebruaryValue(int value) {
    dayNumberPicker.setMaxValue(value);
    if (dayValue != null) {
      if (Integer.parseInt(dayValue) == value + 1) {
        dayNumberPicker.setValue(value);
      }
    }
  }

  private void setMaxValueMonth() {
    dayNumberPicker.setMaxValue(30);
    if (dayValue == null) return;
    if (Integer.parseInt(dayValue) == 31) {
      dayNumberPicker.setValue(30);
    }
  }

  private void leapYearCheck(int year) {
    if (year % 4 == 0) {
      if (year % 100 == 0) {
        if (year % 400 == 0) {
          isLeapYear = true;
          if (monthValue != null) if (monthValue.equals("Februari")) setFebruaryValue(29);
        } else {
          isLeapYear = false;
          if (monthValue != null) if (monthValue.equals("Februari")) setFebruaryValue(28);
        }
      } else {
        isLeapYear = true;
        if (monthValue != null) if (monthValue.equals("Februari")) setFebruaryValue(29);
      }
    } else {
      isLeapYear = false;
      if (monthValue != null) if (monthValue.equals("Februari")) setFebruaryValue(28);
    }
  }

  public String getSelectedDate() {
    return (dayValue == null ? "" : dayValue)
        + " "
        + (monthValue == null ? "" : monthValue)
        + " "
        + (yearValue == null ? "" : yearValue);
  }

  public String getDay() {
    return dayValue;
  }

  public String getMonth() {
    return monthValue;
  }

  public String getYear() {
    return yearValue;
  }
}
