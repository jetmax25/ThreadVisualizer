import java.lang.instrument.Instrumentation;

public class VisualThread extends Thread{
	
	private long id;
	public VisualThread()
	{
		super();
		this.id = super.getId();
		VisualGUI.threadAdded(this);
		Analyzer.addThread(this);
		this.addSlice("Initialized");	
	
	}
	
	public VisualThread(Runnable runnable){
		super(runnable);
		this.id = super.getId();
		Analyzer.addThread(this);
		VisualGUI.threadAdded(this);
		this.addSlice("Initialized");
	}
	
	public VisualThread(Runnable runnable, String string){
		super(runnable, string);
		this.id = super.getId();
		Analyzer.addThread(this);
		VisualGUI.threadAdded(this);
		this.addSlice("Initialized");
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		this.addSlice("Clone()");
		return super.clone();
	}

	@Override
	public int countStackFrames() {
		// TODO Auto-generated method stub
		this.addSlice("countStackFrames()");
		return super.countStackFrames();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		this.addSlice("destroy()");
		Analyzer.removeThreads(this);
		super.destroy();
	}

	@Override
	public ClassLoader getContextClassLoader() {
		// TODO Auto-generated method stub
		this.addSlice("getContextClassLoader()");
		return super.getContextClassLoader();
	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		// TODO Auto-generated method stub
		this.addSlice("getStackTrace()");
		return super.getStackTrace();
	}

	@Override
	public State getState() {
		// TODO Auto-generated method stub
		this.addSlice("getState()");
		return super.getState();
	}

	@Override
	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		// TODO Auto-generated method stub
		this.addSlice("getUncaughtExceptionHandler()");
		return super.getUncaughtExceptionHandler();
	}

	@Override
	public void interrupt() {
		// TODO Auto-generated method stub
		this.addSlice("interrupt()");
		super.interrupt();
	}

	@Override
	public boolean isInterrupted() {
		// TODO Auto-generated method stub
		this.addSlice("isInterrupted()");
		return super.isInterrupted();
	}

	@Override
	public void run() {
		boolean x = true;
		while(x){
			// TODO Auto-generated method stub
			try {
				Thread.sleep(100 * id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				x = false;
	
			}
			this.addSlice("run()");
		}
		
	}

	@Override
	public void setContextClassLoader(ClassLoader arg0) {
		// TODO Auto-generated method stub
		this.addSlice("setContextClassLoader(" + arg0.toString() +")");
		super.setContextClassLoader(arg0);
	}

	@Override
	public void setUncaughtExceptionHandler(UncaughtExceptionHandler arg0) {
		// TODO Auto-generated method stub
		this.addSlice("setUncaughtExceptionHandler(" + arg0.toString()+")");
		super.setUncaughtExceptionHandler(arg0);
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		System.out.println("thread started");
		this.addSlice("start()");
		super.start();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		this.addSlice("toString");
		return super.toString();
	}
	
	public void addSlice(String s )
	{
		if(Analyzer.isAcceptingThread(this.id))  Analyzer.addSlice(new ActivitySlice(s, this.id, System.currentTimeMillis()/*, ObjectSizeFetcher.sizeOf(this) */));
		//System.out.println("Step 2"); }
	}
	
	public void enterCriticalSection(String section)
	{
		Analyzer.enteringCriticalSection(this.id, section, System.currentTimeMillis());
	}
	
	public void leaveCriticalSection(String section)
	{
		Analyzer.leavingCriticalSection(this.id, section, System.currentTimeMillis());
	}
	
}




 class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long sizeOf(Object o) {
        return instrumentation.getObjectSize(o);
    }
}
