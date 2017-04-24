package qlearning;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

class Plot extends ApplicationFrame {

	public Plot(String title, int[] data, String version) {
		super(title);
		final XYSeries dataSeries = new XYSeries("Qlearning");
		for (int i = 0; i < data.length; i++)
			dataSeries.add(i, data[i]);
		final XYSeriesCollection dataCollection = new XYSeriesCollection(dataSeries);
		final JFreeChart chart = ChartFactory.createXYLineChart(version, "episode number", "number of steps",
				dataCollection, PlotOrientation.VERTICAL, true, true, true);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 10000));
		setContentPane(chartPanel);
	}
}