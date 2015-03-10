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

public class NumberPickerPreference extends DialogPreference {

	public static final int MAX_VALUE = 60;
	public static final int MIN_VALUE = 10;
	public String[] values;

	private NumberPicker picker;
	private int value;

	public NumberPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NumberPickerPreference(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
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

		ArrayList<String> values = new ArrayList<String>();
		for (int i = MIN_VALUE; i <= MAX_VALUE; i += 10) {
			values.add(String.valueOf(i));
		}
		String[] toSend = values.toArray(new String[values.size()]);
		
		picker.setDisplayedValues(toSend);
		

		return dialogView;
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		picker.setMinValue(1);
		picker.setMaxValue(values.length);
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