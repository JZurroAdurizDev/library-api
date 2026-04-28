package com.jabierzurro.libraryapi.repository;

import com.jabierzurro.libraryapi.entity.Loan;
import com.jabierzurro.libraryapi.entity.LoanStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for {@link Loan} entity persistence operations.
 *
 * <p>This interface extends {@link JpaRepository}, providing standard CRUD
 * operations as well as custom query methods for loan-specific use cases.
 *
 * <p>Includes custom search functionality and existence checks used to
 * enforce business rules in the service layer.
 *
 * @author Jabier Zurro Aduriz
 */
public interface LoanRepository extends JpaRepository<Loan, Integer>{
    
    /**
     * Searches loans based on optional filtering criteria.
     *
     * <p>All parameters are optional. If a parameter is {@code null}, it is ignored
     * in the filtering process.
     *
     * <p>This query is implemented as a native SQL query.
     *
     * @param userId    identifier of the user associated with the loan
     * @param status    loan status as a String (e.g., ACTIVE, CLOSED)
     * @param startDate start date of the loan
     * @param dueDate   due date of the loan
     * @return list of loans matching the given criteria
     */
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
    
    /**
     * Checks whether any active loan exists for the given list of book IDs.
     *
     * <p>Used to prevent assigning books that are already loaned in another
     * active loan.
     *
     * @param status   loan status to check (typically ACTIVE)
     * @param bookIds  list of book identifiers
     * @return {@code true} if at least one matching loan exists, {@code false} otherwise
     */
    boolean existsByStatusAndBooks_IdIn(LoanStatus status, List<Integer> bookIds);

    /**
     * Checks whether a user has an existing loan with a specific status.
     *
     * <p>Used to enforce rules such as allowing only one active loan per user.
     *
     * @param userId     user identifier
     * @param loanStatus loan status to check
     * @return {@code true} if such a loan exists, {@code false} otherwise
     */
    boolean existsByUser_IdAndStatus(Integer userId, LoanStatus loanStatus);
}