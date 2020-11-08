package com.tivo.discovery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiscoveryConfig {

	private String chromeDriverPath;
	
	private MapperEntry mappingEntry;
	
}
