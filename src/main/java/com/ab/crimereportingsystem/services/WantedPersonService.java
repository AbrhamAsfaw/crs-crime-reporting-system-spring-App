package com.ab.crimereportingsystem.services;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ab.crimereportingsystem.repositories.WantedPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ab.crimereportingsystem.domains.WantedPerson;

@Service
public class WantedPersonService {
	@Autowired
	private WantedPersonRepository repository;


	public WantedPerson save (WantedPerson wantedPerson) {
		return repository.save(wantedPerson);
	}


	public WantedPerson update(WantedPerson wantedPerson) {
		return repository.save(wantedPerson);
	}


	public void delete(WantedPerson wantedPerson) {
		repository.delete(wantedPerson);

	}
	public void deleteById(Long id) {
		repository.deleteById(id);
	}


	public WantedPerson getById(Long id) {
		if(repository.existsById(id)) {
			return repository.findById(id).get();
		}
		return null;
	}
	
	public List<WantedPerson> getRecent(){
		ArrayList<Long> ids = new ArrayList<Long>();
		Long count = repository.count();
		for (int i = 0; i <3 ; i++) {
			ids.add(count);
			count -=1;
		}
		List<WantedPerson> recent = (List<WantedPerson>) repository.findAllById(ids); 
		Collections.reverse(recent);
		return recent;
	}


	public List<WantedPerson> getAll() {
		 List<WantedPerson> all = (List<WantedPerson>) repository.findAll();
		return all;
	}
	public Iterable<WantedPerson> findAll(Sort sort) {
		return repository.findAll(sort);
	}

	public Page<WantedPerson> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}


}
