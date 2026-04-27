package com.jabierzurro.libraryapi.repository;

import com.jabierzurro.libraryapi.entity.Loan;
import com.jabierzurro.libraryapi.entity.LoanStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public interface LoanRepository extends JpaRepository<Loan, Integer>{
    
    @Query(value = """
        SELECT * 
        FROM loans l
        WHERE (:userId IS NULL OR l.user_id = :userId)
            AND (:status IS NULL OR l.status = :status)
            AND (:startDate IS NULL OR l.start_date = :startDate)
            AND (:dueDate IS NULL OR l.due_date = :dueDate)
                   """, nativeQuery = true)
    List<Loan> searchLoans(
            @Param("userId") Integer userId,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("dueDate") LocalDate dueDate
    );
    
    boolean existsByStatusAndBooks_IdIn(LoanStatus status, List<Integer> bookIds);
    boolean existsByUser_IdAndStatus(Integer userId, LoanStatus loanStatus);
}