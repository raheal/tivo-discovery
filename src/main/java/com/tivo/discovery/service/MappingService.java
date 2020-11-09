package com.tivo.discovery.service;

import java.util.List;

import com.tivo.discovery.dto.Mapper;
import com.tivo.discovery.dto.MapperEntry;

public interface MappingService {

	Mapper getMapper();
	
	List<MapperEntry> getMappingEntries();
	
}
