package com.hillel.ua.graphql.repository;

import com.hillel.ua.graphql.entities.Employee;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository extends
        CrudRepository<Employee, Integer>, JpaSpecificationExecutor<Employee>, PagingAndSortingRepository<Employee, Integer> {
}
