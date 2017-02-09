package com.bmudda.hazelcast.docker.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.DuplicateInstanceNameException;
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

	private static final Logger logger = LoggerFactory.getLogger(HazelcastRunner.class);
	private HazelcastInstanceMgr mgr = null;
	
	public HazelcastRunner(String hazelcastConfigPath) throws Exception {
		
		mgr = new HazelcastInstanceMgr(hazelcastConfigPath);
		mgr.start();
		
		// Sleep for two hours
//		Thread.currentThread();
//		Thread.sleep(7200000);
		
		// shutdown everything
//		mgr.shutdown();
	}
	
	public HazelcastInstanceMgr getHazelcastInstanceMgr(){
		return this.mgr;
	}
	
	private class HazelcastInstanceMgr {
		
		private HazelcastInstance hazelcastInstance = null;
		private static final String INSTANCE_NAME = "DOCKER_HAZELCAST";
		private Config conf = null;
		private String hazelcastConfigFile = null;
		
		public HazelcastInstanceMgr(String hazelcastConfigFile) {
			
			this.hazelcastConfigFile = hazelcastConfigFile;
			this.conf =new ClasspathXmlConfig(this.hazelcastConfigFile);
			//this.conf.setClassLoader(this.getClass().getClassLoader());
			this.conf.setInstanceName(INSTANCE_NAME);
		
		}
		
		@SuppressWarnings("unused")
		public HazelcastInstanceMgr() { }
		
		@SuppressWarnings("unused")
		public HazelcastInstance getInstance() {
			return hazelcastInstance;
		}
		
		public void start() {
			
			try{
				
				hazelcastInstance = Hazelcast.newHazelcastInstance(conf);
				
			} catch(DuplicateInstanceNameException e) {
							
				if (e.getMessage() != null && ( 
						e.getMessage().toLowerCase().indexOf("hazelcast instance is already initialized") != -1 ||
						(e.getMessage().toLowerCase().indexOf("hazelcastinstance with name") != -1 && e.getMessage().toLowerCase().indexOf("already exists") != -1)
						)) {
						
					logger.info("HazelcastRunner() got an already initialized " +
							"message when trying to init Hazelcast from: " + this.hazelcastConfigFile + ", we will just re-purpose the existing instance: " + e.getMessage());
					
					// fetch the already running one
					hazelcastInstance = Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME);
				}
							
			}
		}
		
		public void shutdown() {
			this.hazelcastInstance.shutdown();
		}
		
		@SuppressWarnings("unused")
		public Address getAddress() {
			return this.hazelcastInstance.getCluster().getLocalMember().getAddress();
		}
		
	}

}
