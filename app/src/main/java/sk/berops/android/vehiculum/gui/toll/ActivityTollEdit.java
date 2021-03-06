package sk.berops.android.vehiculum.gui.toll;

import android.content.Intent;
import android.os.Bundle;

import sk.berops.android.vehiculum.dataModel.expense.TollEntry;
import sk.berops.android.vehiculum.gui.MainActivity;

public class ActivityTollEdit extends ActivityTollAdd {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		int dynamicID = intent.getIntExtra("dynamicID", -1);
		tollEntry = (TollEntry) MainActivity.garage.getActiveCar().getHistory().getEntry(dynamicID);
		entry = tollEntry;
		editMode = true;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		spinnerTollType.setSelection(tollEntry.getType().getId());
	}
}
