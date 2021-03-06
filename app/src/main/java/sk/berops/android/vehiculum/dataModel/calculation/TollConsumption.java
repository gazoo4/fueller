package sk.berops.android.vehiculum.dataModel.calculation;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.TreeMap;

import sk.berops.android.vehiculum.dataModel.expense.TollEntry;

public class TollConsumption extends Consumption {
	private double totalTollCost;
	private double averageTollCost;
	
	private TreeMap<TollEntry.Type, Double> totalCostPerTollType;
	private TreeMap<TollEntry.Type, Double> averageCostPerTollType;
	
	public TollConsumption() {
		totalTollCost = 0.0;
		averageTollCost = 0.0;
		
		totalCostPerTollType = new TreeMap<TollEntry.Type, Double>();
		averageCostPerTollType = new TreeMap<TollEntry.Type, Double>();
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

		for (TollEntry.Type t : TollEntry.Type.values()) {
			if (getTotalCostPerTollType().get(t) == null) {
				continue;
			}
			double value = getTotalCostPerTollType().get(t);
			pieChartVals.add(new PieEntry((float) value, t.getType(), t.getId()));
			pieChartColors.add(t.getColor());
		}
	}

	@Override
	public double getLabelValue(int depth) {
		if (depth < 1) {
			return super.getLabelValue(depth);
		}
		return getTotalTollCost();
	}

	public double getTotalTollCost() {
		return totalTollCost;
	}

	public void setTotalTollCost(double totalTollCost) {
		this.totalTollCost = totalTollCost;
	}

	public double getAverageTollCost() {
		return averageTollCost;
	}

	public void setAverageTollCost(double averageTollCost) {
		this.averageTollCost = averageTollCost;
	}

	public TreeMap<TollEntry.Type, Double> getTotalCostPerTollType() {
		return totalCostPerTollType;
	}

	public void setTotalCostPerTollType(TreeMap<TollEntry.Type, Double> totalCostPerTollType) {
		this.totalCostPerTollType = totalCostPerTollType;
	}

	public TreeMap<TollEntry.Type, Double> getAverageCostPerTollType() {
		return averageCostPerTollType;
	}

	public void setAverageCostPerTollType(TreeMap<TollEntry.Type, Double> averageCostPerTollType) {
		this.averageCostPerTollType = averageCostPerTollType;
	}
}
