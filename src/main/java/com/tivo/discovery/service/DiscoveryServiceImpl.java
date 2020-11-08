package com.tivo.discovery.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tivo.discovery.dto.DiscoveryRequest;
import com.tivo.discovery.dto.DiscoveryResponse;

@Service
public class DiscoveryServiceImpl implements DiscoveryService{

	@Autowired
	private ExecutionEngine executionEngine;
	
	@Override
	public DiscoveryResponse submitDiscoveryRequest(DiscoveryRequest request) {
		executionEngine.execute(request);
		final DiscoveryResponse response = new DiscoveryResponse();
		response.setUrl(request.getUrl());
		response.setTimestamp(LocalDateTime.now());
		return response;
	}

}
