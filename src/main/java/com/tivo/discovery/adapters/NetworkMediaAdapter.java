package com.tivo.discovery.adapters;

import com.tivo.discovery.dto.DiscoveryConfig;

public interface NetworkMediaAdapter {

	String findMedia (final String url, final DiscoveryConfig config);
	
}
