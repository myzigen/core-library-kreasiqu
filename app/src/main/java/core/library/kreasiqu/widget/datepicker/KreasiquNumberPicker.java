package core.library.kreasiqu.widget.datepicker;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

public class KreasiquNumberPicker extends NumberPicker {

  private Typeface typeFace;

  public KreasiquNumberPicker(Context context) {
    super(context);
  }

  public KreasiquNumberPicker(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public KreasiquNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void addView(View child) {
    super.addView(child);
    updateView(child);
  }

  @Override
  public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
    super.addView(child, index, params);
    updateView(child);

  }

  @Override
  public void addView(View child, android.view.ViewGroup.LayoutParams params) {
    super.addView(child, params);
    updateView(child);
  }

  public void setTypeFace(Typeface typeFace) {
    this.typeFace = typeFace;
    super.invalidate();
  }

  private void updateView(View view) {
    /*
       if (view instanceof TextView) {
         if (PersianDatePickerDialog.typeFace != null)
           ((TextView) view).setTypeface(PersianDatePickerDialog.typeFace);
       }
    */
  }
}
