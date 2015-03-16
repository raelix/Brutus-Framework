package com.brutus.driver.hwsw;

import com.brutus.shared.ManifestCreator;

public class MainMainfest {

	public static void main(String[] args) throws Exception {
		ManifestCreator man = new ManifestCreator();
		man.setAuthor("raelix");
		man.setPackages(BrutusHwSwDriverStarter.class.getCanonicalName());
		man.setType("Server");
		man.setVersion("1.0.0");
		man.generateManifest();
	}
}
