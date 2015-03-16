package com.brutus.driver.arduino;

import com.brutus.shared.ManifestCreator;

public class MainManifest {

	public static void main(String[] args) throws Exception {
		ManifestCreator man = new ManifestCreator();
		man.setAuthor("raelix");
		man.setPackages(BrutusArduinoDriverStarter.class.getCanonicalName());
		man.setType("Driver");
		man.setVersion("1.0.0");
		man.generateManifest();

	}

}
