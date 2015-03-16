package com.brutus.adapter;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.brutus.base.Param;
//reader adapter , writeadapter , getpluginclass , enable plugin conf, mapdb log in array in param, after gson conver json
public abstract class DriverAdapter extends Thread implements Callable<ArrayList<Param>> {
	protected ArrayList<Param> buffer;
	int SERVER_WAIT = 2000;
	
	public DriverAdapter(){
		super();
	}

	public void setBuffer(ArrayList<Param> buffer){
		this.buffer = buffer;
	}

	@Override
	public ArrayList<Param> call() {
		return OnParamsRequest( buffer);
	}

	abstract public void setValues(ArrayList<Param> buffer);

	abstract public ArrayList<Param> OnParamsRequest(ArrayList<Param> buffer);


}