package com.brutus.andbrutus.utils;

public class Utils {

	public static int getIntFromObject(Object value){
		if(value!= null){
			if(value instanceof Integer){
				return (Integer)value;
			}
			else if(value instanceof Double){
				return((Double)value).intValue();
			}
			else if(value instanceof Long){
				return((Long)value).intValue();	 
			}
			else if(value instanceof Short){
				return((Short)value).intValue();
			}
			else if(value instanceof Float)
				return((Float)value).intValue();	
		}
		return -1;
	}
}
