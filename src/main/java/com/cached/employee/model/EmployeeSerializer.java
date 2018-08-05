package com.cached.employee.model;

import java.io.IOException;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;


public class EmployeeSerializer implements StreamSerializer<Employee> {

	@Override
	public int getTypeId() {
		return 5;
	}

	@Override
	public void write(ObjectDataOutput out, Employee employee) throws IOException {
		out.writeInt(employee.getId());
		out.writeInt(employee.getPersonId());
		out.writeUTF(employee.getCompany());
	}

	@Override
	public Employee read(ObjectDataInput in) throws IOException {
		Employee e = new Employee();
		e.setId(in.readInt());
		e.setPersonId(in.readInt());
		e.setCompany(in.readUTF());
		return e;
	}

	@Override
	public void destroy() {
	}

}
