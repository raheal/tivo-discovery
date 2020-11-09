package com.tivo.discovery.service;

import com.tivo.discovery.dto.DiscoveryRequest;
import com.tivo.discovery.dto.DiscoveryResponse;

public interface DiscoveryService {

	DiscoveryResponse submitDiscoveryRequest(final DiscoveryRequest request);
	
	DiscoveryResponse getDiscoveryLink(final DiscoveryRequest request);
	
}
