package com.rahil.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rahil.entities.ContactDetails;

@Repository
public interface ContactRepository extends JpaRepository<ContactDetails, Integer>{

	@Query("select c from ContactDetails c where c.applicationUser.id=:userId")
	public Page<ContactDetails> findContactsByUser(@Param("userId") int userId, Pageable pageable);
	
}
