package sk.berops.android.vehiculum.engine.calculation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import sk.berops.android.vehiculum.dataModel.calculation.FuelConsumption;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry.FuelType;

public class FuellingCalculator {
	//TODO: add to settings including the checks (see below the if clausules)
	public static final int movingPrecision = 6;
	public static final int floatingPrecision = 6;
	
	public static void calculate(LinkedList<FuellingEntry> fuellingEntries) {
		FuellingEntry entryFirst = null;
		FuellingEntry entryLast = new FuellingEntry();

		double totalQuantity = 0.0;
		int fuellingCount = 0;
		double totalFuelCost = 0.0;
		double averageFuelPrice = 0.0;
		
		boolean movingEligible = true;
		boolean floatingEligible = true;
		
		TreeMap<FuelType, FuellingEntry> entryFirstPerFuelType = new TreeMap<FuelType, FuellingEntry>();
		TreeMap<FuelType, FuellingEntry> entryLastPerFuelType = new TreeMap<FuelType, FuellingEntry>();
		
		TreeMap<FuelType, Double> distanceSum = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> quantitySum = new TreeMap<FuelType, Double>();
		
		TreeMap<FuelType, LinkedList<Double>> movingQuantity = new TreeMap<FuelType, LinkedList<Double>>();
		TreeMap<FuelType, LinkedList<Double>> movingDistance = new TreeMap<FuelType, LinkedList<Double>>();
		
		TreeMap<FuelType, ArrayList<Double>> floatingValues = new TreeMap<FuelType, ArrayList<Double>>();
		
		TreeMap<FuelType, Double> totalQuantityPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Integer> fuellingCountPerFuelType = new TreeMap<FuelType, Integer>();
		TreeMap<FuelType, Double> averagePerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> movingAveragePerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> floatingAveragePerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> mileageSinceFirstRefuelPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> mileageSinceLastRefuelPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> totalFuelCostPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> averageFuelCostPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> costSinceLastRefuelPerFuelType = new TreeMap<FuelType, Double>();
		TreeMap<FuelType, Double> averageFuelPricePerFuelType = new TreeMap<FuelType, Double>();
		
		if (movingPrecision < 0) {
			//TODO: these checks to be moved to the settings
			movingEligible = false;
		}
		
		if (movingPrecision < 4) {
			floatingEligible = false;
		}
		
		for (FuellingEntry e : fuellingEntries) {
			FuelType fuelType = e.getFuelType();
			
			if (fuellingCountPerFuelType.get(fuelType) == null) {
				totalQuantityPerFuelType.put(fuelType, 0.0);
				fuellingCountPerFuelType.put(fuelType, 0);
				averagePerFuelType.put(fuelType, 0.0);
				movingAveragePerFuelType.put(fuelType, 0.0);
				floatingAveragePerFuelType.put(fuelType, 0.0);
				mileageSinceFirstRefuelPerFuelType.put(fuelType, 0.0);
				mileageSinceLastRefuelPerFuelType.put(fuelType, 0.0);
				totalFuelCostPerFuelType.put(fuelType, 0.0);
				totalQuantityPerFuelType.put(fuelType, 0.0);
				averageFuelCostPerFuelType.put(fuelType, 0.0);
				costSinceLastRefuelPerFuelType.put(fuelType, 0.0);
				averageFuelPricePerFuelType.put(fuelType, 0.0);
				distanceSum.put(fuelType, 0.0);
				quantitySum.put(fuelType, 0.0);
				movingQuantity.put(fuelType,new LinkedList<Double>());
				movingDistance.put(fuelType,new LinkedList<Double>());
				floatingValues.put(fuelType, new ArrayList<Double>());
			}
			
			
			FuelConsumption consumption = (FuelConsumption) e.getConsumption();
					
			consumption.setLastRefuelType(fuelType);
			
			totalQuantity += e.getFuelQuantitySI();
			consumption.setTotalQuantity(totalQuantity);
			
			double typeTotal = 0.0;
			typeTotal = totalQuantityPerFuelType.get(fuelType);
			typeTotal += e.getFuelQuantitySI();
			totalQuantityPerFuelType.put(fuelType, typeTotal);
			TreeMap<FuelType, Double> totalQuantityPerFuelTypeCopy = new TreeMap<FuelType, Double>(totalQuantityPerFuelType);
			consumption.setTotalQuantityPerFuelType(totalQuantityPerFuelTypeCopy);
			
			consumption.setFuellingCount(++fuellingCount);
			
			int typeCount = fuellingCountPerFuelType.get(fuelType);
			fuellingCountPerFuelType.put(fuelType, ++typeCount);
			TreeMap<FuelType, Integer> fuellingCountPerFuelTypeCopy = new TreeMap<FuelType, Integer>(fuellingCountPerFuelType);
			consumption.setFuellingCountPerFuelType(fuellingCountPerFuelTypeCopy);
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				consumption.setAverageSinceLast(0.0);
			} else {
				double mileageLast = entryLastPerFuelType.get(fuelType).getMileageSI();
				double averageSinceLast = e.getFuelQuantity() / (e.getMileageSI() - mileageLast) * 100;
				consumption.setAverageSinceLast(averageSinceLast);
			}
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				averagePerFuelType.put(fuelType, 0.0);
			} else {
				double quantity = consumption.getTotalQuantityPerFuelType().get(fuelType) - entryFirstPerFuelType.get(fuelType).getFuelQuantitySI();
				double distance = e.getMileageSI() - entryFirstPerFuelType.get(fuelType).getMileageSI();
				averagePerFuelType.put(fuelType, quantity / distance * 100);
			}
			TreeMap<FuelType, Double> averagePerFuelTypeCopy = new TreeMap<FuelType, Double>(averagePerFuelType);
			consumption.setAveragePerFuelType(averagePerFuelTypeCopy);
			
