package org.comeia.project.controller;

import static org.comeia.project.search.VideoSpecification.listAllByCriteria;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.comeia.project.converter.VideoConverter;
import org.comeia.project.domain.Video;
import org.comeia.project.dto.VideoDTO;
import org.comeia.project.dto.VideoFilterDTO;
import org.comeia.project.enumerator.VideoType;
import org.comeia.project.locale.ErrorMessageKeys;
import org.comeia.project.repository.VideoRepository;
import org.comeia.project.search.SearchCriteria;
import org.comeia.project.validator.VideoValidator;
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
@RequestMapping(value = "/api/v1/video")
@AllArgsConstructor
public class VideoController extends ResourceController {

	private final VideoRepository repository;
	private final VideoConverter converter;
	private final VideoValidator validator;
	
	@GetMapping
	public MappingJacksonValue listByCriteria(Pageable pageable,
			@RequestParam(required = false) String attributes,
			VideoFilterDTO filter) {
		
		List<SearchCriteria> criterias = VideoFilterDTO.buildCriteria(filter);
		Page<VideoDTO> pages = this.repository.findAll(listAllByCriteria(criterias), pageable)
				.map(converter::from);
		return buildResponse(pages, attributes);
	}
	
	@GetMapping(path = "/types")
	public MappingJacksonValue types(@RequestParam(required = false) String attributes) {
		VideoType[] types = VideoType.values();
		return buildResponse(types, attributes);
	}
	
	@GetMapping(path = "{id}")
	public MappingJacksonValue getById(@PathVariable long id,
			@RequestParam(required = false) String attributes) {
		
		VideoDTO dto = this.repository.findByIdAndDeletedIsFalse(id)
				.map(this.converter::from)
				.orElseThrow(() -> throwsException(ErrorMessageKeys.ERROR_VIDEO_NOT_FOUND_BY_ID, String.valueOf(id)));
		return buildResponse(dto, attributes);
	}
	
	@PostMapping
	public MappingJacksonValue create(@Validated @RequestBody VideoDTO dto,
			@RequestParam(required = false) String attributes) {
		
		if(Objects.isNull(dto)) {
			throw new HttpMessageNotReadableException("Required request body is missing");
		}
		
		VideoDTO videoDTO = Optional.of(dto)
				.map(this.converter::to)
				.map(this.repository::save)
				.map(this.converter::from)
				.orElseThrow(() -> throwsException("Error"));
		
		return buildResponse(videoDTO, attributes);
	}
	
	@PutMapping(path = "{id}")
	public MappingJacksonValue update(@PathVariable long id,
			@Validated @RequestBody VideoDTO dto,
			@RequestParam(required = false) String attributes) {
		
		if(Objects.isNull(dto)) {
			throw new HttpMessageNotReadableException("Required request body is missing");
		}
		
		VideoDTO videoDTO = this.repository.findByIdAndDeletedIsFalse(id)
				.map(video -> this.converter.to(dto, video))
				.map(this.repository::save)
				.map(this.converter::from)
				.orElseThrow(() -> throwsException(String.valueOf(id)));
		
		return buildResponse(videoDTO, attributes);
	}
	
	@DeleteMapping(path = "{id}")
	public void delete(@PathVariable long id) {
		
		VideoDTO dto = this.repository.findByIdAndDeletedIsFalse(id)
				.map(this.converter::from)
				.orElseThrow(() -> throwsException(ErrorMessageKeys.ERROR_VIDEO_NOT_FOUND_BY_ID, String.valueOf(id)));
		
		Video video = this.converter.to(dto);
		video.setDeleted(true);
		video.setId(dto.getId());
		this.repository.save(video);
		
	}
}
