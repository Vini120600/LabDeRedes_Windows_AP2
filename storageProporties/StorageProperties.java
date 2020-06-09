package org.comeia.project.storageProporties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")

public class StorageProperties {
	/**
	 * Folder location for storing files
	 */
	private String location = "Arquivos";

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
