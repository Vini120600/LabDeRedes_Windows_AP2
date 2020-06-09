package org.comeia.project.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.comeia.project.search.SearchCriteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper=false)
public @Data class FileFilterDTO implements Serializable {

	private String name;
	private String directory;
	private String conteudo;
	
	
	public static List<SearchCriteria> buildCriteria(FileFilterDTO filter) {
		List<SearchCriteria> criterias = new ArrayList<>();
		
		if(filter.getName() != null && !filter.getName().isEmpty()) { 
			criterias.add(new SearchCriteria("name", "%".concat(filter.getName()).concat("%")));
		}
		
		/*if(filter.getDirectory() != null && !filter.getDirectory().isEmpty()) { 
			criterias.add(new SearchCriteria("directory", "%".concat(filter.getDirectory()).concat("%")));
		}*/

		if(filter.getConteudo() != null && !filter.getConteudo().isEmpty()) { 
			criterias.add(new SearchCriteria("conteudo", "%".concat(filter.getConteudo()).concat("%")));
		}
		return criterias;
	}
}
