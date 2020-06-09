package org.comeia.project.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.comeia.project.enumerator.VideoType;
import org.comeia.project.locale.MessagesMessageKeys;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("attributesFilter")
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper=false)
public @Data class VideoDTO implements Serializable {

	private Long id;
	
	@NotNull(message = MessagesMessageKeys.VALIDATE_VIDEO_FULLNAME_NOT_VALID)
	@Size(message = MessagesMessageKeys.VALIDATE_VIDEO_FULLNAME_NOT_LENGTH, min = 5, max = 60)
	private String fullName;
	
	@NotNull(message = MessagesMessageKeys.VALIDATE_VIDEO_TYPE_NOT_VALID)
	private VideoType videoType;

}
