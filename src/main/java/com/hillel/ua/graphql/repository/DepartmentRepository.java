package com.hillel.ua.graphql.repository;

import com.hillel.ua.graphql.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Integer>, JpaSpecificationExecutor<Department> {
}
