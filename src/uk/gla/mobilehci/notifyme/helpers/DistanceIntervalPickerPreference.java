package uk.gla.mobilehci.notifyme.helpers;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

public class DistanceIntervalPickerPreference extends DialogPreference {

	public static final int MAX_VALUE = 1000;
	public static final int MIN_VALUE = 50;
	public String[] values;

	private NumberPicker picker;
	private int value;

	public DistanceIntervalPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setSummary("Distance interval for location updates in meters");
	}

	public DistanceIntervalPickerPreference(Context context,
			AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setSummary("Distance interval for location updates in meters");
		
	}

	@Override
	protected View onCreateDialogView() {
		
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;

		picker = new NumberPicker(getContext());
		picker.setLayoutParams(layoutParams);

		FrameLayout dialogView = new FrameLayout(getContext());
		dialogView.addView(picker);
		
		return dialogView;
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		ArrayList<String> values = new ArrayList<String>();
		for (int i = MIN_VALUE; i <= MAX_VALUE; i += 50) {
			values.add(String.valueOf(i));
		}
		String[] displayed = new String[values.size()];
		displayed = values.toArray(displayed);

		picker.setDisplayedValues(displayed);

		picker.setMinValue(1);
		picker.setMaxValue(displayed.length);
		picker.setValue(getValue());
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			setValue(picker.getValue());
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInt(index, MIN_VALUE);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		setValue(restorePersistedValue ? getPersistedInt(MIN_VALUE)
				: (Integer) defaultValue);
	}

	public void setValue(int value) {
		this.value = value;
		persistInt(this.value);
	}

	public int getValue() {
		return this.value;
	}

}
