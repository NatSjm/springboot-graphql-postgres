package com.hillel.ua.graphql.controller;

import com.hillel.ua.graphql.dto.EmployeeRequestDto;
import com.hillel.ua.graphql.dto.PageableRequest;
import com.hillel.ua.graphql.entities.Department;
import com.hillel.ua.graphql.entities.Employee;
import com.hillel.ua.graphql.entities.Organization;
import com.hillel.ua.graphql.filter.EmployeeFilter;
import com.hillel.ua.graphql.filter.FilterField;
import com.hillel.ua.graphql.repository.DepartmentRepository;
import com.hillel.ua.graphql.repository.EmployeeRepository;
import com.hillel.ua.graphql.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
public class EmployeeController {

    DepartmentRepository departmentRepository;
    EmployeeRepository employeeRepository;
    OrganizationRepository organizationRepository;

    @Autowired
    EmployeeController(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository,
                       OrganizationRepository organizationRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.organizationRepository = organizationRepository;
    }

    @QueryMapping
    public Iterable<Employee> employees() {
        return employeeRepository.findAll();
    }

    @QueryMapping
    public Employee employee(@Argument Integer id) {
        return employeeRepository.findById(id).orElseThrow();
    }

    @MutationMapping
    public Employee newEmployee(@Argument EmployeeRequestDto employee) {
        Department department = departmentRepository.findById(employee.getDepartmentId()).get();
        Organization organization = organizationRepository.findById(employee.getOrganizationId()).get();
        return employeeRepository.save(new Employee(null, employee.getFirstName(), employee.getLastName(),
                employee.getPosition(),  employee.getSalary(), employee.getAge(), department, organization));
    }
    @MutationMapping
    public Employee updateEmployee(@Argument EmployeeRequestDto employee, @Argument Integer id) {
        return employeeRepository.findById(id).map(emp -> {
            emp.setFirstName(employee.getFirstName());
            emp.setLastName(employee.getLastName());
            emp.setAge(employee.getAge());
            emp.setPosition(employee.getPosition());
            emp.setSalary(employee.getSalary());
            return employeeRepository.save(emp);
        }).orElseThrow();
    }
    @MutationMapping
    public Employee deleteEmployee(@Argument Integer id) {
    Employee employee = employeeRepository.findById(id).orElseThrow();
    employeeRepository.delete(employee);
    return employee;
    }

    @QueryMapping
    public Iterable<Employee> employeesWithFilter(@Argument EmployeeFilter filter) {
        Specification<Employee> spec = buildSpecification(filter);
        if (spec != null) return employeeRepository.findAll(spec);
        else return employeeRepository.findAll();
    }

    @QueryMapping
    public Page<Employee> employeesPageable(@Argument PageableRequest pageableRequest) {
        Pageable pageable = PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize());
        return employeeRepository.findAll(pageable);
    }

    @QueryMapping
    public Page<Employee> employeesWithFilterPageable(@Argument EmployeeFilter filter,
                                                      @Argument PageableRequest pageableRequest) {
        Specification<Employee> spec = buildSpecification(filter);
        Pageable pageable = PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize());
        return employeeRepository.findAll(spec, pageable);
    }

    private Specification<Employee> buildSpecification(EmployeeFilter filter) {
        Specification<Employee> spec = null;
        if (filter.getSalary() != null) spec = bySalary(filter.getSalary());
        if (filter.getAge() != null) spec = (spec == null ? byAge(filter.getAge()) : spec.and(byAge(filter.getAge())));
        if (filter.getPosition() != null)
            spec = (spec == null ? byPosition(filter.getPosition()) : spec.and(byPosition(filter.getPosition())));
        return spec;
    }



    private Specification<Employee> bySalary(FilterField filterField) {
        return (root, query, builder) -> filterField.generateCriteria(builder, root.get("salary"));
    }

    private Specification<Employee> byAge(FilterField filterField) {
        return (root, query, builder) -> filterField.generateCriteria(builder, root.get("age"));
    }

    private Specification<Employee> byPosition(FilterField filterField) {
        return (root, query, builder) -> filterField.generateCriteria(builder, root.get("position"));
    }
}
