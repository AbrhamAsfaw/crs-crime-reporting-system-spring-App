package com.ab.crimereportingsystem.controllers;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ab.crimereportingsystem.domains.WantedPerson;
import com.ab.crimereportingsystem.exception.FileStorageException;
import com.ab.crimereportingsystem.services.FileStorageService;
import com.ab.crimereportingsystem.services.WantedPersonService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("admin/wantedPerson")
public class AdminWantedPersonController {
	private WantedPersonService wpService;
	private FileStorageService fileStorageService;
	
	@Autowired
	public AdminWantedPersonController(WantedPersonService wpService, FileStorageService fileStorageService) {
		this.wpService = wpService;
		this.fileStorageService = fileStorageService;
		}
	
	@ModelAttribute(name="wantedPerson")
	public WantedPerson wp(Model model) {
		return new WantedPerson();
	}
	
	@ModelAttribute(name="all_wp")
	public List<WantedPerson> getAll() {
		List<WantedPerson> allWp = wpService.getAll(); 
		return allWp; 
	}
	
	@GetMapping
	public String showForm() {
		return "admin_wp";
	}
	
	@RequestMapping("deleteWp")
	public String deleteWp(@RequestParam("id") Long id) {
		WantedPerson wp = new WantedPerson();
		wp.setId(id);
		wpService.delete(wp);
		return "redirect:/admin/wantedPerson/#wp_list";
		}
	
	@PostMapping
	public String processPost( @RequestParam("file") MultipartFile f, @Valid WantedPerson wantedPerson, Errors errors) throws IOException{
		if (errors.hasErrors()) {
			return "admin_wp";
		}
		String fileName;
        try {
        	 fileName = fileStorageService.storeFile(f);
        }
        
        catch (FileStorageException e) {
        	return "admin_wp";
        }
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/wantedPerson/downloadFile/").path(fileName).toUriString();
		wantedPerson.setPicturePath(fileDownloadUri);
		WantedPerson savedWp = wpService.save(wantedPerson);
		log.info("News object after persisting: " + savedWp);
		
		return "redirect:/admin/wantedPerson/#wp_list";	
	}
	
	@GetMapping("/edit/{id}")
	public String ShowEditForm(@PathVariable("id") Long id, Model model) {
		WantedPerson wantedPerson = wpService.getById(id);
		model.addAttribute("wantedPerson",wantedPerson);
		return "admin_edit_wp";	
	}
	
	@PostMapping("/edit/{id}")
	public String processUpdate(@PathVariable("id") Long id, @RequestParam("file") MultipartFile f,@Valid WantedPerson wantedPerson, Errors errors){
		if (errors.hasErrors()) {
			wantedPerson.setId(id);
			return "admin_edit_wp";
		}
		String fileName;
		try {
			fileName = fileStorageService.storeFile(f);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/wantedPerson/downloadFile/").path(fileName).toUriString();
			wantedPerson.setPicturePath(fileDownloadUri);
		}
		catch(FileStorageException e){
			wantedPerson.setPicturePath(wpService.getById(id).getPicturePath());
			wpService.update(wantedPerson);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wpService.update(wantedPerson);
		return "redirect:/admin/wantedPerson/#wp_list";	
	}
}

