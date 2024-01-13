package com.rahil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rahil.entities.ApplicationUser;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer>{
	
	@Query("select u from ApplicationUser u where u.email =:email")
	public ApplicationUser getUserByUserName(@Param("email") String email);
	
}
