package com.bmudda.hazelcast.docker.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.bitsofinfo.docker.discovery.registrator.consul.ConsulDiscovery;
import org.bitsofinfo.docker.discovery.registrator.consul.ServiceInfo;
import org.bitsofinfo.docker.discovery.registrator.consul.ServiceNameStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsulDiscoveryService implements DiscoveryService {

	private static final Logger logger = LoggerFactory.getLogger(ConsulDiscoveryService.class);
	private ConsulDiscovery consulDiscovery = null;
	private String myUniqueTag;
	
	private Map<String, Map<Integer, Set<String>>> ipPortSeedProperties;
	private Map<String, String> propCopyValueIfEmpty;
	
	@SuppressWarnings("unchecked")
	public ConsulDiscoveryService(Map<String, Map<Integer, Set<String>>> ipPortSeedProperties, Map<String, String> propCopyValueIfEmpty) {
		
		try {
			
			this.ipPortSeedProperties = ipPortSeedProperties;
			this.propCopyValueIfEmpty = propCopyValueIfEmpty;
			this.myUniqueTag = System.getProperty("MY_UNIQUE_TAG");
			
			this.consulDiscovery = new ConsulDiscovery()
				.setConsulIp(System.getProperty("CONSUL_IP"))
		        .setConsulPort(Integer.valueOf(System.getProperty("CONSUL_PORT")))
		        .setServiceName(System.getProperty("MY_SERVICE_NAME"))
		        .setMyNodeUniqueTagId(myUniqueTag)
		        .addPortToDiscover(5701)
		        .addPortToDiscover(2552)
		        .setServiceNameStrategyClass((Class<? extends ServiceNameStrategy>) Class
		                .forName(System.getProperty("SERVICE_NAME_STRATEGY")));
			
			this.seedExposedIpAndPort();
			
		} catch (Exception e) {
			throw new RuntimeException("Error constructing ConsulDiscoveryService " + e.getMessage(), e);
		}
	}
	
	
	
	private void seedExposedIpAndPort() throws Exception{
		
		logger.info("=====> ipPortSeedProperties: " + this.ipPortSeedProperties);
		
		this.setSystemProperties("self", this.getMyServices());
		this.setSystemProperties("peer", this.getMyPeerServices());
		
		this.copySystemProps();
		
	}

	
	private void setSystemProperties(String type, Collection<ServiceInfo> services) {
		
		Map<Integer, Set<String>> mapping = this.ipPortSeedProperties.get(type);		
		
		if(mapping != null) {
			
			logger.info("*****  System.setProperty setting from " + type + " services *********");
			for (ServiceInfo info : services) {
				Set<String> mappedPortMapping = mapping.get(info.getMappedPort());
				
				if(mappedPortMapping != null){
					Iterator<String> it = mappedPortMapping.iterator();
					
					while(it.hasNext()){
						
						String[] parts = it.next().split(Pattern.quote("|"));
						if(parts.length == 2){
							System.setProperty(parts[0], info.getExposedAddress().getHostAddress());
							System.setProperty(parts[1], String.valueOf(info.getExposedPort()));
							
							logger.info(parts[0] + " => " + System.getProperty(parts[0]));
							logger.info(parts[1] + " => "  + System.getProperty(parts[1]));
						}else{
							
							if(System.getProperty(parts[0]) != null && !System.getProperty(parts[0]).isEmpty()){
								System.setProperty(parts[0], System.getProperty(parts[0]) + "," + info.getExposedAddress().getHostAddress() + ":" + info.getExposedPort());
								logger.info(parts[0] + " => " + System.getProperty(parts[0]));
							}else{
								System.setProperty(parts[0], info.getExposedAddress().getHostAddress() + ":" + info.getExposedPort());
								logger.info(parts[0] + " => " + System.getProperty(parts[0]));
							}
		
						}
					}
				}
            }
			logger.info("**************************************************************\n\n");
		}
		
	}
	
	private void copySystemProps() {
			
			logger.info("*****  System.setProperty setting from copySystemProps() *****");
			for(Map.Entry<String,String>  copyEntry : this.propCopyValueIfEmpty.entrySet()){
				
				String key = copyEntry.getKey();
				String value = copyEntry.getValue();
				
				if( System.getProperty(key)  == null) {
					System.setProperty(key, System.getProperty(value));
					logger.info(key + " => " + System.getProperty(key));
				}
				
			}
			logger.info("**************************************************************\n\n");
	}
	
	@Override
	public Collection<ServiceInfo> getMyServices() {
		
		Collection<ServiceInfo> myServices = new ArrayList<ServiceInfo>();
		try {
			myServices = consulDiscovery.discoverMe();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return myServices;
	}

	@Override
	public Collection<ServiceInfo> getMyPeerServices() {
		
		Collection<ServiceInfo> myPeerServices = new ArrayList<ServiceInfo>();
		try {
			myPeerServices = consulDiscovery.discoverPeers();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return myPeerServices;
	}

}
