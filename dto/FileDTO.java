package org.comeia.project.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.comeia.project.enumerator.ServerType;
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
public @Data class FileDTO implements Serializable {

	private Long id;
	private String name;
	private String conteudo;
	//private String directory;

 
}