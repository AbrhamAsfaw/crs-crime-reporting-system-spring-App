package com.ab.crimereportingsystem;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.ab.crimereportingsystem.controllers.AdminStolenVehicleController;
import com.ab.crimereportingsystem.services.StolenVehicleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


public class AdminStolenVehicleControllerTest {
	@Mock
	private StolenVehicleService stolenVehicleService;
	
	@InjectMocks
    AdminStolenVehicleController admiStolenVehicleController;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(admiStolenVehicleController).setViewResolvers(viewResolver()).build();
	}
	
	@Test
	public void testSVPage() throws Exception {
		mockMvc.perform(get("/admin/stolenVehicle"))
		.andExpect(status().isOk())
		.andExpect(view().name("admin_sv"))
		;
	}
	
	private ViewResolver viewResolver()
	{
	    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

	    viewResolver.setPrefix("classpath:templates/");
	    viewResolver.setSuffix(".html");

	    return viewResolver;
	}

}
