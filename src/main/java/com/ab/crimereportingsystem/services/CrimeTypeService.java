package com.ab.crimereportingsystem.services;

import java.util.Collections;
import java.util.List;

import com.ab.crimereportingsystem.repositories.CrimeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ab.crimereportingsystem.domains.CrimeType;

@Service
public class CrimeTypeService {
	
	@Autowired
	private CrimeTypeRepository repository;


	public CrimeType save (CrimeType crimeType) {
		return repository.save(crimeType);
	}


	public CrimeType update(CrimeType crimeType) {
		return repository.save(crimeType);
	}


	public void delete(CrimeType crimeType) {
		repository.delete(crimeType);

	}


	public CrimeType getById(Long id) {
		return repository.findById(id).get();
	}
	
	public boolean isFound(Long id) {
		return repository.existsById(id);
	}


	public List<CrimeType> getAll() {
		 List<CrimeType> all = (List<CrimeType>) repository.findAll();
		 Collections.reverse(all);
		return all;
	}

}
