package org.comeia.project.converter;

import static java.util.Optional.ofNullable;

import java.util.Objects;

import org.comeia.project.domain.File_;
import org.comeia.project.dto.FileDTO;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FileConverter implements Converter<File_, FileDTO> {

	@Override
	public FileDTO from(File_ entity) {
		
		if(Objects.isNull(entity)) {
			return null;
		}
		
		FileDTO dto = new FileDTO();
		
		ofNullable(entity.getId())
			.ifPresent(dto::setId);
		
		ofNullable(entity.getName())
			.ifPresent(dto::setName);
		
		/*ofNullable(entity.getDirectory())
			.ifPresent(dto::setDirectory);*/
		
		ofNullable(dto.getConteudo())
		.ifPresent(dto::setConteudo);
		
		
	
		
		return dto;
	}
	
	@Override
	public File_ to(FileDTO dto, File_ entity) {
		
		if(Objects.isNull(dto)) {
			return null;
		}

		if(Objects.isNull(entity)) {
			entity = new File_();
		}
		
		ofNullable(dto.getName())
			.ifPresent(entity::setName);
		
		ofNullable(dto.getConteudo())
		.ifPresent(dto::setConteudo);
		
		
		
		
		
		
		return entity;
	}
}