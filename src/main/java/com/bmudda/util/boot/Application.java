package com.bmudda.util.boot;


import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class Application {
	
	private static final String OPTION_CONSUL_SERVER_IP = "consulserverip";
	private static final String OPTION_CONSUL_SERVER_PORT = "consulserverport";
	
	public static void main(String[] args) {
		
		System.out.println("Main() received arguments: " + Arrays.toString(args));
		
		Options options = new Options();
		
		options.addOption(
				Option.builder("csip").longOpt(OPTION_CONSUL_SERVER_IP).hasArg(true).numberOfArgs(1)
				.desc("Optional: Consul Server IP address for Hazelcast discovery service").build()
			).addOption(
				Option.builder("cspo").longOpt(OPTION_CONSUL_SERVER_PORT).hasArg(true).numberOfArgs(1)
				.desc("Optional: Consul Server IP port for Hazelcast discovery service (default to port 8500)").build()
			);
		
		 // create the parser
	    CommandLineParser parser = new DefaultParser();
	    CommandLine commandLine = null;
	    try {
	        // parse the command line arguments
	    	commandLine = parser.parse( options, args );
	    } catch( ParseException e ) {
	        // oops, something went wrong
	       throw new RuntimeException( "Parsing failed.  Reason: " + e.getMessage(), e);
	    }
	    
	    
	    if (optionPresent(commandLine,OPTION_CONSUL_SERVER_IP)) {
			System.setProperty("CONSUL_IP", commandLine.getOptionValue(OPTION_CONSUL_SERVER_IP));
			System.setProperty("hz.consul.server.ip", commandLine.getOptionValue(OPTION_CONSUL_SERVER_IP));
			
		}
	    
	    if (optionPresent(commandLine,OPTION_CONSUL_SERVER_PORT)) {
			System.setProperty("CONSUL_PORT", commandLine.getOptionValue(OPTION_CONSUL_SERVER_PORT));
			System.setProperty("hz.consul.server.port", commandLine.getOptionValue(OPTION_CONSUL_SERVER_PORT));
			
		}else {
			System.setProperty("CONSUL_PORT", "8500");
			System.setProperty("hz.consul.server.port", "8500");
			
		}
	    
		
		String appContextClasspathFilename = "springConfigs/common.xml";
		
		Object xmlFileResource = new ClassPathResource(appContextClasspathFilename);

		ApplicationContext ctx = SpringApplication.run(new Object[] {
				xmlFileResource,
				Application.class
				
		}, args); 

    }
	
	
	private static boolean optionPresent(CommandLine commandLine, String optionName) {
		return commandLine.hasOption(optionName) && 
			 commandLine.getOptionValue(optionName) != null && 
			 !commandLine.getOptionValue(optionName).trim().isEmpty() &&
			 !commandLine.getOptionValue(optionName).trim().equalsIgnoreCase("null");
	}
	
}