			if (fuellingCount == 1) {
				consumption.setGrandAverage(0.0);
			} else {
				double grandAverage = 0.0;
				for (FuelType t : averagePerFuelType.keySet()) {
					grandAverage += averagePerFuelType.get(t);
				}
				consumption.setGrandAverage(grandAverage);
			}
			
			if (movingEligible) {
				if (movingQuantity.get(fuelType).size() != 0) {
					quantitySum.put(fuelType, quantitySum.get(fuelType) + e.getFuelQuantitySI());
				}
				
				movingQuantity.get(fuelType).add(Double.valueOf(e.getFuelQuantitySI()));
				movingDistance.get(fuelType).add(Double.valueOf(e.getMileageSI()));
				
				if (movingQuantity.get(fuelType).size() > (movingPrecision + 1)) {
					quantitySum.put(fuelType, quantitySum.get(fuelType) - movingQuantity.get(fuelType).getFirst().doubleValue());
					movingQuantity.get(fuelType).removeFirst();
					movingDistance.get(fuelType).removeFirst();
				}
				
				if (movingQuantity.get(fuelType).size() > 1) {
					double distance = movingDistance.get(fuelType).getLast().doubleValue() - movingDistance.get(fuelType).getFirst().doubleValue();
					distanceSum.put(fuelType, distance);
					double movingAverageType = quantitySum.get(fuelType) / distanceSum.get(fuelType) * 100;
					movingAveragePerFuelType.put(fuelType, movingAverageType);
				}
				TreeMap<FuelType, Double> movingAveragePerFuelTypeCopy = new TreeMap<FuelType, Double>(movingAveragePerFuelType);
				consumption.setMovingAveragePerFuelType(movingAveragePerFuelTypeCopy);
			}
			
			if (floatingEligible) {
				ArrayList<Double> values = floatingValues.get(fuelType);
				values.add(Double.valueOf(consumption.getAverageSinceLast()));
				if (values.size() > floatingPrecision + 1) {
					values.remove(0);
				}
				values = new ArrayList<Double>(values); // when cutting min/max, not to overwrite the array...
				
				if (values.size() >= 4) {
					//cut min & max
					double min = Double.MAX_VALUE;
					double max = Double.MIN_VALUE;
					int minIndex = -1;
					int maxIndex = -1;
					for (int i = 0; i < values.size() - 1; i++) {
						if (values.get(i).doubleValue() >= max) {
							max = values.get(i).doubleValue();
							maxIndex = i;
						}
						if (values.get(i).doubleValue() < min) {
							min = values.get(i).doubleValue();
							minIndex = i;
						}
					}
					if (minIndex >= 0) {
						values.remove(minIndex);
					}
					if (maxIndex >= 0) {
						values.remove(maxIndex);
					}
				}
				
				double sum = 0.0;
				for (Double d : values) {
					sum += d.doubleValue();
				}
				floatingAveragePerFuelType.put(fuelType, sum / values.size());
				
				TreeMap<FuelType, Double> floatingAveragePerFuelTypeCopy = new TreeMap<FuelType, Double>(floatingAveragePerFuelType);
				consumption.setFloatingAveragePerFuelType(floatingAveragePerFuelTypeCopy);
			}
			
