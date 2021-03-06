package sk.berops.android.vehiculum.dataModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Currency {
	
	private Unit unit;

	public enum Unit{
		EURO(0, "€", "Euro"), 
		US_DOLLAR(1, "$", "US Dollar"),
		UK_POUND(2, "£", "UK Pound");
		private int id;
		private String unit;	
		private String longUnit;
		Unit(int id, String unit, String longUnit) {
			this.setId(id);
			this.setUnit(unit);
			this.setLongUnit(longUnit);
		}
		
		private static Map<Integer, Unit> idToUnitMapping;

		public static Unit getUnit(int id) {
			if (idToUnitMapping == null) {
				initMapping();
			}
			
			Unit result = null;
			result = idToUnitMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToUnitMapping = new HashMap<Integer, Unit>();
			for (Unit unit : values()) {
				idToUnitMapping.put(unit.id, unit);
			}
		}
	
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getLongUnit() {
			return longUnit;
		}
		public void setLongUnit(String longUnit) {
			this.longUnit = longUnit;
		}
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public static double convertToSI(double cost, Unit currency) {
		return convertToSI(cost, currency, new Date());
	}
	
	public static double convertToSI(double cost, Unit currency, Date eventDate) {
		return convert(cost, currency, Unit.EURO, eventDate);
	}
	
	public static double convert(double cost, Unit fromCurrency, Unit toCurrency) {
		return convert(cost, fromCurrency, toCurrency, new Date());
	}
	
	public static double convert(double cost, Unit fromCurrency, Unit toCurrency, Date eventDate) {
		return cost;
	}
}
