package sk.berops.android.fueller.dataModel.calculation;

import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;

public class Consumption {
	private double totalCostAllEntries;										//cumulated cost across all entries
	private TreeMap<ExpenseType, Double> totalCostPerEntryType;				//cumulated cost within the entries
	private double averageCostAllEntries;									//average cost of all entries per distance
	private TreeMap<ExpenseType, Double> averageCostPerEntryType;			//average cost within the entry type per distance
	private ExpenseType lastEntryType;
	
	public Consumption() {
		totalCostAllEntries = 0.0;
		totalCostPerEntryType = new TreeMap<ExpenseType, Double>();
		averageCostAllEntries = 0.0;
		averageCostPerEntryType = new TreeMap<ExpenseType, Double>();
		lastEntryType = ExpenseType.FUEL;
	}

	public double getTotalCost() {
		return totalCostAllEntries;
	}

	public void setTotalCost(double totalCost) {
		this.totalCostAllEntries = totalCost;
	}

	public TreeMap<ExpenseType, Double> getTotalCostPerEntryType() {
		return totalCostPerEntryType;
	}

	public void setTotalCostPerEntryType(
			TreeMap<ExpenseType, Double> totalCostPerEntryType) {
		this.totalCostPerEntryType = totalCostPerEntryType;
	}

	public double getAverageCost() {
		return averageCostAllEntries;
	}

	public void setAverageCost(double averageCost) {
		this.averageCostAllEntries = averageCost;
	}

	public TreeMap<ExpenseType, Double> getAverageCostPerEntryType() {
		return averageCostPerEntryType;
	}

	public void setAverageCostPerEntryType(
			TreeMap<ExpenseType, Double> averageCostPerEntryType) {
		this.averageCostPerEntryType = averageCostPerEntryType;
	}

	public ExpenseType getLastEntryType() {
		return lastEntryType;
	}

	public void setLastEntryType(ExpenseType lastEntryType) {
		this.lastEntryType = lastEntryType;
	}
}
