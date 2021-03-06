package com.ab.crimereportingsystem.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.ab.crimereportingsystem.repositories.RoleRepository;
import com.ab.crimereportingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ab.crimereportingsystem.domains.Role;
import com.ab.crimereportingsystem.domains.User;

@Service
public class UserService implements UserDetailsService {
	
	
	private UserRepository repository;
	private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

	public void save (User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        repository.save(user);
	}
	
	public void saveSuper(User user) {
		if(repository.findByUsername("super") == null) {
			user.setUsername("super");
			user.setFullname("super");
			user.setPhoneNumber("super");
			user.setStreetAddress("super");
			user.setPassword(bCryptPasswordEncoder.encode("super"));
	        user.setEnabled(1);
	        Role userRole = roleRepository.findByRole("SUPER");
	        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
	        repository.save(user);
		}
		
	}
	public void saveAdmin (User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        repository.save(user);
	}


	public User update(User user) {
		return repository.save(user);
	}


	public void delete(User user) {
		repository.delete(user);

	}


	public User findUserByUsername(String username) {
    	return repository.findByUsername(username);
    }


	public List<User> getAll() {
		 List<User> all = (List<User>) repository.findAll();
		return all;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repository.findByUsername(username);
		
		if(user != null) {
			return user;
		}
		throw new UsernameNotFoundException("User '" + username + "' not found");
	}

}
