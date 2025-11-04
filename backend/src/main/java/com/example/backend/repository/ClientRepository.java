package com.example.backend.repository;

import com.example.backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	@Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.addresses LEFT JOIN FETCH c.phoneNumbers")
	List<Client> findAllWithRelations();

	@Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.addresses LEFT JOIN FETCH c.phoneNumbers WHERE c.id = :id")
	Optional<Client> findByIdWithRelations(@Param("id") Long id);
}
