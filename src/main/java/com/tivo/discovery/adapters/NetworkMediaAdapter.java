package com.tivo.discovery.adapters;

import com.tivo.discovery.dto.AdapterResponse;
import com.tivo.discovery.dto.DiscoveryConfig;

public interface NetworkMediaAdapter {

	AdapterResponse findMedia (final String url, final DiscoveryConfig config);
	
}
