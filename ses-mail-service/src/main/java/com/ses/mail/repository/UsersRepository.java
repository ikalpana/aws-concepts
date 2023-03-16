package com.ses.mail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ses.mail.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	

}
