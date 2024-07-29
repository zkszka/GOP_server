package com.mysite.missing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.missing.entity.Missing;

public interface MissingRepository extends JpaRepository<Missing, Long> {

}
