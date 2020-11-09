package com.tivo.discovery.service;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivo.discovery.dto.Mapper;
import com.tivo.discovery.dto.MapperEntry;

@Service
public class MappingServiceImpl implements MappingService{

	private static final Logger LOGGER = LoggerFactory.getLogger(MappingServiceImpl.class);
	
	@Value("${application.mapping.file.path}")
	private String mappingFilePath;
	
	private Mapper mapper;
	
	@PostConstruct
	private void generateMapper() {
		try {
			mapper = new ObjectMapper().readValue(new File(mappingFilePath), Mapper.class);
			LOGGER.info("Loaded {} mappings from {}", mapper.getEntries().size(), mappingFilePath);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	@Override
	public List<MapperEntry> getMappingEntries() {
		if (mapper != null) {
			return mapper.getEntries();
		}
		return null;
	}

	@Override
	public Mapper getMapper() {
		return mapper;
	}

}
