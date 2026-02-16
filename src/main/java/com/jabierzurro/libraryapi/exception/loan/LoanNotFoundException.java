/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jabierzurro.libraryapi.exception.loan;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public class LoanNotFoundException extends NotFoundException {
    public LoanNotFoundException(Integer id) {
        super("Loan with id " + id + " not found");
    }
}
