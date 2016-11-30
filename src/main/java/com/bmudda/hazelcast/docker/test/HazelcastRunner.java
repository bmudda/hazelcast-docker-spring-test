package com.bmudda.hazelcast.docker.test;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.nio.Address;

/**
 * 
 * Simple test class to run a Hazelcast instance
 * 
 * @author bonaya.mudda
 *
 */
public class HazelcastRunner {
	
	private String hazelcastConfigPath = null;
	private HazelcastInstance hazelcastInstance = null;
	private static final String INSTANCE_NAME = "DOCKER_HAZELCAST";
	private Config conf = null;
	
	
	public HazelcastRunner(String hazelcastConfigPath) throws Exception {
		
		this.hazelcastConfigPath = hazelcastConfigPath;
		Thread.currentThread().setContextClassLoader(HazelcastInstance.class.getClassLoader());
//		this.conf =new ClasspathXmlConfig(this.getClass().getClassLoader(), this.hazelcastConfigPath);
		this.conf = new ClasspathXmlConfig(this.hazelcastConfigPath);
		//this.conf.setClassLoader();
		this.conf.setInstanceName(INSTANCE_NAME);
		
		this.hazelcastInstance = Hazelcast.newHazelcastInstance(conf);
		
		// Sleep for two hours
		//Thread.currentThread();
		//Thread.sleep(7200000);
				
		this.hazelcastInstance.shutdown();
		
		/*
		
		HazelcastInstanceMgr mgr = new HazelcastInstanceMgr(hazelCastConfigPath);
		mgr.start();
		
		// Sleep for two hours
		Thread.currentThread();
		Thread.sleep(7200000);
		
		
		// shutdown everything
		mgr.shutdown();
		
		*/
		
		System.exit(0);
	}
	
	/*private class HazelcastInstanceMgr {
		
		private HazelcastInstance hazelcastInstance = null;
		private static final String INSTANCE_NAME = "DOCKER_HAZELCAST";
		private Config conf = null;
		
		public HazelcastInstanceMgr(String hazelcastConfigFile) {
			this.conf =new ClasspathXmlConfig(hazelcastConfigFile);
			this.conf.setClassLoader(this.getClass().getClassLoader());
			this.conf.setInstanceName(INSTANCE_NAME);
		}
		
		@SuppressWarnings("unused")
		public HazelcastInstanceMgr() { }
		
		@SuppressWarnings("unused")
		public HazelcastInstance getInstance() {
			return hazelcastInstance;
		}
		
		public void start() {
			hazelcastInstance = Hazelcast.newHazelcastInstance(conf);
		}
		
		public void shutdown() {
			this.hazelcastInstance.shutdown();
		}
		
		@SuppressWarnings("unused")
		public Address getAddress() {
			return this.hazelcastInstance.getCluster().getLocalMember().getAddress();
		}
		
	}*/

}
