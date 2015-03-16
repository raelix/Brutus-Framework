
package com.brutus.shared;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class GC {

	public GC() {
	}

	public static void callFree() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Calendar c = Calendar.getInstance(); 
		Runtime runtime = Runtime.getRuntime();
		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		long usedMemory = totalMemory - runtime.freeMemory();
		runtime.gc();
		//        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
		//	      int noThreads = currentGroup.activeCount();
		//	      Thread[] lstThreads = new Thread[noThreads];
		//	      currentGroup.enumerate(lstThreads);
		//	      for (int i = 0; i < noThreads; i++)
		//	      Debug.infoMax("Thread No:" + i + " = "+ lstThreads[i].getName());
		Debug.print(dateFormat.format(c.getTime())+": GC Tot Memory: " + ((int)totalMemory/1024) + " Kb Used: " + ((int)usedMemory/1024) + " Kb Free: "+((int)freeMemory/1024)+" Kb",6);
	}
}
