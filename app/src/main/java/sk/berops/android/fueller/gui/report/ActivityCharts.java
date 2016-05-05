package sk.berops.android.fueller.gui.report;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

import java.util.ArrayList;
import java.util.HashMap;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;
import sk.berops.android.fueller.dataModel.expense.History;
import sk.berops.android.fueller.engine.calculation.ChartInterpolator;
import sk.berops.android.fueller.engine.charts.HistoryViewData;
import sk.berops.android.fueller.gui.MainActivity;

public class ActivityCharts extends Activity {
	
	private int resolution = 10; //TODO this should go to settings
	private boolean splineInterpolation = true; //TODO this should go to settings
	private Car car = MainActivity.garage.getActiveCar();
	private History history = car.getHistory();
	private FuelType fuelType = history.getFuellingEntries().getLast().getFuelType();
	
	//http://android-graphview.org/#api
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charts);
		attachGuiObjects();
		generateChart(car.getInitialMileage(), car.getCurrentMileage());
	}
	
	public void attachGuiObjects() {
	}
	
	private void generateChart(double domainStart, double domainEnd) {
		GraphView graphView = new LineGraphView(this, "Consumption History");
		int dataSize = (int) (domainEnd - domainStart) + 1;
		HashMap<FuelType, ArrayList<HistoryViewData>> graphDataMap = new HashMap<FuelType, ArrayList<HistoryViewData>>(); 
		
		// initialize HistoryViewData for all fuel types
		for (FuelType type : FuelType.values()) {
			graphDataMap.put(type, new ArrayList<HistoryViewData>());
		}
		
		// fill-in the HistoryViewData with all the average consumptions
		for (FuellingEntry e : history.getFuellingEntries()) {
			FuelType type = e.getFuelType();
			double xValue = e.getMileage();
			double yValue = e.getFuelConsumption().getMovingAveragePerFuelType().get(type).doubleValue();
			// the first consumption cannot be calculated and is always 0
			// we don't really expect to have another 0.0 consumption (that means we didn't refuel at all, right?)
			// if (yValue == 0.0) continue;
			// TODO: this would make sense, however the chart is shifted away from 0-axis and doesn't look nice
			// to be reconsidered once the chart can be position correctly
			graphDataMap.get(type).add(new HistoryViewData(xValue, yValue));
		}
		
		// remove those HistoryViewData which are empty (as there was no re-fuelling of this type)
		for (FuelType type : FuelType.values()) {
			if (graphDataMap.get(type).size() < 3) {
				graphDataMap.remove(type);
				continue;
			}
		}
		
		HistoryViewData[] data = new HistoryViewData[0];
		GraphViewSeriesStyle style;
		GraphViewSeries series;
		int viewportThreshold = 10; //TODO add this to settings
		if (splineInterpolation) viewportThreshold *= resolution;
		// add all the data series to the chart
		for (FuelType type : graphDataMap.keySet()) {
			data = graphDataMap.get(type).toArray(new HistoryViewData[0]);
			// apply spline interpolation if needed
			if (splineInterpolation) {
				data = ChartInterpolator.applySpline(data, resolution);
			}
			style = new GraphViewSeriesStyle(type.getColor(), 4);
			series = new GraphViewSeries(type.getType(), style, data);
			graphView.addSeries(series);
		}
		
		if (data.length > viewportThreshold) {
			int start = (int) data[data.length - viewportThreshold].getX();
			int size = (int) data[data.length - 1].getX() - start;
			System.out.println(""+ start +", "+ size);
			graphView.setViewPort(start, size);
		}
		
		graphView.setBackgroundColor(Color.BLACK);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.GRAY);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.GRAY);
		graphView.getGraphViewStyle().setGridColor(Color.GRAY);
		graphView.getGraphViewStyle().setTextSize(20);
		graphView.setScrollable(true);
		graphView.setScalable(true);
		graphView.setShowLegend(true);
		graphView.setLegendAlign(LegendAlign.BOTTOM);
		graphView.setLegendWidth(200);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_charts_layout);
		layout.addView(graphView);
	}
}