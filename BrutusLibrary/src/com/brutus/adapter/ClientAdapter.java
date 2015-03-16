package com.brutus.adapter;

import java.util.ArrayList;

import com.brutus.base.Param;

 public class ClientAdapter  extends Thread{
	protected CoreAdapter core;
	public  ClientAdapter(CoreAdapter core){
		super();
		this.core = core;
//		start();
	}
	
	protected ArrayList<Param> getValues(ArrayList<Param> params){
		return core.getValues(params);
	}
}