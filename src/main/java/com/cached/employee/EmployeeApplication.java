package com.cached.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cached.employee.model.Employee;
import com.cached.employee.model.EmployeeSerializer;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = JmxAutoConfiguration.class)
@EnableTransactionManagement(proxyTargetClass=true)
@EntityScan(basePackages = "com.cached.employee.model")
@EnableJpaRepositories(basePackages = "com.cached.employee.repository")
@EnableSwagger2
public class EmployeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeApplication.class, args);
	}
	
// 	@Bean
//	HazelcastInstance hazelcastInstance() {
// 		//Client Config for connection
//		ClientConfig config = new ClientConfig();
//		config.getGroupConfig().setName("dev").setPassword("dev-pass");
//		config.getNetworkConfig().addAddress("192.168.1.3:5701");
//		
//		//adding serialization to member
//		SerializerConfig sc = new SerializerConfig()
//				.setTypeClass(Employee.class).setClass(EmployeeSerializer.class)
//				.setImplementation(new EmployeeSerializer());
//		config.getSerializationConfig().addSerializerConfig(sc);
//		
//		HazelcastInstance instance = HazelcastClient.newHazelcastClient(config);
//		return instance;
//	}
 	
 	@Bean
	Config config() {
		Config c = new Config();
		c.setInstanceName("cache-1");
		c.getGroupConfig().setName("dev").setPassword("dev-pass");
		ManagementCenterConfig mcc = new ManagementCenterConfig()
				.setUrl("http://192.168.99.100:38080/mancenter").setEnabled(true);
		c.setManagementCenterConfig(mcc);
		SerializerConfig sc = new SerializerConfig().setTypeClass(Employee.class).setClass(EmployeeSerializer.class);
		c.getSerializationConfig().addSerializerConfig(sc);
		return c;
	}

}
