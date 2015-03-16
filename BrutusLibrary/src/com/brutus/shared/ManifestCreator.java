package com.brutus.shared;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ManifestCreator {
	private   String version = "1.0.0";
	private   String author  = "anonymous";
	private   String packages = "packageName";
	private   String type = "";


	public  void generateManifest() throws Exception{
		Manifest man = new Manifest();
		Attributes attr = man.getMainAttributes();
		attr.put(Attributes.Name.MANIFEST_VERSION, getVersion());
		attr.put(new Attributes.Name("Author"), getAuthor());
		attr.put(new Attributes.Name("Module-Class"), getPackages());
		attr.put(new Attributes.Name("Type"), getType());
		File manifest = new File("MANIFEST.MF");
		OutputStream os = new FileOutputStream(manifest);
		man.write(os);
		os.flush();
		os.close();
	}

	

//	@SuppressWarnings("unused")
//	private  String getMacAddr(){
//		InetAddress ip;
//		try {
//			ip = InetAddress.getLocalHost();
//			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
//			byte[] mac = network.getHardwareAddress();
//			StringBuilder sb = new StringBuilder();
//			for (int i = 0; i < mac.length; i++) {
//				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
//			}
//			return sb.toString();
//		} catch (Exception e){
//			e.printStackTrace();
//			return "NOTEXIST";
//		}
//	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}


	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}


	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}


	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}


	/**
	 * @return the packages
	 */
	public String getPackages() {
		return packages;
	}


	/**
	 * @param packages the packages to set
	 */
	public void setPackages(String packages) {
		this.packages = packages;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
