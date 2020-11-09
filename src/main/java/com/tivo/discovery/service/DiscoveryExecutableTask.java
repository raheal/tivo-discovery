package com.tivo.discovery.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.tivo.discovery.builders.DownloadRequestBuilder;
import com.tivo.discovery.dto.DiscoveryConfig;
import com.tivo.discovery.dto.DiscoveryRequest;
import com.tivo.discovery.dto.DownloadRequest;
import com.tivo.discovery.dto.DownloadResponse;

import reactor.core.publisher.Mono;

public class DiscoveryExecutableTask implements Runnable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryExecutableTask.class);
	
	private DiscoveryRequest request;
	
	private DiscoveryConfig config;
	
	private NetworkMediaAdapter adapter;
	
	public DiscoveryExecutableTask(final DiscoveryRequest request, final DiscoveryConfig config) {
		this.request = request;
		this.config = config;
		this.adapter = new ChromeDevToolsAdapter();
	}
	
	public String retrieveLink() {
		return adapter.findMedia(request.getUrl(), config);
	}
	
	public void execute() {
		String link = adapter.findMedia(request.getUrl(), config);
		System.out.println("LINK>>> "+link);
		if (link != null) {
			final DownloadRequest request = new DownloadRequestBuilder()
					.setAutoRestart(false)
					.setUrl(link)
					.setOutputFileName(UUID.randomUUID().toString())
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
