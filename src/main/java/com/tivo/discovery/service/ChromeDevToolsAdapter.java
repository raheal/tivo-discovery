package com.tivo.discovery.service;

import java.util.Optional;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.network.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.tivo.discovery.dto.DiscoveryConfig;
import com.tivo.discovery.dto.Result;

@Component
public class ChromeDevToolsAdapter implements NetworkMediaAdapter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChromeDevToolsAdapter.class);
	
	@Override
	public String findMedia(String url, DiscoveryConfig config) {
		
		LOGGER.info("Running chrome developer tools for URL : {}", url);
		
		final Result result = new Result();
		System.setProperty("webdriver.chrome.driver", config.getChromeDriverPath());
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		ChromeDriver chromeDriver = new ChromeDriver(options);
		
		DevTools chromeDevTools = chromeDriver.getDevTools();
		chromeDevTools.createSession();
		
		chromeDevTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        //set blocked URL patterns
        chromeDevTools.send(Network.setBlockedURLs(ImmutableList.of("*.css", "*.png")));

        //add event listener to verify that css and png are blocked
        chromeDevTools.addListener(Network.requestWillBeSent(), responseReceived -> {
        	
        	if (config.getMappingEntry().getMatchType().equals("contains")) {
        		if (responseReceived.getRequest().getUrl().contains(config.getMappingEntry().getRegex())) {
            		result.setUrlResult(responseReceived.getRequest().getUrl());
            	}
        	}
        	
        	System.out.println("OUT>> "+responseReceived.getRequest().getUrl());
        });
       
		chromeDriver.get(url);
		chromeDevTools.close();
		chromeDriver.quit();
		System.out.println("Error: END");
		System.out.println("Result = "+result.getUrlResult());
		return result.getUrlResult();
	}

}
