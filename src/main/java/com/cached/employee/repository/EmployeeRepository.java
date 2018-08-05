package com.cached.employee.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cached.employee.model.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

	public Employee findByPersonId(Integer personId);
	public List<Employee> findByCompany(String company);
	
}
