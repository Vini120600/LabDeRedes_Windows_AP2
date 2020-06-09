package org.comeia.project.converter;

import static java.util.Optional.ofNullable;

import java.util.Objects;

import org.comeia.project.domain.Video;
import org.comeia.project.dto.VideoDTO;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class VideoConverter implements Converter<Video, VideoDTO> {

	@Override
	public VideoDTO from(Video entity) {
		
		if(Objects.isNull(entity)) {
			return null;
		}
		
		VideoDTO dto = new VideoDTO();
		
		ofNullable(entity.getId())
			.ifPresent(dto::setId);
		
		ofNullable(entity.getFullName())
			.ifPresent(dto::setFullName);
		
		ofNullable(entity.getVideoType())
			.ifPresent(dto::setVideoType);
		
		return dto;
	}
	
	@Override
	public Video to(VideoDTO dto, Video entity) {
		
		if(Objects.isNull(dto)) {
			return null;
		}

		if(Objects.isNull(entity)) {
			entity = new Video();
		}
		
		ofNullable(dto.getFullName())
			.ifPresent(entity::setFullName);
		
		ofNullable(dto.getVideoType())
			.ifPresent(entity::setVideoType);
		
		return entity;
	}
}
