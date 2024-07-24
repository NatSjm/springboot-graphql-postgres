package com.hillel.ua.graphql.controller;

import com.hillel.ua.graphql.dto.OrganizationRequestDto;
import com.hillel.ua.graphql.entities.Department;
import com.hillel.ua.graphql.entities.Organization;
import com.hillel.ua.graphql.entities.Employee;
import com.hillel.ua.graphql.repository.OrganizationRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller

public class OrganizationController {
    OrganizationRepository repository;

    @Autowired
    public OrganizationController(OrganizationRepository repository) {
        this.repository = repository;
    }

    @MutationMapping
    public Organization newOrganization(@Argument OrganizationRequestDto organization) {
        return repository.save(new Organization(null, organization.getName(), null, null));
    }

    @QueryMapping
    public Iterable<Organization> organizations() {
        return repository.findAll();
    }

    @QueryMapping
    public Organization organization(@Argument Integer id) {
        return repository.findById(id).orElseThrow(()
                -> new RuntimeException("Organization not found"));
    }
    @QueryMapping
    public Optional<Organization> organizationName(@Argument String name) {
        return repository.findByName(name);
    }
    @BatchMapping(typeName = "Organization", field = "employees")
    public Map<Organization, Set<Employee>> employees(List<Organization> organizations) {
        return organizations.stream()
                .collect(Collectors.toMap(organization -> organization, Organization::getEmployees));
    }
    @BatchMapping(typeName = "Organization", field = "departments")
    public Map<Organization, Set<Department>> departments(List<Organization> organizations) {
        return organizations.stream()
                .collect(Collectors.toMap(organization -> organization, Organization::getDepartments));
    }
    @MutationMapping
    public Organization updateOrganization(@Argument OrganizationRequestDto organization, @Argument Integer id) {
        Organization organization1 = repository.findById(id).orElseThrow(()
                -> new RuntimeException("Organization not found"));
        organization1.setName(organization.getName());
        return repository.save(organization1);
    }
    @MutationMapping
    public Organization deleteOrganization(@Argument Integer id) {
     Organization organization = repository.findById(id).orElseThrow(()->
             new RuntimeException("Organization not found"));
     repository.delete(organization);
     return organization;
    }

}
