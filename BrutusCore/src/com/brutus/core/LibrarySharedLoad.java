package com.brutus.core;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.brutus.adapter.ClientAdapter;
import com.brutus.adapter.CoreAdapter;
import com.brutus.adapter.DriverAdapter;
import com.brutus.shared.BrutusConf;
import com.brutus.shared.Debug;

public class LibrarySharedLoad {
	
	static ArrayList<File> files = new ArrayList<>();
	private static List<URL> urls = new ArrayList<URL>(); 
	private static ClassLoader classLoader;
	
	public static  List<Object> loadModules(CoreAdapter core,LinkedList<DriverAdapter> serverIstance, LinkedList<ClientAdapter> clientIstance) throws NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{ 
		List<Object> modules = new ArrayList<Object>(); 
		Class<?> server = DriverAdapter.class;
		Class<?> client = ClientAdapter.class;
//		BrutusArduinoDriverStarter ard = new BrutusArduinoDriverStarter();
//		serverIstance.add(ard);
		for(String c : getModuleClasses()){ 
			try { 
				Class<?> moduleClass = Class.forName(c, true, classLoader); 
				if(client.isAssignableFrom(moduleClass)){
					Constructor<?> ctor = moduleClass.getDeclaredConstructor(CoreAdapter.class);
					ctor.setAccessible(true);
					ClientAdapter ad = (ClientAdapter) ctor.newInstance(core);
					ad.start();
					clientIstance.add(ad);
				}
				else if(server.isAssignableFrom(moduleClass)){
					Constructor<?> ctor = moduleClass.getDeclaredConstructor();
					DriverAdapter ad = (DriverAdapter) ctor.newInstance();
					ad.start();
					serverIstance.add(ad);
				}
			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) { 
				e.printStackTrace(); 
			} 
		} 
		return modules; 
	}
	private static  List<String> getModuleClasses(){ 
		List<String> classes = new ArrayList<String>(); 
		File[] files = new File(BrutusConf.pluginDirectory).listFiles(new ModuleFilter());
		Debug.print("Number Plugin found: "+files.length+" ",4);
		for(File f : files){ 
			JarFile jarFile = null; 
			try { 
				jarFile = new JarFile(f); 
				Manifest manifest = jarFile.getManifest();
				if(manifest.getMainAttributes() != null && 
						manifest.getMainAttributes().getValue("Module-Class") != null)
					classes.add(manifest.getMainAttributes().getValue("Module-Class"));
				urls.add(f.toURI().toURL());
				AccessController.doPrivileged(new PrivilegedAction<Object>(){ 
					@Override 
					public Object run() { 
						classLoader = new URLClassLoader(  urls.toArray(new URL[urls.size()]), 
								Main.class.getClassLoader()); 
						return null; 
					} 
				}); 
			} catch (Exception e) { 
				e.printStackTrace(); 
			} finally { 
				if(jarFile != null){ 
					try { 
						jarFile.close(); 
					} catch (IOException e) { 
						e.printStackTrace(); 
					} 
				} 
			} 
		}
		return classes; 
	}

	public static class ModuleFilter implements FileFilter { 
		@Override 
		public boolean accept(File file) { 
			return file.isFile() && file.getName().toLowerCase().endsWith(".jar"); 
		} 
	}
	
	
	public static void addLibrary(){
		File fileFolder = new File(BrutusConf.brutusPluginFloder);
		if(fileFolder.exists()){
			files = listFilesForFolder(fileFolder);
		System.out.println("BrutusCore: Found "+files.size()+" Shared Library to copy...");
			copyLibrary();
		}
		else{
			System.out.println("BrutusCore: No shared Library founds!skipping...");
			return;
		}
	}

	public static void copyLibrary(){
		String javaPath = System.getProperty("java.library.path");
		String os = System.getProperty("os.name");
		StringTokenizer splitter;
		if(!os.toLowerCase().contains("win"))
			splitter = new StringTokenizer(javaPath,":");
		else splitter = new StringTokenizer(javaPath,";");
		LinkedList<String> librariesPath = new LinkedList<String>();//create a empty list of libraries path
		while(splitter.hasMoreTokens()){
			librariesPath.add(splitter.nextToken());//produces list of libraries path
		}
		if(os.toLowerCase().contains("win")){
			for(File file : files){
				for(String path : librariesPath){
					File nameDestination  = new File(path+"//"+file.getName());
					if(new File(path).exists()){
						if(!nameDestination.exists()){//if RXTXcomm.jar isn't in path
							System.out.println("coping "+file.getAbsolutePath()+" to "+path+"");
							doWindowsCopy(file.getAbsolutePath(), path);
						}
						else Debug.print("BrutusCore: "+nameDestination.getName()+" exist in "+path+"!skipping...", 5);
					}
					else Debug.print("BrutusCore: "+path+" not exist!skipping...", 5);
				}
			}
		}//on windows
		else {
			for(File file : files){
				for(String path : librariesPath){
					File existInPathLibrary = new File(path+"/"+file.getName()); 
					if(new File(path).exists()){
						if(!existInPathLibrary.exists()){//if RXTXcomm.jar isn't in path
							System.out.println("coping "+existInPathLibrary.getAbsolutePath()+" to "+path+"");
							doLinuxCopy(existInPathLibrary.getAbsolutePath(), path+"/");
						}
						else Debug.print("BrutusCore: "+existInPathLibrary.getName()+" exist in "+path+"!skipping...", 5);
					}
					else Debug.print("BrutusCore: "+path+" not exist!skipping...", 5);
				}
			}
		}//on linux

	}


	public static ArrayList<File> listFilesForFolder(final File folder) {
		ArrayList<File> temp = new ArrayList<File>();
		for (final File fileEntry : folder.listFiles()) {
			String extension = fileEntry.getAbsolutePath().substring(fileEntry.getAbsolutePath().lastIndexOf('.'));
			if(extension.toLowerCase().contentEquals(".jar")||
					extension.toLowerCase().contentEquals(".dll") ||
					extension.toLowerCase().contentEquals(".so")){
				temp.add(fileEntry);
			}
		}
		return temp;
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
			System.out.println("cmd "+ "/c "+ "COPY " +"\""+commonAbsolutePath+"\" "+ "\""+pathDest+"\"");
			Runtime.getRuntime().exec("cmd "+ "/c "+ "COPY " +"\""+commonAbsolutePath+"\" "+ "\""+pathDest+"\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
