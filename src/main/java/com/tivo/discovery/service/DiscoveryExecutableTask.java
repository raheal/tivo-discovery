package com.tivo.discovery.service;

import com.tivo.discovery.dto.DiscoveryConfig;
import com.tivo.discovery.dto.DiscoveryRequest;

public class DiscoveryExecutableTask implements Runnable{
	
	private DiscoveryRequest request;
	
	private DiscoveryConfig config;
	
	public DiscoveryExecutableTask(final DiscoveryRequest request, final DiscoveryConfig config) {
		this.request = request;
		this.config = config;
	}
	
	@Override
	public void run() {
		final NetworkMediaAdapter adapter = new ChromeDevToolsAdapter();
		final String link = adapter.findMedia(request.getUrl(), config);
		System.out.println("LINK>>> "+link);
	}

}
