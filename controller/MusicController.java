package org.comeia.project.controller;

import static org.comeia.project.search.MusicSpecification.listAllByCriteria;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.comeia.project.converter.MusicConverter;
import org.comeia.project.domain.Music;
import org.comeia.project.dto.MusicDTO;
import org.comeia.project.dto.MusicFilterDTO;
import org.comeia.project.enumerator.MusicType;
import org.comeia.project.locale.ErrorMessageKeys;
import org.comeia.project.repository.MusicRepository;
import org.comeia.project.search.SearchCriteria;
import org.comeia.project.validator.MusicValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/music")
@AllArgsConstructor
public class MusicController extends ResourceController {

	private final MusicRepository repository;
	private final MusicConverter converter;
	private final MusicValidator validator;
	
	@GetMapping
	public MappingJacksonValue listByCriteria(Pageable pageable,
			@RequestParam(required = false) String attributes,
			MusicFilterDTO filter) {
		
		List<SearchCriteria> criterias = MusicFilterDTO.buildCriteria(filter);
		Page<MusicDTO> pages = this.repository.findAll(listAllByCriteria(criterias), pageable)
				.map(converter::from);
		return buildResponse(pages, attributes);
	}
	
	@GetMapping(path = "/types")
	public MappingJacksonValue types(@RequestParam(required = false) String attributes) {
		MusicType[] types = MusicType.values();
		return buildResponse(types, attributes);
	}
	
	@GetMapping(path = "{id}")
	public MappingJacksonValue getById(@PathVariable long id,
			@RequestParam(required = false) String attributes) {
		
		MusicDTO dto = this.repository.findByIdAndDeletedIsFalse(id)
				.map(this.converter::from)
				.orElseThrow(() -> throwsException(ErrorMessageKeys.ERROR_MUSIC_NOT_FOUND_BY_ID, String.valueOf(id)));
		return buildResponse(dto, attributes);
	}
	
	@PostMapping
	public MappingJacksonValue create(@Validated @RequestBody MusicDTO dto,
			@RequestParam(required = false) String attributes) {
		
		if(Objects.isNull(dto)) {
			throw new HttpMessageNotReadableException("Required request body is missing");
		}
		
		MusicDTO musicDTO = Optional.of(dto)
				.map(this.converter::to)
				.map(this.repository::save)
				.map(this.converter::from)
				.orElseThrow(() -> throwsException("Error"));
		
		return buildResponse(musicDTO, attributes);
	}
	
	@PutMapping(path = "{id}")
	public MappingJacksonValue update(@PathVariable long id,
			@Validated @RequestBody MusicDTO dto,
			@RequestParam(required = false) String attributes) {
		
		if(Objects.isNull(dto)) {
			throw new HttpMessageNotReadableException("Required request body is missing");
		}
		
		MusicDTO musicDTO = this.repository.findByIdAndDeletedIsFalse(id)
				.map(music -> this.converter.to(dto, music))
				.map(this.repository::save)
				.map(this.converter::from)
				.orElseThrow(() -> throwsException(String.valueOf(id)));
		
		return buildResponse(musicDTO, attributes);
	}
	
	@DeleteMapping(path = "{id}")
	public void delete(@PathVariable long id) {
		
		MusicDTO dto = this.repository.findByIdAndDeletedIsFalse(id)
				.map(this.converter::from)
				.orElseThrow(() -> throwsException(ErrorMessageKeys.ERROR_MUSIC_NOT_FOUND_BY_ID, String.valueOf(id)));
		
		Music music = this.converter.to(dto);
		music.setDeleted(true);
		music.setId(dto.getId());
		this.repository.save(music);
		
	}
}
