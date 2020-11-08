package com.tivo.discovery.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tivo.discovery.dto.DiscoveryRequest;
import com.tivo.discovery.dto.DiscoveryResponse;
import com.tivo.discovery.service.DiscoveryService;

@RequestMapping("api/v1/discovery")
@RestController
public class TivoDiscoveryController {

	@Autowired
	private DiscoveryService discoveryService;
	
	@PostMapping("/submit")
	public DiscoveryResponse submitDiscoveryRequest(@RequestBody DiscoveryRequest request) {
		return discoveryService.submitDiscoveryRequest(request);
	}
	
}
