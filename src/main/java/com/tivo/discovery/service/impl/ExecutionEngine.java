package com.tivo.discovery.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tivo.discovery.dto.DiscoveryConfig;
import com.tivo.discovery.dto.DiscoveryRequest;
import com.tivo.discovery.dto.Mapper;
import com.tivo.discovery.dto.MapperEntry;
import com.tivo.discovery.service.MappingService;

@Component
public class ExecutionEngine {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionEngine.class);
	
	private ExecutorService executorService;

	@Autowired
	private MappingService mappingService;
	
	@Value("${chromedriver.path}")
	private String chromeDriverPath;

	
	@PostConstruct
	public void init() {
		executorService = Executors.newFixedThreadPool(1);
	}
	
	public void execute(final DiscoveryRequest request) {
		final MapperEntry selectedMapperEntry = getSelectedMapperEntry(mappingService.getMapper(), request.getAdapterName());
		executorService.submit(new DiscoveryExecutableTask(request, new DiscoveryConfig(chromeDriverPath, selectedMapperEntry)));
	}
	
	public String executeDiscoveryAndRetrieveLink(final DiscoveryRequest request) {
		final MapperEntry selectedMapperEntry = getSelectedMapperEntry(mappingService.getMapper(), request.getAdapterName());
		return new DiscoveryExecutableTask(request, 
				new DiscoveryConfig(chromeDriverPath, selectedMapperEntry)).retrieveLink();
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
	
}
