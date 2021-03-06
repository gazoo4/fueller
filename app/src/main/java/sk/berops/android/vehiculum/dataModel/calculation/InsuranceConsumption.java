package sk.berops.android.vehiculum.dataModel.calculation;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.TreeMap;

import sk.berops.android.vehiculum.dataModel.expense.InsuranceEntry;

public class InsuranceConsumption extends Consumption {
	private double totalInsuranceCost;
	private double averageInsuranceCost;
	
	private TreeMap<InsuranceEntry.Type, Double> totalCostPerInsuranceType;
	private TreeMap<InsuranceEntry.Type, Double> averageCostPerInsuranceType;
	
	public InsuranceConsumption() {
		totalInsuranceCost = 0.0;
		averageInsuranceCost = 0.0;
		
		totalCostPerInsuranceType = new TreeMap<InsuranceEntry.Type, Double>();
		averageCostPerInsuranceType = new TreeMap<InsuranceEntry.Type, Double>();
	}

	@Override
	public void reloadChartData(int data) {
		// We're on level 1. For anything smaller than level 1, we should call our parent class for the data generation instead
		if (data < 1) {
			super.reloadChartData(data);
			return;
		}
		pieChartVals = new ArrayList<>();
		pieChartColors = new ArrayList<>();

		for (InsuranceEntry.Type t : InsuranceEntry.Type.values()) {
			if (getTotalCostPerInsuranceType().get(t) == null) {
				continue;
			}
			double value = getTotalCostPerInsuranceType().get(t);
			pieChartVals.add(new PieEntry((float) value, t.getType(), t.getId()));
			pieChartColors.add(t.getColor());
		}
	}

	@Override
	public double getLabelValue(int depth) {
		if (depth < 1) {
			return super.getLabelValue(depth);
		}
		return getTotalInsuranceCost();
	}

	public double getTotalInsuranceCost() {
		return totalInsuranceCost;
	}

	public void setTotalInsuranceCost(double totalInsuranceCost) {
		this.totalInsuranceCost = totalInsuranceCost;
	}

	public double getAverageInsuranceCost() {
		return averageInsuranceCost;
	}

	public void setAverageInsuranceCost(double averageInsuranceCost) {
		this.averageInsuranceCost = averageInsuranceCost;
	}

	public TreeMap<InsuranceEntry.Type, Double> getTotalCostPerInsuranceType() {
		return totalCostPerInsuranceType;
	}

	public void setTotalCostPerInsuranceType(TreeMap<InsuranceEntry.Type, Double> totalCostPerInsuranceType) {
		this.totalCostPerInsuranceType = totalCostPerInsuranceType;
	}

	public TreeMap<InsuranceEntry.Type, Double> getAverageCostPerInsuranceType() {
		return averageCostPerInsuranceType;
	}

	public void setAverageCostPerInsuranceType(TreeMap<InsuranceEntry.Type, Double> averageCostPerInsuranceType) {
		this.averageCostPerInsuranceType = averageCostPerInsuranceType;
	}
}
