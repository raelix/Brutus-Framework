package com.brutus.core.rest;
import java.io.File;
import java.io.FileFilter;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.brutus.adapter.ClientAdapter;
import com.brutus.adapter.CoreAdapter;
import com.brutus.shared.BrutusConf;

public class BrutusRestClientStarter extends ClientAdapter {

	public static CoreAdapter Core ;
	public static BrutusRestClient plugin;
	protected String webConfig = "WEB-INF/web.xml";
	protected String webappDirLocation = "WebContent/";

	public BrutusRestClientStarter(CoreAdapter core) {
		super(core);
		Core = core;
		init();
	}

	public void init(){
		try {
			plugin = (BrutusRestClient) BrutusConf.loadPluginClass(BrutusRestClient.class, "BrutusRestClient");
		} catch (Exception e) {
			System.err.println("No configuration found for BrutusRestClient using default port 8080!");
		}
	}
	private ServletContainer resourceConfig() {
		return new ServletContainer(new ResourceConfig(
				new ResourceLoader().getClasses()));
	}


	@Override
	public void run(){
		super.run();
		try {
			if(plugin == null ){

				 
	            // Define a folder to hold web application contents.
	            Tomcat tomcat = new Tomcat();
	     
	            // Define port number for the web application
	            String webPort = System.getenv("PORT");
	            if (webPort == null || webPort.isEmpty()) {
	                webPort = "8080";
	            }
	            // Bind the port to Tomcat serverRuntime.getRuntime().exec("jar -xvf gwt.war");
	            tomcat.setPort(Integer.valueOf(webPort));
	         
	            // Define a web application context.
	            Context context = tomcat.addWebapp("/", new File(
	                    webappDirLocation).getAbsolutePath());
	     
	           // Add servlet that will register Jersey REST resources
	            Tomcat.addServlet(context, "jersey-container-servlet", resourceConfig());
	            context.addServletMapping("/brutus/*", "jersey-container-servlet");
	     
	            tomcat.start();
	            tomcat.getServer().await();

			}
			else{
				if( !plugin.isEnable()){
					System.out.println("BrutusClientRest is disable from settings!");
					return;
				}  

				Tomcat tomcat = new Tomcat();
				//HttpSession session = request.getSession();
				String webPort = ""+plugin.getPort();
				if (webPort == null || webPort.isEmpty()) 
					webPort = "8080";

				tomcat.setPort(Integer.valueOf(webPort));
				
				//explode war archive
//				   File[] files = new File(webappDirLocation).listFiles(new ModuleFilter());
//		            
//				     if(files.length > 0){
//				    	 try {
//				    		 System.out.println("extract "+files[0].getName());
//							Runtime.getRuntime().exec("jar -xvf"+" "+files[0].getName());
//						} catch (IOException e) {
//							System.err.println("cannot extract webPage from "+files[0].getName());
//						} 
//				     }
//				     else{
//				    	 System.out.println("no gwt found");
//				     }
				
				Context context = tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());//contenuto webapp
				Tomcat.addServlet(context, "jersey-container-servlet", resourceConfig());
				context.addServletMapping("/brutus/*", "jersey-container-servlet");
				tomcat.start();
				tomcat.getServer().await();

			}
		} catch (ServletException | LifecycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class ModuleFilter implements FileFilter { 
		@Override 
		public boolean accept(File file) { 
			return file.isFile() && file.getName().toLowerCase().endsWith(".war"); 
		} 
	}

}
