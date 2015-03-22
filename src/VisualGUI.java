import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class VisualGUI {
	XYSeriesCollection dataset = new XYSeriesCollection();
	XYSeriesCollection dataset2 = new XYSeriesCollection();
	XYSeriesCollection dataset3 = new XYSeriesCollection();
	static TaskSeriesCollection data4 = new TaskSeriesCollection();
	XYTaskDataset dataset4;
	JFreeChart chart; //CPU usage chart
	JFreeChart chart2; //Memory usage chart
	JFreeChart chart3;
	JFreeChart chart4; //Critical Section chart
	JFreeChart currChart; //currChart keeps track of the chart currently being displayed to the user
	ChartPanel chartPanel;
	ChartPanel chartPanel2;
	ChartPanel chartPanel3;
	ChartPanel chartPanel4;
	XYSeries[] seriesArray;
	XYSeries[] seriesArray2;
	//static TaskSeries[] taskSeriesArray = new TaskSeries[(int) getNumberOfThreads()];
	static ArrayList<TaskSeries> taskSeriesArray = new ArrayList<TaskSeries>();
	JCheckBox[] checkboxes; //array that hold all of the checkboxes
	JFrame jframe;
	GridBagConstraints gc;
	JPanel jpanel1;
	JPanel jpanel2;
	JPanel jpanel3;

	XYPlot plot1;
	XYPlot plot2;
	XYPlot plot3;
	static XYPlot plot4;
	NumberAxis domain1;
	NumberAxis domain2;
	NumberAxis domain3;
	NumberAxis domain4;

	XYBarRenderer renderer4;
	
	static long programStartTime = System.currentTimeMillis();
	
	public static ArrayList<VisualThread> threads = new ArrayList<VisualThread>();
	
	static ArrayList<StoredTask> storedTasks = new ArrayList<StoredTask>();

	protected VisualGUI(){

		
		Thread guiThread = new Thread(new Runnable(){
			public void run(){
				
				System.out.printf("numThreads: %d\n", (int)getNumberOfThreads());												
				
				// create datasets
				//dataset = new XYSeriesCollection();	

				//dataset2 = new XYSeriesCollection();

				//data4 = new TaskSeriesCollection();
				dataset4 = new XYTaskDataset(data4);
				

				// create charts
				chart = ChartFactory.createXYLineChart("CPU Usage", "Time", "Percentage", dataset, PlotOrientation.VERTICAL, true, true, false);
				// create a panel to put the chart in
				chartPanel = new ChartPanel(chart);
				currChart = chart;

				plot1 = (XYPlot) chart.getXYPlot();
				domain1 = (NumberAxis) plot1.getDomainAxis();
				domain1.setRange(0,10);
				
				//This is for chart1 and chart2 ******************************************
				seriesArray = new XYSeries[(int)getNumberOfThreads()];				  //**
				seriesArray2 = new XYSeries[(int) getNumberOfThreads()];			  //**
				for(int i = 0; i<seriesArray.length; i++){                            //**
					seriesArray[i] = new XYSeries("Thread" + Integer.toString(i+1));  //**
					seriesArray2[i] = new XYSeries("Thread" + Integer.toString(2*i)); //**
					dataset.addSeries(seriesArray[i]);								  //**
					dataset2.addSeries(seriesArray2[i]);    						  //**
				}																	  //**
				//************************************************************************
				

				chart2 = ChartFactory.createXYLineChart("Memory Usage", "Time", "Percentage", dataset2, PlotOrientation.VERTICAL, true, true, false);
				plot2 = (XYPlot) chart2.getXYPlot();
				domain2 = (NumberAxis) plot2.getDomainAxis();
				domain2.setRange(0,10);
				
				chart3 = ChartFactory.createScatterPlot("Thread Lifecycle", "Time", "Actions", dataset3);
				String[] chart3AxisLabels = {"interrupted", "Joined", "Created", "Destroyed", "running"};
				SymbolAxis symbolaxis2 = new SymbolAxis("Series", chart3AxisLabels);
				plot3 = chart3.getXYPlot();
				plot3.setRangeAxis(symbolaxis2);
				plot3.setDomainAxis(new NumberAxis("Time (Milliseconds"));
				
				chart4 = ChartFactory.createXYBarChart("Critical Sections", "Thread", false, "Time", dataset4, PlotOrientation.HORIZONTAL, true, true, false);

					
				//create labels for the critical sections chart y-axis
				String[] chart4AxisLabels = new String[(int)getNumberOfThreads()];
				for(int j=0; j<chart4AxisLabels.length; j++){
					chart4AxisLabels[j] = "Thread " + (j+1);
				}
				SymbolAxis symbolaxis = new SymbolAxis("Series", chart4AxisLabels);

				//set the domain and range of the critical sections axes
				plot4 = chart4.getXYPlot();
				plot4.setDomainAxis(symbolaxis);
				plot4.setRangeAxis(new NumberAxis("Time (Milliseconds)"));

				//not entirely sure why we need these three lines but its started working when I put them in
				renderer4 = (XYBarRenderer) plot4.getRenderer();
				renderer4.setUseYInterval(true);
				ChartUtilities.applyCurrentTheme(chart4);
				
				//addStoredTasks();
				
				System.out.println("taskSeriesArray filled");
							

				//creating JButtons for JPanel 1
				JButton button1 = new JButton("CPU Visualizer");
				JButton button2 = new JButton("Memory Visualizer");
				JButton button3 = new JButton("Thread Lifecycle");
				JButton button4 = new JButton("Thread Critical Sections");


				button1.setMaximumSize(new Dimension(200, 200));
				button2.setMaximumSize(new Dimension(200, 200));
				button3.setMaximumSize(new Dimension(200, 200));
				button4.setMaximumSize(new Dimension(200, 200));

				//add action listeners to each button
				button1.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						chartPanel = new ChartPanel(chart);
						jpanel2.removeAll();
						jpanel2.add(chartPanel);
						jpanel2.revalidate();
						jpanel2.repaint();
						currChart = chart;
					}
				});




				button2.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						chartPanel2 = new ChartPanel(chart2);
						jpanel2.removeAll();
						jpanel2.add(chartPanel2);
						jpanel2.revalidate();
						jpanel2.repaint();
						currChart = chart2;
					}
				});


				button3.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						chartPanel3 = new ChartPanel(chart3);
						jpanel2.removeAll();
						jpanel2.add(chartPanel3);
						jpanel2.revalidate();
						jpanel2.repaint();
						currChart = chart3;
					}
				});


				button4.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						chartPanel4 = new ChartPanel(chart4);
						jpanel2.removeAll();
						jpanel2.add(chartPanel4);
						jpanel2.revalidate();
						jpanel2.repaint();
						currChart = chart4;
					}
				});





				//creating JPanels
				jpanel1 = new JPanel();
				jpanel1.setLayout(new BoxLayout(jpanel1, BoxLayout.Y_AXIS));

				jpanel2 = new JPanel();
				jpanel3 = new JPanel();			

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
				int numThreads = (int) getNumberOfThreads();
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


				Thread thread = new Thread(new Runnable(){
					public void run(){
						int counter = 0;
						int i;
						while(counter < 100){
							for(i=0; i<seriesArray.length; i++){
								addCpuUsage(counter, i);
								addMemoryUsage(counter, i);
								if(counter > 10){
									domain1.setRange(counter-10, counter);
									domain2.setRange(counter-10, counter);
								}
							}
							counter++;
							try{Thread.sleep(1000);}
							catch(InterruptedException e){

							}
						}
					}
				});
				thread.run();


			}
		});		

		guiThread.start();
	}


	public void addCpuUsage(int counter, int index){
		seriesArray[index].add(counter, returnRandom());
	}
	
	public void addMemoryUsage(int counter, int index){
		seriesArray2[index].add(counter, (returnRandom()*2)%100);
	}
	
	//This method returns a random integer
	public static int returnRandom(){
		Random rn = new Random();
		int answer = rn.nextInt(100) + 1;
		return answer;
	}


	//This method gets the number of threads from the library and returns it
	private static long getNumberOfThreads(){
		return Analyzer.threadCount();
		//return 3;
	}


	//This method make a certain line on the line chart invisible 
	private void removeSeries(String string){		
		if(string == null)
			return;

		int i=1;
		while(i<100){
			if(string.endsWith(Integer.toString(i))){
				XYPlot plot = (XYPlot) currChart.getPlot();
				XYItemRenderer renderer = plot.getRenderer();
				renderer.setSeriesVisible(i-1, false);

				return;
			}
			i++;
		}
	}


	//This method makes a previously invisible line on the line chart visible again
	private void addSeries(String string){		
		if(string == null)
			return;

		int i=1;
		while(i<100){
			if(string.endsWith(Integer.toString(i))){
				XYPlot plot = (XYPlot) currChart.getPlot();
				XYItemRenderer renderer = plot.getRenderer();
				renderer.setSeriesVisible(i-1, true);

				return;
			}
			i++;
		}
	}
	
	//This method takes in an ActivitySlice as a parameter and sends the data
	//to the charts
	public static void addActivitySlice(ActivitySlice slice){
		System.out.println("Activity Slice: " + slice.getDescription());
		
	}
	
	
	//This method adds a critical section to the critical section bar chart and should be called by
	//the Visualizer class when the Visualizer.leaveCriticalSection() method is used
	public static void addCriticalSection(String criticalSection, long id, long enter, long leave){
		//System.out.println("Critical Section Added");
		Date enterTime = new Date(enter-programStartTime);
		Date exitTime = new Date(leave-programStartTime);
		Task thisTask = new Task(criticalSection, enterTime, exitTime);
		int index = getTaskSeriesID(id);
		//System.out.println(id + " " + enter + " " + leave);
		try{taskSeriesArray.get(index).add(new Task(criticalSection, enterTime, exitTime));}
		catch(NullPointerException n){
			storedTasks.add(new StoredTask(thisTask, index));
		}		
	}
	
	
	public static void addStoredTasks(){
		for(int i=0; i<storedTasks.size(); i++){
			StoredTask thisStoredTask = storedTasks.get(i);
			Task newTask = thisStoredTask.task;
			taskSeriesArray.get(thisStoredTask.index).add(newTask);
		}
	}
	
	//returns -1 if thread with given id does not exist
	public static int getTaskSeriesID(long id){
		for(int i=0; i<threads.size(); i++){
			VisualThread thisThread = threads.get(i);
			if(thisThread.getId() == id)
				return i;
		}
		return -1;
	}
	
	//called when a Thread enters a critical section
	public static void enteringCS(String critalSection, long id, long time)
	{
		
	}
	
	//called when a thread leaves a critical section
	public static void leaveCS(String criticalSection, long id, long enter, long leave)
	{
		
	}

	//This method will be called by the library when the number of threads changes
	public static void threadAdded(VisualThread newThread){
		threads.add(newThread);
		TaskSeries task = new TaskSeries("Thread "+ Long.toString(getTaskSeriesID(newThread.getId())+1));
		taskSeriesArray.add(task);
		data4.add(task);
	}

}
