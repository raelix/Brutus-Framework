package com.brutus.shared;



public class Debug {
	static int debugLevel = 2;

	public static final void print(String output, int level){
		if(BrutusConf.getInstance() == null || BrutusConf.getInstance().getCore() == null){
			if(debugLevel >= level){
				System.out.println(output);
			}
		}
		else if( BrutusConf.getInstance().getCore().getDebugLevel() >= level){
			System.out.println(output);
		}
	}
	public static final void printError(String output, int level){
		if(BrutusConf.getInstance() == null || BrutusConf.getInstance().getCore() == null){
			if(debugLevel >= level){
				System.out.println(output);
			}
		}
		else if(BrutusConf.getInstance().getCore().getDebugLevel()  >= level){
				System.err.println(output);
			}
	}
}