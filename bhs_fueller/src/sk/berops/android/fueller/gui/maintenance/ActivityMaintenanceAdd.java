package sk.berops.android.fueller.gui.maintenance;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.configuration.Preferences;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.FieldsEmptyException;
import sk.berops.android.fueller.dataModel.expense.MaintenanceEntry.Type;
import sk.berops.android.fueller.dataModel.expense.MaintenanceEntry;
import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart;
import sk.berops.android.fueller.gui.Colors;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.ActivityAddEventGeneric;
import sk.berops.android.fueller.gui.common.FragmentEntryEditDelete;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.gui.common.TextFormatter;
import sk.berops.android.fueller.gui.fuelling.ActivityFuellingEdit;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMaintenanceAdd extends ActivityAddEventGeneric implements
		FragmentEntryEditDelete.EntryEditDeleteDialogListener {

	class CostCalculateListener implements TextWatcher, OnItemSelectedListener {

		@Override
		public void afterTextChanged(Editable s) {
			reloadCost();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			reloadCost();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	protected static final int ADD_PART = 1;
	protected static final int EDIT_PART = 2;

	protected int selectedPartPosition;
	protected LinkedList<ReplacementPart> parts;
	PartsAdapter adapter;

	protected ListView listView;
	protected EditText editTextLaborCost;
	protected TextView textViewPartsCost;
	protected TextView textViewTotalCost;
	protected RadioGroup radioGroupType;
	protected Spinner spinnerLaborCostCurrency;

	protected MaintenanceEntry maintenanceEntry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_maintenance);

		if (maintenanceEntry == null) {
			maintenanceEntry = new MaintenanceEntry();
			maintenanceEntry.setExpenseType(ExpenseType.MAINTENANCE);
			maintenanceEntry.setParts(new LinkedList<ReplacementPart>());
		}
		parts = maintenanceEntry.getParts();

		super.entry = (Entry) this.maintenanceEntry;
		super.editMode = editMode;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		textViewDisplayDate = (TextView) findViewById(R.id.activity_maintenance_date_text);
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_maintenance_distance_unit);
		textViewPartsCost = (TextView) findViewById(R.id.activity_maintenance_parts_cost);
		textViewTotalCost = (TextView) findViewById(R.id.activity_maintenance_total_cost);
		editTextMileage = (EditText) findViewById(R.id.activity_maintenance_mileage);
		editTextLaborCost = (EditText) findViewById(R.id.activity_maintenance_labor_cost);
		editTextCost = (EditText) findViewById(R.id.activity_maintenance_cost);
		editTextComment = (EditText) findViewById(R.id.activity_maintenance_comment);

		spinnerCurrency = (Spinner) findViewById(R.id.activity_maintenance_currency);
		spinnerLaborCostCurrency = (Spinner) findViewById(R.id.activity_maintenance_labor_cost_currency);

		radioGroupType = (RadioGroup) findViewById(R.id.activity_maintenance_type);

		listView = (ListView) findViewById(R.id.activity_maintenance_parts_listview);
		adapter = new PartsAdapter(this, parts);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), "Click ListItem Number " + position,
						Toast.LENGTH_LONG).show();
				setSelectedPartPosition(position);
				DialogFragment dialog = new FragmentEntryEditDelete();
				dialog.show(getFragmentManager(), "FragmentEntryEditDelete");
				return true;
			}
		});
	}

	@Override
	protected void styleGuiObjects() {
		super.styleGuiObjects();
		editTextLaborCost.setHintTextColor(Colors.LIGHT_GREEN);

		ArrayAdapter<CharSequence> adapterLaborCostCurrency = ArrayAdapter.createFromResource(this,
				R.array.activity_generic_currency, R.layout.spinner_white);
		adapterLaborCostCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLaborCostCurrency.setAdapter(adapterLaborCostCurrency);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		spinnerCurrency.setSelection(0);

		CostCalculateListener costCalculator = new CostCalculateListener();
		editTextLaborCost.addTextChangedListener(costCalculator);
		spinnerLaborCostCurrency.setOnItemSelectedListener(costCalculator);
		
		reloadCost();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case ADD_PART:
			if (resultCode == RESULT_OK) {
				ReplacementPart result = (ReplacementPart) data.getExtras().getSerializable("part");
				maintenanceEntry.getParts().add(result);
				reloadCost();
			}
			if (resultCode == RESULT_CANCELED) {
				// If no result, no issue
			}
			break;
		case EDIT_PART:
			maintenanceEntry.getParts().remove(selectedPartPosition);
			if (resultCode == RESULT_OK) {
				ReplacementPart result = (ReplacementPart) data.getExtras().getSerializable("part");
				maintenanceEntry.getParts().add(selectedPartPosition, result);
				reloadCost();
			}
			if (resultCode == RESULT_CANCELED) {
				// If no result, no issue
			}
			break;
		}
	}

	protected void reloadCost() {
		double partsCostSI = maintenanceEntry.getPartsCostSI();
		double totalCostSI = 0;

		Currency.Unit currency = Currency.Unit.getUnit(spinnerLaborCostCurrency.getSelectedItemPosition());

		try {
			double laborCost = GuiUtils.extractDouble(editTextLaborCost);
			maintenanceEntry.setLaborCost(laborCost, currency);
		} catch (NumberFormatException ex) {
			// if editTextLaborCost is empty, it should not be an issue
		} finally {
			totalCostSI = partsCostSI + maintenanceEntry.getLaborCostSI();
		}

		double totalCostNative = Currency.convert(totalCostSI, Currency.Unit.getUnit(0), currency,
				entry.getEventDate());
		textViewTotalCost.setText(TextFormatter.format(totalCostNative, "#######.##") + " "
				+ currency.getUnit());
		editTextCost.setText(Double.toString(totalCostNative));
		
		double partsCostNative = Currency.convert(partsCostSI, Currency.Unit.getUnit(0), currency, entry.getEventDate());
		textViewPartsCost.setText(TextFormatter.format(partsCostNative, "#######.##") + " "
				+ currency.getUnit());
		
		adapter.notifyDataSetChanged();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_maintenance_button_add:
			startActivityForResult(new Intent(this, ActivityPartAdd.class), ADD_PART);
			break;
		case R.id.activity_maintenance_button_delete:
			break;
		case R.id.activity_maintenance_button_commit:
			try {
				saveEntry();
				super.saveFieldsAndPersist(view);
				startActivity(new Intent(this, MainActivity.class));
			} catch (FieldsEmptyException ex) {
				ex.throwAlert();
			}
			break;
		}
	}

	private void saveEntry() throws FieldsEmptyException {
		updateType();
	}

	private void updateType() throws FieldsEmptyException {
		MaintenanceEntry.Type type;
		switch (radioGroupType.getCheckedRadioButtonId()) {
		case R.id.activity_maintenance_type_planned:
			type = Type.PLANNED;
			break;
		case R.id.activity_maintenance_type_unplanned:
			type = Type.UNPLANNED;
			break;
		case R.id.activity_maintenance_type_accident:
			type = Type.ACCIDENT_REPAIR;
			break;
		default:
			throwAlertFieldsEmpty(R.string.activity_maintenance_type_hint);
			return;
		}
		maintenanceEntry.setType(type);
	}

	@Override
	public void OnDialogEditClick(DialogFragment dialog) {
		int position = parts.size() - 1 - getSelectedPartPosition();
		System.out.println("Editing entry in position " + getSelectedPartPosition());
		Intent newIntent = new Intent(this, ActivityPartEdit.class);
		newIntent.putExtra("ReplacementPart", maintenanceEntry.getParts().get(position));
		startActivityForResult(newIntent, EDIT_PART);
	}

	@Override
	public void OnDialogDeleteClick(DialogFragment dialog) {
		ReplacementPart part = parts.get(parts.size() - 1 - getSelectedPartPosition());
		System.out.println("Removing part with ID " + (parts.size() - 1 - getSelectedPartPosition()));  
		parts.remove(part);
		MainActivity.dataHandler.persistGarage(MainActivity.garage);
		adapter.notifyDataSetChanged();
		reloadCost();
	}

	public int getSelectedPartPosition() {
		return selectedPartPosition;
	}

	public void setSelectedPartPosition(int selectedPartPosition) {
		this.selectedPartPosition = selectedPartPosition;
	}
}