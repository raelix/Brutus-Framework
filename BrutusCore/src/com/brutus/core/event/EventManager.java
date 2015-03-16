package com.brutus.core.event;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

import com.brutus.shared.Constants;


public class EventManager implements PropertyChangeListener,Constants {

	protected static Object lockPoll = new Object();//monitor lock for poll threads
	protected static AtomicInteger counterPoll = new AtomicInteger(0);//limit threads for poll 
	
	public EventManager(){
		super();
	}
	


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("evt: "+evt.getPropertyName()+" old "+evt.getOldValue()+" new "+evt.getNewValue());
		
		
	}

	

}