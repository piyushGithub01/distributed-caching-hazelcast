package com.cached.employee.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cached.employee.cache.EmployeeCache;
import com.cached.employee.model.Employee;
import com.cached.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private Logger logger = Logger.getLogger(EmployeeService.class.getName());

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeCache employeeCache;
	
	@SuppressWarnings("rawtypes")
	public Employee findByPersonId(Integer personId) {
		Optional<Employee> e = employeeCache.findByPersonId(personId);
		if(e.isPresent()) {
			logger.info("Employee From Cache: " + e.get());
			return e.get();
		}
		Employee emp = employeeRepository.findByPersonId(personId);
		logger.info("Employee From Database: " + emp);
		employeeCache.add(emp);
		return emp;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Employee> findByCompany(String company) {
		Collection<Employee> cEmp = employeeCache.findByCompany(company);
		if (cEmp.size() > 0) {
			logger.info("Employee From Cache: " + cEmp.toString());
			return cEmp.stream().collect(Collectors.toList());
		}
		List<Employee> e = employeeRepository.findByCompany(company);
		logger.info("Employee From Database: " + e.toString());
		e.parallelStream().forEach(x -> employeeCache.add(x));
		return e;
	}

	public Employee findById(Integer id) {
		Employee e = employeeCache.findById(id);
		if(e != null) {
			logger.info("Employee From Cache: " + e);
			return e;
		}
		Optional<Employee> oe = employeeRepository.findById(id);
		logger.info("Employee From Database: " + oe);
		if (oe.isPresent()) {
			e = oe.get();
			employeeCache.add(e);
		}
		return e;
	}
	
	public Employee add(Employee e) {
		Employee emp = employeeRepository.save(e);
		logger.info("Employee Added to Database: " + emp);
		employeeCache.add(emp);
		logger.info("Employee Added to Cache: " + emp);
		return e;
	}
	
}
