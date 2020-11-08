package com.tivo.discovery.service;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivo.discovery.dto.DiscoveryConfig;
import com.tivo.discovery.dto.DiscoveryRequest;
import com.tivo.discovery.dto.Mapper;
import com.tivo.discovery.dto.MapperEntry;

@Component
public class ExecutionEngine {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionEngine.class);
	
	private ExecutorService executorService;
	
	@Value("${chromedriver.path}")
	private String chromeDriverPath;
	
	@Value("${application.mapping.file.path}")
	private String mappingFilePath;
	
	private Mapper adapterMapper;
	
	@PostConstruct
	public void init() {
		executorService = Executors.newFixedThreadPool(1);
		adapterMapper = generateMapper();
	}
	
	public void execute(final DiscoveryRequest request) {
		final MapperEntry selectedMapperEntry = getSelectedMapperEntry(adapterMapper, request.getAdapterName());
		executorService.submit(new DiscoveryExecutableTask(request, new DiscoveryConfig(chromeDriverPath, selectedMapperEntry)));
	}
	
	
	@PreDestroy
	public void destroy() {
		executorService.shutdown();
		if (executorService.isShutdown()) {
			LOGGER.info("ExecutorService has shutdown");
		}
	}
	
	
	private MapperEntry getSelectedMapperEntry(final Mapper mapper, final String mappingName) {
		for (final MapperEntry mapperEntry : mapper.getEntries()) {
			if (mapperEntry.getName().equals(mappingName)) {
				return mapperEntry;
			}
		}
		return null;
	}
	
	private Mapper generateMapper() {
		Mapper mapper = null;
		try {
			mapper = new ObjectMapper().readValue(new File(mappingFilePath), Mapper.class);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return mapper;
	}
	
}
