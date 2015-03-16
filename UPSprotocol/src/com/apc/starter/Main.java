package com.apc.starter;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {
	static {
		System.loadLibrary("hidapi-jni-32");
	}
	public static void main(String[] args) throws ServletException, LifecycleException {
		tomcat();

	}


	public static void tomcat() throws ServletException, LifecycleException{
		Tomcat tomcat = new Tomcat();
		String webPort = "8888";
		if (webPort == null || webPort.isEmpty()) {
			webPort = "8888";
		}
		tomcat.setPort(Integer.valueOf(webPort));
		Context context = tomcat.addWebapp("/", new File("WebContent/").getAbsolutePath());
		Tomcat.addServlet(context, "jersey-container-servlet", resourceConfig());
		context.addServletMapping("/*", "jersey-container-servlet");
		tomcat.start();
		tomcat.getServer().await();
	}

	private static ServletContainer resourceConfig() {
		return new ServletContainer(new ResourceConfig(
				new ResourceLoader().getClasses()));
	}
}
