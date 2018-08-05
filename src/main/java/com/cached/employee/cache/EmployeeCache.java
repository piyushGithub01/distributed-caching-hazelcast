package com.cached.employee.cache;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cached.employee.model.Employee;
import com.cached.employee.model.EmployeeSerializer;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

@Component
public class EmployeeCache {
	
	private Logger logger = Logger.getLogger(EmployeeCache.class.getName());

	IMap<Integer, Employee> map;
	
	@PostConstruct
	public void init() {
		HazelcastInstance instance = getHazelcastClient();
		map = instance.getMap("employee");
		map.addIndex("company", true);
		logger.info("Employees cache size: " + map.size());
	}
	
	private HazelcastInstance getHazelcastClient() {
		ClientConfig config = new ClientConfig();
		config.getGroupConfig().setName("dev").setPassword("dev-pass");
		config.getNetworkConfig().addAddress("192.168.1.3:5701");
		
		//adding serialization to member
		SerializerConfig sc = new SerializerConfig()
				.setTypeClass(Employee.class).setClass(EmployeeSerializer.class)
				.setImplementation(new EmployeeSerializer());
		config.getSerializationConfig().addSerializerConfig(sc);
		
		HazelcastInstance instance = HazelcastClient.newHazelcastClient(config);
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public Optional<Employee> findByPersonId(Integer personId) {
		Predicate predicate = Predicates.equal("personId", personId);
		Collection<Employee> ps = map.values(predicate);
		Optional<Employee> e = ps.stream().findFirst();
		return e;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection<Employee> findByCompany(String company) {
		Predicate predicate = Predicates.equal("company", company);
		Collection<Employee> ps = map.values(predicate);
		return ps;
	}

	public Employee findById(Integer id) {
		return map.get(id);
	}
	
	public void add(Employee e) {
		logger.info("Adding Employee to Cache: " + e);
		map.put(e.getId(), e);
	}

}
