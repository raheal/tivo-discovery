package com.tivo.discovery.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscoveryResponse {

	private String url;
	
	private LocalDateTime timestamp;
	
}
