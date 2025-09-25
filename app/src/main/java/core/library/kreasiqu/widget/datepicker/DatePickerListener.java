package core.library.kreasiqu.widget.datepicker;

public interface DatePickerListener {
  void onPickupDate(String value);

  void onDay(String day);

  void onMonth(String month);

  void onYear(String onYear);
}
