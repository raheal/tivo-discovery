package com.tivo.discovery.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.tivo.discovery.adapters.NetworkMediaAdapter;
import com.tivo.discovery.adapters.NetworkMediaAdapterFactory;
import com.tivo.discovery.adapters.impl.ChromeDevToolsAdapter;
import com.tivo.discovery.adapters.impl.HtmlSourceCodeAdapter;
import com.tivo.discovery.builders.DownloadRequestBuilder;
import com.tivo.discovery.dto.AdapterResponse;
import com.tivo.discovery.dto.DiscoveryConfig;
import com.tivo.discovery.dto.DiscoveryRequest;
import com.tivo.discovery.dto.DownloadRequest;
import com.tivo.discovery.dto.DownloadResponse;
import com.tivo.discovery.util.Util;

import reactor.core.publisher.Mono;

public class DiscoveryExecutableTask implements Runnable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryExecutableTask.class);
	
	private DiscoveryRequest request;
	
	private DiscoveryConfig config;
	
	private NetworkMediaAdapter adapter;
	
	public DiscoveryExecutableTask(final DiscoveryRequest request, final DiscoveryConfig config) {
		this.request = request;
		this.config = config;
		this.adapter = NetworkMediaAdapterFactory.getAdapter(config.getMappingEntry().getAdapter());
	}
	
	public String retrieveLink() {
		return adapter.findMedia(request.getUrl(), config).getUrl();
	}
	
	public void execute() {
		final AdapterResponse adapterResponse = adapter.findMedia(request.getUrl(), config);
		if (adapterResponse.getUrl() != null) {
			final DownloadRequest request = new DownloadRequestBuilder()
					.setAutoRestart(false)
					.setUrl(adapterResponse.getUrl())
					.setOutputFileName(adapterResponse.getResourceTitle() != null ? Util.truncate(adapterResponse.getResourceTitle(), 30) : UUID.randomUUID().toString())
					.setStartFileNumber(1)
					.setEndFileNumber(2000)
					.setIsStream(config.getMappingEntry().isStream())
					.setProcessingPlan("STRAIGHT_THROUGH")
					.setResumeDownload(false)
					.build();
			
			// send request to the download-service
			Mono<DownloadResponse> response = triggerDownload(request, config);
			final DownloadResponse resp = response.block();
			LOGGER.info("Task ID : {}, Status : {}", resp.getTaskId(), resp.getStatus());
		
		} else {
			LOGGER.warn("Unable to process request to download service");
		}		
	}
	
	@Override
	public void run() {
		execute();
	}
		
	public Mono<DownloadResponse> triggerDownload(final DownloadRequest request, final DiscoveryConfig config) {
		WebClient webClient = WebClient.builder()
				.baseUrl("http://localhost:8080")
				.build();
		
		return webClient.post()
				.uri("/api/v1/download")
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.retrieve()
				.bodyToMono(DownloadResponse.class);
	}

}
