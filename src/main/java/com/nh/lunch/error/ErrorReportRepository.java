package com.nh.lunch.error;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorReportRepository extends JpaRepository<ErrorReport, Integer> {
	
}
