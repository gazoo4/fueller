package sk.berops.android.fueller.gui.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.expense.ServiceEntry;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityEntryGenericAdd;
import sk.berops.android.fueller.gui.common.UtilsActivity;

public class ActivityServiceAdd extends ActivityEntryGenericAdd {
	protected Spinner spinnerServiceType;
	
	protected ServiceEntry serviceEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_service);

		if (serviceEntry == null) {
			serviceEntry = new ServiceEntry();
			serviceEntry.setExpenseType(ExpenseType.SERVICE);
		}

		super.entry = (Entry) this.serviceEntry;
		super.editMode = editMode;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void attachGuiObjects() {
		textViewDisplayDate = (TextView) findViewById(R.id.activity_service_date_text);
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_service_distance_unit);
		
		editTextMileage = (EditText) findViewById(R.id.activity_service_mileage);
		editTextCost = (EditText) findViewById(R.id.activity_service_cost);
		editTextComment = (EditText) findViewById(R.id.activity_service_comment);
		
		spinnerCurrency = (Spinner) findViewById(R.id.activity_service_currency);
		spinnerServiceType = (Spinner) findViewById(R.id.activity_service_type);
	}
	
	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		UtilsActivity.styleSpinner(spinnerServiceType, this, R.array.activity_service_type);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
	}
	
	@Override
	protected void updateFields() throws FieldsEmptyException {
		super.updateFields();
		updateType();
	}
	
	private void updateType() {
		ServiceEntry.Type type;
		
		type = ServiceEntry.Type.getType(spinnerServiceType.getSelectedItemPosition());
		serviceEntry.setType(type);
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_service_button_commit:
			try {
				super.saveFieldsAndPersist(view);
				startActivity(new Intent(this, MainActivity.class));
			} catch (FieldsEmptyException ex) {
				ex.throwAlert();
			}
			break;
		}
	}
}