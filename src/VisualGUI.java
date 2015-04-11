import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

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
	static ConcurrentLinkedQueue<CriticalSectionQObject> criticalSectionQueue = new ConcurrentLinkedQueue<CriticalSectionQObject>();
	static ConcurrentLinkedQueue<ActivitySlice> activitySliceQueue = new ConcurrentLinkedQueue<ActivitySlice>();
	static ConcurrentLinkedQueue<SystemSlice> systemSliceQueue = new ConcurrentLinkedQueue<SystemSlice>();
	
	static XYSeriesCollection dataset = new XYSeriesCollection();
	static XYSeriesCollection dataset2 = new XYSeriesCollection();
	static XYSeriesCollection dataset3 = new XYSeriesCollection();
	static TaskSeriesCollection data4 = new TaskSeriesCollection();
	XYTaskDataset dataset4;
	JFreeChart chart; //CPU usage chart
	JFreeChart chart2; //Memory usage chart
	JFreeChart chart3;
	JFreeChart chart4; //Critical Section chart
	static JFreeChart currChart; //currChart keeps track of the chart currently being displayed to the user
	ChartPanel chartPanel;
	ChartPanel chartPanel2;
	ChartPanel chartPanel3;
	ChartPanel chartPanel4;
	//XYSeries[] seriesArray;
	static ArrayList<XYSeries> seriesArraylist = new ArrayList<XYSeries>();
	//static XYSeries overallSeries1 = new XYSeries("Overall");
	//XYSeries[] seriesArray2;
	static ArrayList<XYSeries> seriesArraylist2 = new ArrayList<XYSeries>();
	static ArrayList<XYSeries> seriesArraylist3 = new ArrayList<XYSeries>();
	static ArrayList<TaskSeries> taskSeriesArray = new ArrayList<TaskSeries>();
	//static JCheckBox[] checkboxes; //array that hold all of the checkboxes
	
	static ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
	
	JFrame jframe;
	GridBagConstraints gc;
	JPanel jpanel1;
	JPanel jpanel2;
	static JPanel jpanel3 = new JPanel();

	XYPlot plot1;
	XYPlot plot2;
	XYPlot plot3;
	static XYPlot plot4;
	
	static ArrayList<String> chart4AxisLabels = new ArrayList<String>();
	
	NumberAxis domain1;
	NumberAxis domain2;
	NumberAxis domain3;
	NumberAxis domain4;
	
	static Task curTask;

	static XYBarRenderer renderer4;
	
	static long programStartTime = System.currentTimeMillis();
	
	public static ArrayList<VisualThread> threads = new ArrayList<VisualThread>();
		
	protected VisualGUI(){

		
		Thread guiThread = new Thread(new Runnable(){
			public void run(){
				
				Analyzer.startActivityNotifications();
	
				
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
				plot1.setBackgroundPaint(Color.BLACK);

//				//This is for chart1 and chart2 ******************************************
//				seriesArray = new XYSeries[(int)getNumberOfThreads()];				  //**
//				seriesArray2 = new XYSeries[(int) getNumberOfThreads()];			  //**
//				for(int i = 0; i<seriesArray.length; i++){                            //**
//					seriesArray[i] = new XYSeries("Thread" + Integer.toString(i+1));  //**
//					seriesArray2[i] = new XYSeries("Thread" + Integer.toString(2*i)); //**
//					dataset.addSeries(seriesArray[i]);								  //**
//					dataset2.addSeries(seriesArray2[i]);    						  //**
//				}																	  //**
//				//************************************************************************
				//dataset.addSeries(overallSeries1);

				chart2 = ChartFactory.createXYLineChart("Memory Usage", "Time", "Percentage", dataset2, PlotOrientation.VERTICAL, true, true, false);
				plot2 = (XYPlot) chart2.getXYPlot();
				domain2 = (NumberAxis) plot2.getDomainAxis();
				domain2.setRange(0,10);
				plot2.setBackgroundPaint(Color.BLACK);
				
				chart3 = ChartFactory.createScatterPlot("Thread Lifecycle", "Time", "Actions", dataset3);
				String[] chart3AxisLabels = {"Interrupted", "Joined", "Created", "Destroyed", "Started"};
				SymbolAxis symbolaxis2 = new SymbolAxis("Series", chart3AxisLabels);
				plot3 = chart3.getXYPlot();
				plot3.setRangeAxis(symbolaxis2);
				plot3.setDomainAxis(new NumberAxis("Time (Milliseconds)"));

				
				chart4 = ChartFactory.createXYBarChart("Critical Sections", "Thread", false, "Time", dataset4, PlotOrientation.HORIZONTAL, true, true, false);
				
				//set the domain and range of the critical sections axes
				plot4 = chart4.getXYPlot();
				plot4.setRangeAxis(new NumberAxis("Time (Milliseconds)"));
				plot4.setRenderer(new CriticalSectionsBarRenderer());
			

				//not entirely sure why we need these three lines but its started working when I put them in
				renderer4 = (XYBarRenderer) plot4.getRenderer();
				renderer4.setUseYInterval(true);
				ChartUtilities.applyCurrentTheme(chart4);
				
				
											
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
				//jpanel3 = new JPanel();			

				//adding buttons to JPanel1
				jpanel1.add(button1);
				jpanel1.add(button2);
				jpanel1.add(button3);
				jpanel1.add(button4);

				jpanel1.setVisible(true);


				//adding the chartPanel with the JFreeChart inside to JPanel2
				jpanel2.add(chartPanel);
				jpanel2.setVisible(true);

//				//adding checkboxes to jpanel3
//				int numThreads = (int) getNumberOfThreads();
//				checkboxes = new JCheckBox[numThreads];
//
//				int i;
//				for(i=0; i<numThreads; i++){
//					checkboxes[i] = new JCheckBox("Thread " + Integer.toString(i+1), true);
//					checkboxes[i].addActionListener(new ActionListener(){
//						public void actionPerformed(ActionEvent e){
//							//remove data series here
//							JCheckBox thisBox = (JCheckBox) e.getSource();
//							if(thisBox.isSelected() == false){
//								setSeriesInvisible(thisBox.getText());
//							}
//							else{
//								setSeriesVisible(thisBox.getText());
//							}
//						}
//					});
//					jpanel3.add(checkboxes[i]);
//				}
				
				
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
				
				
				Analyzer.startDataCollection();
				
				Thread dequeuerThread = new Thread(new Runnable(){
					public void run(){
						while(true){
							while(criticalSectionQueue.isEmpty() == true){}
							for(int i=0; i<criticalSectionQueue.size(); i++){
								CriticalSectionQObject qObject = criticalSectionQueue.poll();
								taskSeriesArray.get(qObject.index).add(qObject.task);
							}
						}
					}
				});
				dequeuerThread.start();


				Thread thread = new Thread(new Runnable(){
					public void run(){
						int counter = 0;
						int i;
						while(true){
							for(i=0; i<seriesArraylist.size(); i++){
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
				thread.start();


			}
		});		

		guiThread.start();
	}


	public void addCpuUsage(int counter, int index){
		//seriesArray[index].add(counter, returnRandom());
		XYSeries series = seriesArraylist.get(index);
		series.add(counter, returnRandom());
		
	}
	
	public void addMemoryUsage(int counter, int index){
		//seriesArray2[index].add(counter, (returnRandom()*2)%100);
		XYSeries series = seriesArraylist2.get(index);
		series.add(counter, (returnRandom()*2)%100);
	}
	
	//This method returns a random integer
	public static int returnRandom(){
		Random rn = new Random();
		int answer = rn.nextInt(40) + 1;
		return answer;
	}


	//This method gets the number of threads from the library and returns it
	private static long getNumberOfThreads(){
		return Analyzer.threadCount();
		//return 3;
	}
	
	private static void createCheckbox(int threadNumber){
		JCheckBox newCheckBox = new JCheckBox("Thread " + threadNumber, true);
		
		
		newCheckBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//remove data series here
				JCheckBox thisBox = (JCheckBox) e.getSource();
				if(thisBox.isSelected() == false){
					setSeriesInvisible(thisBox.getText());
				}
				else{
					setSeriesVisible(thisBox.getText());
				}
			}
		});
		
		newCheckBox.setVisible(true);
		jpanel3.add(newCheckBox);
		jpanel3.revalidate();
		jpanel3.repaint();
	}
	
	
	private static void createChart4AxisLabels(int threadNumber){
		chart4AxisLabels.add("Thread" + threadNumber);
		String[] axisLabels = new String[chart4AxisLabels.size()];
		for(int i=0; i<axisLabels.length; i++){
			axisLabels[i] = chart4AxisLabels.get(i);
		}
		
		SymbolAxis symbolAxis = new SymbolAxis("Series", axisLabels);
		try{plot4.setDomainAxis(symbolAxis);}
		catch(NullPointerException e){}
	}


	//This method make a certain line on the line chart invisible 
	private static void setSeriesInvisible(String string){		
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
	private static void setSeriesVisible(String string){		
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
	
	
	public static void addSystemSlice(SystemSlice slice){
		//System.out.println(slice.toString());
		//overallSeries1.add(slice.getTime() - programStartTime, slice.getCpu());
	}
	
	
	//This method takes in an ActivitySlice as a parameter and sends the data
	//to the charts
	public static void addActivitySlice(ActivitySlice slice){
		//System.out.println("Activity Slice: " + slice.getDescription());
		if(slice.getDescription().equals("Initialized")){
			XYSeries series = seriesArraylist3.get(getTaskSeriesID(slice.getThread()));
			series.add(slice.getTime() - programStartTime, 2D);
		}
		else if(slice.getDescription().equals("start()")){
			XYSeries series = seriesArraylist3.get(getTaskSeriesID(slice.getThread()));
			series.add(slice.getTime() - programStartTime, 4D);
		}
		else if(slice.getDescription().equals("interrupt()")){
			XYSeries series = seriesArraylist3.get(getTaskSeriesID(slice.getThread()));
			series.add(slice.getTime() - programStartTime, 0D);
		}
		else if(slice.getDescription().equals("destroy()")){
			XYSeries series = seriesArraylist3.get(getTaskSeriesID(slice.getThread()));
			series.add(slice.getTime() - programStartTime, 3D);
		}
		
	}
		
	
	
	//This method adds a critical section to the critical section bar chart and should be called by
	//the Visualizer class when the Visualizer.leaveCriticalSection() method is used
	public static void addCriticalSection(String criticalSection, long id, long enter, long leave){
		long start = enter - programStartTime;
		long end = leave - programStartTime;
		Date enterTime = new Date(start);
		Date exitTime = new Date(end);
		
		curTask = new Task(criticalSection, enterTime, exitTime);
		int index = getTaskSeriesID(id);
		
		CriticalSectionQObject qObject = new CriticalSectionQObject(curTask, index);
		criticalSectionQueue.add(qObject);		
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
		int threadNum = threads.size();
		XYSeries series = new XYSeries("Thread" + Integer.toString(threadNum));
		seriesArraylist.add(series);
		XYSeries series2 = new XYSeries("Thread" + Integer.toString(threadNum));
		seriesArraylist2.add(series2);
		dataset.addSeries(series);
		dataset2.addSeries(series2);
		
		createCheckbox(threadNum);
		createChart4AxisLabels(threadNum);
		TaskSeries task = new TaskSeries("Thread "+ Long.toString(getTaskSeriesID(newThread.getId())+1));
		XYSeries series3 = new XYSeries("Thread" + threadNum);
		seriesArraylist3.add(series3);
		dataset3.addSeries(series3);
		taskSeriesArray.add(task);
		data4.add(task);
	}

}
