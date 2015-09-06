package sk.berops.android.fueller.gui.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.NoSuchElementException;

import sk.berops.android.fueller.configuration.Preferences;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.dataModel.Record;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.gui.Colors;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public abstract class ActivityEntryGenericAdd extends ActivityExpenseAdd implements DatePickerDialog.OnDateSetListener {

	protected EditText editTextMileage;
	protected EditText editTextCost;
	protected TextView textViewDisplayDate;
	protected TextView textViewDistanceUnit;
	
	protected Entry entry;
	protected boolean editMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.record = (Record) this.entry;
		super.onCreate(savedInstanceState);
		
		updateTextAddEventDate();
	}
	
	@Override
	protected abstract void attachGuiObjects();
	
	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		editTextMileage.setHintTextColor(Colors.LIGHT_GREEN);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		textViewDistanceUnit.setText(car.getDistanceUnit().getUnit());
	}
	
	public void showDatePickerDialog(View v) {
		FragmentDatePicker fragment = new FragmentDatePicker();
		fragment.show(getFragmentManager(), "datePicker");
	}

	private void updateTextAddEventDate() {
		if (entry.getEventDate() == null) {
			entry.setEventDate(Calendar.getInstance().getTime());
		}
		textViewDisplayDate.setText(DateFormat.getDateInstance().format(entry.getEventDate()));
	}
	
	private void updateMileage() throws FieldsEmptyException {
		Car car = MainActivity.garage.getActiveCar();
		double mileage = 0;
		try {
			mileage = Double.parseDouble(editTextMileage.getText().toString());
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(R.string.activity_generic_mileage_hint);
		}
		
		entry.setMileage(mileage);
		car.setCurrentMileage(mileage);
	}
	
	protected void updateCost() throws FieldsEmptyException {
		try {
			Currency.Unit currency = Currency.Unit.getUnit(spinnerCurrency.getSelectedItemPosition());
			entry.setCost(GuiUtils.extractDouble(editTextCost), currency);
		} catch (NumberFormatException ex) {
			throwAlertFieldsEmpty(R.string.activity_generic_cost_hint);
		}
	}
	
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		entry.setEventDate(c.getTime());
		updateTextAddEventDate();
	}
	
	protected void updateFields() throws FieldsEmptyException {
		super.updateFields();
		updateMileage();
		updateCost();
	}
	
	public void saveFieldsAndPersist(View view) throws FieldsEmptyException {
		Car car = MainActivity.garage.getActiveCar();
		entry.setCar(car);
		if (!editMode) {
			car.getHistory().getEntries().add(entry);
		}
		super.saveFieldsAndPersist(view);
	}
}