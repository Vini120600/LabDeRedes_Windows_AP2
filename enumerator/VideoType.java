package org.comeia.project.enumerator;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFilter("attributeFilter")
public enum VideoType {

	MP4("Tipo Padrão"), 
	WMV("Recomendado pelo Sistema");

	public final String displayName;

	private VideoType(String displayName) {
		this.displayName = displayName;
	}
	
	@JsonCreator
    public static VideoType forValue(String value) {
        return VideoType.valueOf(value);
    }
	
	@JsonValue
	public HashMap<String, String> jsonValue() {
		HashMap<String, String> map = new HashMap<>();
		map.put("name", this.name());
		map.put("displayName", this.displayName);
		return map;
	}
}
