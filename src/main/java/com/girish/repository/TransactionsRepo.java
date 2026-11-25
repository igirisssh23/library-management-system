package com.girish.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.girish.exception.DataAccessException;

public class TransactionsRepo {

	    public void insert(Connection con, int bookId, int memberId) {
	        String sql = "INSERT INTO transactions (book_id, member_id, issue_date) VALUES (?, ?, CURDATE())";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, bookId);
	            ps.setInt(2, memberId);
	            ps.executeUpdate();
	        } catch (SQLException e) {
	            throw new DataAccessException("Failed to record issue (check member ID)", e);
	        }
	    }

	    public Optional<Integer> findOpenBookId(Connection con, int transactionId) {
	        String sql = "SELECT book_id FROM transactions WHERE id = ? AND return_date IS NULL";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, transactionId);
	            try (ResultSet rs = ps.executeQuery()) {
	                return rs.next() ? Optional.of(rs.getInt("book_id")) : Optional.empty();
	            }
	        } catch (SQLException e) {
	            throw new DataAccessException("Failed to look up transaction", e);
	        }
	    }

	    public void markReturned(Connection con, int transactionId) {
	        String sql = "UPDATE transactions SET return_date = CURDATE() WHERE id = ?";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, transactionId);
	            ps.executeUpdate();
	        } catch (SQLException e) {
	            throw new DataAccessException("Failed to mark return", e);
	        }
	    }
	}


