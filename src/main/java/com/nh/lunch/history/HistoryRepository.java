package com.nh.lunch.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HistoryRepository extends JpaRepository<History, HistoryId> {
	@Query(
		    value = "SELECT *"
		          + " FROM ("
		          + "    SELECT *"
		          + "    FROM history"
		          + "    WHERE member_id = :memberId"
		          + "    ORDER BY final_date DESC "
		          + " ) "
		          + " WHERE ROWNUM <= 10",
		    nativeQuery = true
		)
	List<History> findTop10ByHistoryId(@Param("memberId") Integer memberId);
}
