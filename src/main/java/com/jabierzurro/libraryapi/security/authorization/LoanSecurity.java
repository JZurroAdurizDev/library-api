package com.jabierzurro.libraryapi.security.authorization;

import com.jabierzurro.libraryapi.repository.LoanRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jabier Zurro Aduriz
 */
@Component("loanSecurity")
public class LoanSecurity {

    private final LoanRepository loanRepository;

    public LoanSecurity(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public boolean isOwner(Integer loanId, Integer userId) {
        return loanRepository.findById(loanId)
                .map(loan -> loan.getUser().getId().equals(userId))
                .orElse(false);
    }
}
