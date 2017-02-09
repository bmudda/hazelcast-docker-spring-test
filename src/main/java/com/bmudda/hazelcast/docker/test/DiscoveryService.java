package com.bmudda.hazelcast.docker.test;

import java.util.Collection;

import org.bitsofinfo.docker.discovery.registrator.consul.ServiceInfo;

public interface DiscoveryService {

	public Collection<ServiceInfo> getMyServices();

	public Collection<ServiceInfo> getMyPeerServices();

}
