package org.comeia.project.enumerator;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFilter("attributeFilter")
public enum MusicType {

	MP3("Tipo Padrão"), 
	WMA("Recomendado pelo Sistema");

	public final String displayName;

	private MusicType(String displayName) {
		this.displayName = displayName;
	}
	
	@JsonCreator
    public static MusicType forValue(String value) {
        return MusicType.valueOf(value);
    }
	
	@JsonValue
	public HashMap<String, String> jsonValue() {
		HashMap<String, String> map = new HashMap<>();
		map.put("name", this.name());
		map.put("displayName", this.displayName);
		return map;
	}
}
