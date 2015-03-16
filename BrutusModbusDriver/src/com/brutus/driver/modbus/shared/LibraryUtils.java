package com.brutus.driver.modbus.shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class LibraryUtils {

	public static void installLibrary(){
		String javaPath = System.getProperty("java.library.path");
		String arch = System.getProperty("os.arch");
		String os = System.getProperty("os.name");
		String baseUrl = System.getProperty("user.dir")+ "/"  ;
		StringTokenizer splitter;
		if(!os.toLowerCase().contains("win"))
			splitter = new StringTokenizer(javaPath,":");
		else splitter = new StringTokenizer(javaPath,";");
		LinkedList<String> librariesPath = new LinkedList<String>();//get a list of libraries path
		if(!os.toLowerCase().contains("win"))//on windows serial is default enable no need edit
			try {
				Runtime.getRuntime().exec("sudo chmod +x "+baseUrl+"drivers/rpi-serial-control");
				Runtime.getRuntime().exec("sudo cp -avr drivers/rpi-serial-control /usr/bin/");
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("sudo rpi-serial-control status").getInputStream()));
				String s = null;
				while ((s = stdInput.readLine()) != null) {
				    System.out.println(s);
				    if(s.contains("reboot")){
				    	System.err.println("For serial connection reboot is needed! Rebooting now!...");
				    	Runtime.getRuntime().exec("reboot -h");
				    }
				    else if(s.contains("enable")){
				    	Runtime.getRuntime().exec("sudo rpi-serial-control disable");
				    }
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		while(splitter.hasMoreTokens()){
			librariesPath.add(splitter.nextToken());//produces list
		}
		if(os.toLowerCase().contains("win")){//windows case
			if(arch.contains("arm")){
			}
			if(arch.contains("32")){
				System.out.println("win arch x86");
				File library = new File(baseUrl+"drivers\\win32\\rxtxSerial.dll");
				File common = new File(baseUrl+"drivers\\RXTXcomm.jar");
				if (!library.exists()){
					System.out.println(library.getAbsolutePath()+" not found");
					return;
				}
				if (!common.exists()){
					System.out.println(common.getAbsolutePath()+" not found");
					return;
				}
				for(String path : librariesPath){
					File existInPathLibrary = new File(path+"\\rxtxSerial.dll"); 
					File existInPathCommon = new File(path+"\\RXTXcomm.jar"); 
					if(!existInPathCommon.exists()){//if RXTXcomm.jar isn't in path
						System.out.println("coping "+common.getAbsolutePath()+" to "+path+"");
						doWindowsCopy(common.getAbsolutePath(), path);
					}
					if(!existInPathLibrary.exists()){//if rxtxSerial.dll isn't in path
						System.out.println("coping "+library.getAbsolutePath()+" to "+path+"");
						doWindowsCopy(library.getAbsolutePath(), path);
					}
				}
			}
			if(arch.contains("64")){
				File library = new File(baseUrl+"drivers\\win64\\rxtxSerial.dll");
				File common = new File(baseUrl+"drivers\\RXTXcomm.jar");
				if (!library.exists()){
					System.out.println(library.getAbsolutePath()+" not found");
					return;
				}
				if (!common.exists()){
					System.out.println(common.getAbsolutePath()+" not found");
					return;
				}
				for(String path : librariesPath){
					File existInPathLibrary = new File(path+"\\rxtxSerial.dll"); 
					File existInPathCommon = new File(path+"\\RXTXcomm.jar"); 
					if(!existInPathCommon.exists()){//if RXTXcomm.jar isn't in path
						doWindowsCopy(common.getAbsolutePath(), path+"\\");
					}
					if(!existInPathLibrary.exists()){//if rxtxSerial.dll isn't in path
						doWindowsCopy(library.getAbsolutePath(), path+"\\");
					}
				}
			}
		}
		else if(!os.toLowerCase().contains("win")){
			if(arch.contains("arm")){//linux arm arch
				File library = new File(baseUrl+"drivers/arm/librxtxSerial.so");
				File common = new File(baseUrl+"drivers/RXTXcomm.jar");
				if (!library.exists()){
					System.out.println(library.getAbsolutePath()+" not found");
					return;
				}
				if (!common.exists()){
					System.out.println(common.getAbsolutePath()+" not found");
					return;
				}
				for(String path : librariesPath){
					File existInPathLibrary = new File(path+"/librxtxSerial.so"); 
					File existInPathCommon = new File(path+"/RXTXcomm.jar"); 

					if(!existInPathCommon.exists()){//if RXTXcomm.jar isn't in path
						doLinuxCopy(common.getAbsolutePath(), path+"/");

						System.out.println("coping "+common.getAbsolutePath()+" to "+path+"/");
					}
					if(!existInPathLibrary.exists()){//if rxtxSerial.dll isn't in path
						doLinuxCopy(library.getAbsolutePath(), path+"/");

						System.out.println("coping "+library.getAbsolutePath()+" to "+path+"/");
					}
				}
			}
			else {//linux 32_64 arch
				File library = new File(baseUrl+"drivers/linux32_64/librxtxSerial.so");
				File common = new File(baseUrl+"drivers/RXTXcomm.jar");
				if (!library.exists()){
					System.out.println(library.getAbsolutePath()+" not found");
					return;
				}
				if (!common.exists()){
					System.out.println(common.getAbsolutePath()+" not found");
					return;
				}
				for(String path : librariesPath){
					File existInPathLibrary = new File(path+"/librxtxSerial.so"); 
					File existInPathCommon = new File(path+"/RXTXcomm.jar"); 

					if(!existInPathCommon.exists()){//if RXTXcomm.jar isn't in path
						doLinuxCopy(common.getAbsolutePath(), path+"/");

						System.out.println("coping "+common.getAbsolutePath()+" to "+path+"/");
					}
					if(!existInPathLibrary.exists()){//if rxtxSerial.dll isn't in path
						doLinuxCopy(library.getAbsolutePath(), path+"/");

						System.out.println("coping "+library.getAbsolutePath()+" to "+path+"/");
					}
				}
			}
		}
	}

	public static void doLinuxCopy(String commonAbsolutePath, String pathDest){
		try {
			Runtime.getRuntime().exec("sudo cp -avr "+commonAbsolutePath+" "+pathDest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void doWindowsCopy(String commonAbsolutePath, String pathDest){
		try {
			Runtime.getRuntime().exec("cmd "+ "/c "+ "COPY " +"\""+commonAbsolutePath+"\" "+ "\""+pathDest+"\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}