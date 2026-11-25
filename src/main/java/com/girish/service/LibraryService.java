package com.girish.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.girish.exception.DataAccessException;
import com.girish.model.Book;
import com.girish.repository.BookRepoImpl;
import com.girish.repository.TransactionsRepo;
import com.girish.util.DBConnection;

public class LibraryService {  private final BookRepoImpl bookRepoImpl= new BookRepoImpl();
private final TransactionsRepo transactionRepo = new TransactionsRepo();

public void addBook(Book book) {
	bookRepoImpl.add(book);
}

public List<Book> listBooks() {
    return bookRepoImpl.findAll();
}

public List<Book> searchBooks(String keyword) {
    return bookRepoImpl.search(keyword);
}

/** Reduce stock and record the issue as ONE atomic unit. */
public void issueBook(int bookId, int memberId) {
    try (Connection con = DBConnection.getConnection()) {
        try {
            con.setAutoCommit(false);

            if (!bookRepoImpl.decrementCopies(con, bookId)) {
                throw new IllegalStateException("Book is unavailable or does not exist.");
            }
            transactionRepo.insert(con, bookId, memberId);

            con.commit();
        } catch (RuntimeException e) {
            con.rollback();
            throw e;
        }
    } catch (SQLException e) {
        throw new DataAccessException("Issuing book failed", e);
    }
}

/** Mark the transaction returned and restore stock as ONE atomic unit. */
public void returnBook(int transactionId) {
    try (Connection con = DBConnection.getConnection()) {
        try {
            con.setAutoCommit(false);

            int bookId = transactionRepo.findOpenBookId(con, transactionId)
                    .orElseThrow(() -> new IllegalStateException(
                            "No open transaction with that ID."));
            transactionRepo.markReturned(con, transactionId);
            bookRepoImpl.incrementCopies(con, bookId);

            con.commit();
        } catch (RuntimeException e) {
            con.rollback();
            throw e;
        }
    } catch (SQLException e) {
        throw new DataAccessException("Returning book failed", e);
    }
}
}