			if (fuellingCount == 1) {
				consumption.setMileageSinceFirstRefuel(0.0);
			} else {
				double mileage = e.getMileageSI() - entryFirst.getMileageSI();
				consumption.setMileageSinceFirstRefuel(mileage);
			}
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				mileageSinceFirstRefuelPerFuelType.put(fuelType, 0.0);
			} else {
				double mileage = e.getMileageSI() - entryFirstPerFuelType.get(fuelType).getMileageSI();
				mileageSinceFirstRefuelPerFuelType.put(fuelType, mileage);
			}
			TreeMap<FuelType, Double> mileageSinceFirstRefuelPerFuelTypeCopy = new TreeMap<FuelType, Double>(mileageSinceFirstRefuelPerFuelType);
			consumption.setMileageSinceFirstRefuelPerFuelType(mileageSinceFirstRefuelPerFuelTypeCopy);
			
			if (fuellingCount == 1) {
				consumption.setMileageSinceLastRefuel(0.0);
			} else {
				double mileage = e.getMileageSI() - entryLast.getMileageSI();
				consumption.setMileageSinceLastRefuel(mileage);
			}
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				mileageSinceLastRefuelPerFuelType.put(fuelType, 0.0);
			} else {
				double mileage = e.getMileageSI() - entryLastPerFuelType.get(fuelType).getMileageSI();
				mileageSinceLastRefuelPerFuelType.put(fuelType, mileage);
			}
			TreeMap<FuelType, Double> mileageSinceLastRefuelPerFuelTypeCopy = new TreeMap<FuelType, Double>(mileageSinceLastRefuelPerFuelType);
			consumption.setMileageSinceLastRefuelPerFuelType(mileageSinceLastRefuelPerFuelTypeCopy);
			
			totalFuelCost += e.getCost();
			consumption.setTotalFuelCost(totalFuelCost);
			
			double totalFuelCostType = totalFuelCostPerFuelType.get(fuelType);
			totalFuelCostType += e.getCost();
			totalFuelCostPerFuelType.put(fuelType, totalFuelCostType);
			TreeMap<FuelType, Double> totalFuelCostPerFuelTypeCopy = new TreeMap<FuelType, Double>(totalFuelCostPerFuelType);
			consumption.setTotalFuelCostPerFuelType(totalFuelCostPerFuelTypeCopy);
			
			double averageFuelCost;
			if (fuellingCount == 1) {
				averageFuelCost = 0.0;
			} else {
				double fuelCost = totalFuelCost - entryFirst.getCost();
				double mileage = e.getMileageSI() - entryFirst.getMileageSI();
				averageFuelCost = fuelCost / mileage;
			}
			consumption.setAverageFuelCost(averageFuelCost);
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				averageFuelCostPerFuelType.put(fuelType, 0.0);
			} else {
				double fuelCost = totalFuelCostPerFuelType.get(fuelType) - entryFirstPerFuelType.get(fuelType).getCost();
				double mileage = e.getMileageSI() - entryFirstPerFuelType.get(fuelType).getMileageSI();
				double averageFuelCostType = fuelCost / mileage;
				averageFuelCostPerFuelType.put(fuelType, averageFuelCostType);
			}
			TreeMap<FuelType, Double> averageFuelCostPerFuelTypeCopy = new TreeMap<FuelType, Double>(averageFuelCostPerFuelType);
			consumption.setAverageFuelCostPerFuelType(averageFuelCostPerFuelTypeCopy);
			
			if (fuellingCount == 1) {
				consumption.setCostSinceLastRefuel(0.0);
			} else {
				double mileage = e.getMileageSI() - entryLast.getMileageSI();
				double costSinceLastRefuel = e.getCost() / mileage;
				consumption.setCostSinceLastRefuel(costSinceLastRefuel);
			}
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				costSinceLastRefuelPerFuelType.put(fuelType, 0.0);
			} else {
				double mileage = e.getMileageSI() - entryLastPerFuelType.get(fuelType).getMileageSI();
				double costSinceLastRefuelType = e.getCost() / mileage;
				costSinceLastRefuelPerFuelType.put(fuelType, costSinceLastRefuelType);
			}
			TreeMap<FuelType, Double> costSinceLastRefuelPerFuelTypeCopy = new TreeMap<FuelType, Double>(costSinceLastRefuelPerFuelType);
			consumption.setCostSinceLastRefuelPerFuelType(costSinceLastRefuelPerFuelTypeCopy);
			
			if (fuellingCount == 1) {
				averageFuelPrice = e.getFuelPrice();
			} else {
				double totalPrice = averageFuelPrice * (fuellingCount - 1);
				averageFuelPrice = (totalPrice + e.getFuelPrice()) / fuellingCount;
			}
			consumption.setAverageFuelPrice(averageFuelPrice);
			
			if (fuellingCountPerFuelType.get(fuelType) == 1) {
				averageFuelPricePerFuelType.put(fuelType, e.getFuelPrice());
			} else {
				double totalPriceType = averageFuelPricePerFuelType.get(fuelType) * (fuellingCountPerFuelType.get(fuelType) - 1);
				double averageFuelPriceType = (totalPriceType + e.getFuelPrice()) / fuellingCountPerFuelType.get(fuelType);
				averageFuelPricePerFuelType.put(fuelType, averageFuelPriceType);
			}
			TreeMap<FuelType, Double> averageFuelPricePerFuelTypeCopy = new TreeMap<FuelType, Double>(averageFuelPricePerFuelType);
			consumption.setAverageFuelPricePerFuelType(averageFuelPricePerFuelTypeCopy);
			
			if (entryFirst == null) {
				entryFirst = e;
			}
			if (entryFirstPerFuelType.get(fuelType) == null) {
				entryFirstPerFuelType.put(fuelType, e);
			}
			entryLast = e;
			entryLastPerFuelType.put(fuelType, e);
		}
	}
}
