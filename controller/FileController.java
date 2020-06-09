package org.comeia.project.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.nio.file.Files;

import java.util.List;

import org.comeia.project.converter.FileConverter;
import org.comeia.project.domain.File_;
import org.comeia.project.dto.FileDTO;
import org.comeia.project.dto.FileFilterDTO;
//import org.comeia.project.enumerator.FileType;
import org.comeia.project.locale.ErrorMessageKeys;
import org.comeia.project.repository.FileRepository;
import org.comeia.project.search.SearchCriteria;
import org.comeia.project.service.FileSystemStorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
//import org.comeia.project.validator.FileValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.JpaSort.Path;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Controller
@RestController
@RequestMapping(value = "/api/v1/file_")
@AllArgsConstructor

public class FileController extends ResourceController {

	private final FileRepository repository;
	private final FileConverter converter;
	public final FileSystemStorageService storageService;
	private static final String EXTENSION = ".pdf";
	private static final String SERVER_LOCATION = "Arquivos";

	@GetMapping
	public MappingJacksonValue listByCriteria(Pageable pageable, @RequestParam(required = false) String attributes,
			FileFilterDTO filter) {

		List<SearchCriteria> criterias = FileFilterDTO.buildCriteria(filter);
		Page<FileDTO> pages = this.repository.findAll(pageable).map(converter::from);
		return buildResponse(pages, attributes);
	}

	
	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, 
			RedirectAttributes redirectAttributes) {

		storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}

	// CRIAR O ARQUIVO
	// @PostMapping(path = "/create")
	@PostMapping
	public MappingJacksonValue criarFile(@RequestBody FileDTO dto, @RequestParam(required = false) String attributes) {
		if (Objects.isNull(dto)) {
			throw new HttpMessageNotReadableException("Required request body is missing");
		}
		FileDTO fileDTO = Optional.of(dto).map(this.converter::to).map(this.repository::save).map(this.converter::from)
				.orElseThrow(() -> throwsException("Error"));

		try {
			File myObj = new File(dto.getName());
			if (myObj.createNewFile()) {
				System.out.println("\nARQUIVO CRIADO COM SUCESSO: --> " + myObj.getName());
			} else {
				System.out.println("\n--> ESTE ARQUIVO JA EXISTE! <--\n");
			}
		} catch (IOException e) {
			System.out.println("\n--> UM ERRO OCORREU NA CRIAÇAO DO ARQUIVO! <--\n");
			e.printStackTrace();
		}

		return buildResponse(fileDTO, attributes);
	}

	// DELETAR O ARQUIVO
	// @PostMapping(path = "/delete")
	// @DeleteMapping( path = "/delete")
	@DeleteMapping(path = "{id}")
	/*
	 * public MappingJacksonValue deleteFile(@RequestBody FileDTO dto,
	 * 
	 * @RequestParam(required = false) String attributes) {
	 */
	public void delete(@PathVariable long id) {

		FileDTO dto = this.repository.findByIdAndDeletedIsFalse(id).map(this.converter::from)
				.orElseThrow(() -> throwsException(ErrorMessageKeys.ERROR_FILE_NOT_FOUND_BY_ID, String.valueOf(id)));
		File_ file = this.converter.to(dto);
		file.setDeleted(true);
		file.setId(dto.getId());
		this.repository.save(file);

		/*
		 * if(Objects.isNull(dto)) { throw new
		 * HttpMessageNotReadableException("Required request body is missing"); }
		 * FileDTO fileDTO = Optional.of(dto) .map(this.converter::to)
		 * .map(this.repository::save) .map(this.converter::from) .orElseThrow(() ->
		 * throwsException("Error"));
		 * 
		 * File myObj = new File(dto.getName()); if (myObj.delete()) {
		 * System.out.println("\n--> ARQUIVO DELETADO COM SUCESSO: " + myObj.getName());
		 * } else { System.out.println("\n--> ERRO AO DELETAR O ARQUIVO! <--\n"); }
		 * 
		 * return buildResponse(fileDTO, attributes);
		 */
	}

///////////////////////////////////////////////////////////////////////////	
	@RequestMapping(path = "/download", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(@RequestParam("file") String file_) throws IOException {
		File file = new File(SERVER_LOCATION + File.separator + file_);

		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file_);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");

		java.nio.file.Path path = Paths.get(file.getAbsolutePath());
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

		return ResponseEntity.ok().headers(header).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
	}
////////////////////////////////////////////////////////////////////////////	

	// ESCREVER NO ARQUIVO
	// @PostMapping(path = "/write")
	/*
	 * @PutMapping(path = "/write") public MappingJacksonValue
	 * writeFile(@RequestBody FileDTO dto,
	 * 
	 * @RequestParam(required = false) String attributes) {
	 * 
	 * if(Objects.isNull(dto)) { throw new
	 * HttpMessageNotReadableException("Required request body is missing"); }
	 * FileDTO fileDTO = Optional.of(dto) .map(this.converter::to)
	 * .map(this.repository::save) .map(this.converter::from) .orElseThrow(() ->
	 * throwsException("Error"));
	 * 
	 * try { FileWriter editor = new FileWriter(fileDTO.getName());
	 * editor.write(dto.getConteudo()); editor.close();
	 * System.out.println("\n--> ARQUIVO EDITADO COM SUCESSO! <--\n"); } catch
	 * (IOException e) {
	 * System.out.println("\n--> ERRO AO EDITAR O ARQUIVO! <--\n");
	 * e.printStackTrace(); }
	 * 
	 * return buildResponse(fileDTO, attributes); }
	 */

	// LER O ARQUIVO
	// @PostMapping(path = "/read")
	/*
	 * @GetMapping(path = "/read") public MappingJacksonValue readFile(@RequestBody
	 * FileDTO dto,
	 * 
	 * @RequestParam(required = false) String attributes) { if(Objects.isNull(dto))
	 * { throw new
	 * HttpMessageNotReadableException("Required request body is missing"); }
	 * FileDTO fileDTO = Optional.of(dto) .map(this.converter::to)
	 * .map(this.repository::save) .map(this.converter::from) .orElseThrow(() ->
	 * throwsException("Error"));
	 * 
	 * try { File myObj = new File(dto.getName()); Scanner myReader = new
	 * Scanner(myObj); while (myReader.hasNextLine()) { String data =
	 * myReader.nextLine(); System.out.println("\n"); System.out.println(data);
	 * System.out.println("\n"); } myReader.close(); } catch (FileNotFoundException
	 * e) { System.out.println("\n--> ERRO AO LER O ARQUIVO! <--\n");
	 * e.printStackTrace(); } return buildResponse(fileDTO, attributes); }
	 */

}