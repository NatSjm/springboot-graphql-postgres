package com.hillel.ua.graphql.controller;

import com.hillel.ua.graphql.dto.DepartmentRequestDto;
import com.hillel.ua.graphql.entities.Department;
import com.hillel.ua.graphql.entities.Employee;
import com.hillel.ua.graphql.entities.Organization;
import com.hillel.ua.graphql.repository.DepartmentRepository;
import com.hillel.ua.graphql.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final OrganizationRepository organizationRepository;

    @MutationMapping
    public Department newDepartment(@Argument DepartmentRequestDto department) {
        Organization organization = organizationRepository.findById(department.getOrganizationId()).orElseThrow(()
                -> new RuntimeException("Organization not found"));
        return departmentRepository.save(new Department(null, department.getName(), null, organization));
    }
    @MutationMapping
    public Department updateDepartment(@Argument DepartmentRequestDto department, @Argument Integer id) {
        return departmentRepository.findById(id).map(dep -> {
            dep.setName(department.getName());
            return departmentRepository.save(dep);
        }).orElseThrow();
    }
    @MutationMapping
    public Department deleteDepartment(@Argument Integer id) {
       Department department = departmentRepository.findById(id).orElseThrow();
       departmentRepository.delete(department);
       return department;
    }

    @QueryMapping Iterable<Department> departments() {
        return departmentRepository.findAll();
    }
    @QueryMapping Department department(@Argument Integer id) {
        return departmentRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Department not found"));
    }
    @SchemaMapping(typeName = "Department", field = "organization")
    public Organization organization(Department department) {
        return department.getOrganization();
    }
    @BatchMapping(typeName = "Department", field = "employees")
    public Map<Department, Set<Employee>> employees(List<Department> departments) {
        return departments.stream()
                .collect(Collectors.toMap(department -> department, Department::getEmployees));
    }

}
