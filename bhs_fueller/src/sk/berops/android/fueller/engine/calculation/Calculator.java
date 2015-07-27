package sk.berops.android.fueller.engine.calculation;

import sk.berops.android.fueller.dataModel.expense.History;

public class Calculator {
	public static void calculateAll(History history) {
		GenericCalculator.calculate(history.getEntries());
		FuellingCalculator.calculate(history.getFuellingEntries());
		MaintenanceCalculator.calculate(history.getMaintenanceEntries());
	}
}
