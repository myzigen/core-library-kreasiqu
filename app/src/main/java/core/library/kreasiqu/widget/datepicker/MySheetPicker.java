package core.library.kreasiqu.widget.datepicker;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import core.library.kreasiqu.util.WindowPreferencesManager;
import core.library.kreasiqu.R;

public class MySheetPicker extends BottomSheetDialogFragment {
  private OnDatePickedListener listener;
  private WindowPreferencesManager windowManager;
  private Button btnConfirm, btnCancel;
  private KreasiquDatePicker picker;

  public interface OnDatePickedListener {
    void onDatePicked(String date);
  }

  public void setOnDatePickedListener(OnDatePickedListener l) {
    this.listener = l;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arg2) {
    View view = inflater.inflate(R.layout.kreasiku_sheet_date_picker, container, false);

    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle arg1) {
    super.onViewCreated(view, arg1);
    btnConfirm = view.findViewById(R.id.confirmBtn);
    btnCancel = view.findViewById(R.id.cancelBtn);
    picker = view.findViewById(R.id.datePicker);

    btnCancel.setOnClickListener(v -> dismiss());

    btnConfirm.setOnClickListener(
        v -> {
          String finalValue = picker.getSelectedDate();
          if (listener != null) {
            listener.onDatePicked(finalValue);
          }
          dismiss();
        });

    picker.setOnDatePickerListener(
        new DatePickerListener() {
          @Override
          public void onPickupDate(String value) {}

          @Override
          public void onDay(String day) {}

          @Override
          public void onMonth(String month) {}

          @Override
          public void onYear(String onYear) {}
        });
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), getTheme());

    // Edge-to-edge sebelum show
    windowManager = new WindowPreferencesManager(requireContext());
    windowManager.applyEdgeToEdgePreference(dialog.getWindow());

    dialog.setOnShowListener(
        dialogInterface -> {
          BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
          FrameLayout bottomSheet =
              bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

          if (bottomSheet != null) {
            bottomSheet.setBackgroundResource(core.library.kreasiqu.R.drawable.shape_corners_top);
            // Pasang insets setelah BottomSheet siap

            View root = bottomSheet; // atau binding.getRoot()
            applyInsets(root);

            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(true);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
          }
        });

    return dialog;
  }

  public void applyInsets(View content) {
    ViewCompat.setOnApplyWindowInsetsListener(
        content,
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());

          // Top = status bar, Bottom = paling besar antara ime/nav bar
          int top = systemBars.top;
          int bottom = Math.max(systemBars.bottom, ime.bottom);

          v.setPadding(systemBars.left, top, systemBars.right, bottom);

          return insets;
        });
  }
}
