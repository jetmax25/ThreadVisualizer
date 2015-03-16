import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class VisualGUI {
	
	XYSeriesCollection dataset;
	JFreeChart chart;
	ChartPanel chartPanel;
	XYSeries[] seriesArray;
	JCheckBox[] checkboxes;
	JFrame jframe;
	GridBagConstraints gc;
	
	public VisualGUI(){
		// create a dataset...
					dataset = new XYSeriesCollection();			

					// create a chart...
					chart = ChartFactory.createXYLineChart("CPU Usage", "Time", "Percentage", dataset, PlotOrientation.VERTICAL, true, true, false);
					// create a panel to put the chart in
					chartPanel = new ChartPanel(chart);
					
					
					seriesArray = new XYSeries[getNumberOfThreads()];
					for(int i = 0; i<seriesArray.length; i++){
						seriesArray[i] = new XYSeries("Thread" + Integer.toString(i+1));
						dataset.addSeries(seriesArray[i]);
					}
					
					
					chartPanel.setVisible(true);
						
					
					//creating JButtons for JPanel 1
					JButton button1 = new JButton("CPU Visualizer");
					JButton button2 = new JButton("Memory Visualizer");
					JButton button3 = new JButton("Thread Sleep Time");
					JButton button4 = new JButton("Thread Critical Sections");
					
								
					button1.setMaximumSize(new Dimension(200, 200));
					button2.setMaximumSize(new Dimension(200, 200));
					button3.setMaximumSize(new Dimension(200, 200));
					button4.setMaximumSize(new Dimension(200, 200));
					
					
					
					
					
					//creating JPanels
					JPanel jpanel1 = new JPanel();
					jpanel1.setLayout(new BoxLayout(jpanel1, BoxLayout.Y_AXIS));
					
					JPanel jpanel2 = new JPanel();
					JPanel jpanel3 = new JPanel();			
					
					//adding buttons to JPanel1
					jpanel1.add(button1);
					jpanel1.add(button2);
					jpanel1.add(button3);
					jpanel1.add(button4);
					
					jpanel1.setVisible(true);
					
					
					//adding the chartPanel with the JFreeChart inside to JPanel2
					jpanel2.add(chartPanel);
					jpanel2.setVisible(true);
					
					//adding checkboxes to jpanel3
					int numThreads = getNumberOfThreads();
					checkboxes = new JCheckBox[numThreads];
					
					int i;
					for(i=0; i<numThreads; i++){
						checkboxes[i] = new JCheckBox("Thread " + Integer.toString(i+1), true);
						checkboxes[i].addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								//remove data series here
								JCheckBox thisBox = (JCheckBox) e.getSource();
								if(thisBox.isSelected() == false){
									removeSeries(thisBox.getText());
								}
								else{
									addSeries(thisBox.getText());
								}
							}
						});
						jpanel3.add(checkboxes[i]);
					}
					jpanel3.setVisible(true);
					
					
					
					//Adding components to JFrame using GridBagLayout manager
					jframe = new JFrame("Concurrent Visualizer");
					jframe.setLayout(new GridBagLayout());
					jframe.setResizable(false);
					jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					gc = new GridBagConstraints();
					gc.gridx = 0;
					gc.gridy = 0;
					gc.gridwidth = 1;
					gc.gridheight = 1;
					gc.weightx = 10;
					gc.weighty = 10;
					gc.insets = new Insets(0, 0, 0, 0);
					gc.anchor = GridBagConstraints.FIRST_LINE_START;
					gc.fill = GridBagConstraints.BOTH;
					
					
					gc.gridheight = 3;
					gc.gridwidth = 1;
					jframe.add(jpanel1, gc);
					
					
					gc.gridx = 1;
					gc.gridy = 0;
					gc.gridheight = 2;
					gc.gridwidth = 2;
					jframe.add(jpanel2, gc);
					
					gc.gridx = 1;
					gc.gridy = 2;
					gc.gridheight = 1;
					gc.gridwidth = 2;
					jframe.add(jpanel3, gc);
					
					
					jframe.setSize(900, 600);
					jframe.setVisible(true);
					
					
					
					
					
					int counter = 0;
					while(counter < 100){
						for(i=0; i<seriesArray.length; i++){
							seriesArray[i].add(counter, returnRandom());
						}
						counter++;
						try{Thread.sleep(1000);}
						catch(InterruptedException e){

						}
					}
					
					
	}
	
	public static int returnRandom(){
		Random rn = new Random();
		int answer = rn.nextInt(100) + 1;
		return answer;
	}
	
	public static int getNumberOfThreads(){
		return 3;
	}
	
	public void removeSeries(String string){		
		if(string == null)
			return;
		
		int i=1;
		while(i<100){
			if(string.endsWith(Integer.toString(i))){
				//dataset.removeSeries(0);
				XYPlot plot = (XYPlot) chart.getPlot();
				XYItemRenderer renderer = plot.getRenderer();
				renderer.setSeriesVisible(i-1, false);
				
				return;
			}
			i++;
		}
	}
	
	public void addSeries(String string){		
		if(string == null)
			return;
		
		int i=1;
		while(i<100){
			if(string.endsWith(Integer.toString(i))){
				//dataset.removeSeries(0);
				XYPlot plot = (XYPlot) chart.getPlot();
				XYItemRenderer renderer = plot.getRenderer();
				renderer.setSeriesVisible(i-1, true);
				
				return;
			}
			i++;
		}
	}
	
}