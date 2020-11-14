package com.tivo.discovery.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tivo.discovery.dto.DiscoveryRequest;
import com.tivo.discovery.dto.DiscoveryResponse;
import com.tivo.discovery.service.DiscoveryService;
import com.tivo.discovery.service.MappingService;

@RequestMapping("api/v1/discovery")
@RestController
public class TivoDiscoveryController {

	@Autowired
	private DiscoveryService discoveryService;
	
	
	@PostMapping("/submit")
	public DiscoveryResponse submitDiscoveryRequest(@RequestBody DiscoveryRequest request) {
		return discoveryService.submitDiscoveryRequest(request);
	}
	
	@GetMapping("/link")
	public DiscoveryResponse getDiscoveryLink(@RequestBody DiscoveryRequest request) {
		return discoveryService.getDiscoveryLink(request);
	}

}
