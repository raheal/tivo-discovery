package com.tivo.discovery.service;

import com.tivo.discovery.dto.DiscoveryRequest;
import com.tivo.discovery.dto.DiscoveryResponse;

public interface DiscoveryService {

	/**
	 * Discovery the link and submit to the download service
	 * @param request - the request
	 * @return -the response containing the link
	 */
	
	DiscoveryResponse submitDiscoveryRequest(final DiscoveryRequest request);
	
	/**
	 * Discover the link and return the result to the client
	 * @param request - the request
	 * @return - the response containing the link
	 */
	
	DiscoveryResponse getDiscoveryLink(final DiscoveryRequest request);
	
}
