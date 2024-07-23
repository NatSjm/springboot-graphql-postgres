package com.hillel.ua.graphql.repository;

import com.hillel.ua.graphql.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrganizationRepository extends
        CrudRepository<Organization, Integer>, JpaSpecificationExecutor<Organization> {
    @Query("select o from Organization o where o.name = :name")
    Optional<Organization> findByName(@Param("name") String name);
}

