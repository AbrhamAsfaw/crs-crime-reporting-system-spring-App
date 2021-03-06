package com.ab.crimereportingsystem.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ab.crimereportingsystem.domains.WantedPerson;
import com.ab.crimereportingsystem.services.FileStorageService;
import com.ab.crimereportingsystem.services.WantedPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@CrossOrigin(origins="*")
@RestController
@RequestMapping(path = "/api/wantedPerson", produces="application/json" )
public class WantedPersonRestController {
	@Autowired
    WantedPersonService wantedPersonService;
	@Autowired
    FileStorageService fileStorageService;
	ObjectMapper objectMapper = new ObjectMapper();
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public WantedPerson createWantedPerson(
			@RequestParam(value="wantedpersonJson", required = true ) String wantedpersonJson,
			@RequestParam(required = true, value="file") MultipartFile file) 
			throws JsonParseException, JsonMappingException, IOException{
		String fileName = fileStorageService.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/wantedPerson/downloadFile/").path(fileName).toUriString();
		WantedPerson wantedPerson = objectMapper.readValue(wantedpersonJson, WantedPerson.class);
		wantedPerson.setPicturePath(fileDownloadUri);
		wantedPersonService.save(wantedPerson);
		
		return wantedPerson;
		
	}
	@GetMapping
	public List<WantedPerson> getAllWantedPerson(){
		return wantedPersonService.getAll();
	}
	@GetMapping("/{id}")
	public ResponseEntity<WantedPerson> getWantedPersonById(@PathVariable("id") Long id){
		WantedPerson wantedPerson =wantedPersonService.getById(id);
		if(wantedPerson != null) {
			return new ResponseEntity<>(wantedPerson, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteWantedPersonById(@PathVariable("id") Long id){
		try {
		wantedPersonService.deleteById(id);
		}
		catch(EmptyResultDataAccessException e){
			
		}
	}
	
	@GetMapping("/recent")
	public Iterable<WantedPerson> recentWantedPerson(@RequestParam(name="page",defaultValue="0") int page, @RequestParam(name="size", defaultValue="3") int size) {
		
		PageRequest pageRequest = PageRequest.of(page,size,Sort.by("createdAt").descending());
		return wantedPersonService.findAll(pageRequest).getContent();
	}
	
	
	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
		Resource resource  = fileStorageService.loadFileAsResource(fileName);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		if(contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=\"%s\"", resource.getFilename()))
				.body(resource);
		
	}

}
