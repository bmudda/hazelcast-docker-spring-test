package com.bmudda.hazelcast.docker.test;

import org.slf4j.Logger;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.DuplicateInstanceNameException;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

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
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(HazelcastRunner.class);
	
	public HazelcastRunner(String hazelcastConfigPath) throws Exception {
		
		
		try {
			this.hazelcastConfigPath = hazelcastConfigPath;
			
			ClassLoader tccl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(HazelcastInstance.class.getClassLoader());
	//		this.conf =new ClasspathXmlConfig(this.getClass().getClassLoader(), this.hazelcastConfigPath);
			this.conf = new ClasspathXmlConfig(this.hazelcastConfigPath);
			//this.conf.setClassLoader();
			this.conf.setInstanceName(INSTANCE_NAME);
			
			this.hazelcastInstance = Hazelcast.newHazelcastInstance(conf);
			
			Thread.currentThread().setContextClassLoader(tccl);
			
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
			
			//System.exit(0);
		
		} catch(DuplicateInstanceNameException e) {
				
				if (e.getMessage() != null && ( 
						e.getMessage().toLowerCase().indexOf("hazelcast instance is already initialized") != -1 ||
						(e.getMessage().toLowerCase().indexOf("hazelcastinstance with name") != -1 && e.getMessage().toLowerCase().indexOf("already exists") != -1)
						)) {
						
					logger.info("HazelcastRunner() got an already initialized " +
							"message when trying to init Hazelcast from: " + hazelcastConfigPath + ", we will just re-purpose the existing instance: " + e.getMessage());
					
					// fetch the already running one
					hazelcastInstance = Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME);
				}
				
		}
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
