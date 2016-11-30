package com.bmudda.util.boot;


import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication(exclude = {})

public class Application {
	
	public static void main(String[] args) {
		
		String appContextClasspathFilename = "springConfigs/common.xml";
		
		Object xmlFileResource = new ClassPathResource(appContextClasspathFilename);

		ApplicationContext ctx = SpringApplication.run(new Object[] {
				xmlFileResource,
				Application.class
				
		}, args); 
		
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
	
}
