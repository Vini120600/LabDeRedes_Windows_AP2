package org.comeia.project.converter;

import static java.util.Optional.ofNullable;

import java.util.Objects;

import org.comeia.project.domain.Music;
import org.comeia.project.dto.MusicDTO;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MusicConverter implements Converter<Music, MusicDTO> {

	@Override
	public MusicDTO from(Music entity) {
		
		if(Objects.isNull(entity)) {
			return null;
		}
		
		MusicDTO dto = new MusicDTO();
		
		ofNullable(entity.getId())
			.ifPresent(dto::setId);
		
		ofNullable(entity.getFullName())
			.ifPresent(dto::setFullName);
		
		ofNullable(entity.getMusicType())
			.ifPresent(dto::setMusicType);
		
		return dto;
	}
	
	@Override
	public Music to(MusicDTO dto, Music entity) {
		
		if(Objects.isNull(dto)) {
			return null;
		}

		if(Objects.isNull(entity)) {
			entity = new Music();
		}
		
		ofNullable(dto.getFullName())
			.ifPresent(entity::setFullName);
		
		ofNullable(dto.getMusicType())
			.ifPresent(entity::setMusicType);
		
		return entity;
	}
}
