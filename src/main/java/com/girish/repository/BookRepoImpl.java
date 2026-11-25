package com.girish.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.girish.exception.DataAccessException;
import com.girish.model.Book;
import com.girish.util.DBConnection;

public class BookRepoImpl implements BookRepo {

	    @Override
	    public void add(Book book) {
	        String sql = "INSERT INTO books (title, author, isbn, available_copies) VALUES (?, ?, ?, ?)";
	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, book.getTitle());
	            ps.setString(2, book.getAuthor());
	            ps.setString(3, book.getIsbn());
	            ps.setInt(4, book.getAvailableCopies());
	            ps.executeUpdate();
	        } catch (SQLException e) {
	            throw new DataAccessException("Failed to add book", e);
	        }
	    }

	    @Override
	    public List<Book> findAll() {
	        String sql = "SELECT * FROM books ORDER BY title";
	        List<Book> books = new ArrayList<>();
	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                books.add(mapRow(rs));
	            }
	            return books;
	        } catch (SQLException e) {
	            throw new DataAccessException("Failed to list books", e);
	        }
	    }

	    @Override
	    public List<Book> search(String keyword) {
	        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";
	        List<Book> books = new ArrayList<>();
	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            String pattern = "%" + keyword + "%";
	            ps.setString(1, pattern);
	            ps.setString(2, pattern);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    books.add(mapRow(rs));
	                }
	            }
	            return books;
	        } catch (SQLException e) {
	            throw new DataAccessException("Search failed", e);
	        }
	    }

	    @Override
	    public boolean decrementCopies(Connection con, int bookId) {
	        String sql = "UPDATE books SET available_copies = available_copies - 1 "
	                   + "WHERE id = ? AND available_copies > 0";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, bookId);
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            throw new DataAccessException("Failed to update stock", e);
	        }
	    }

	    @Override
	    public boolean incrementCopies(Connection con, int bookId) {
	        String sql = "UPDATE books SET available_copies = available_copies + 1 WHERE id = ?";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, bookId);
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            throw new DataAccessException("Failed to update stock", e);
	        }
	    }

	    private Book mapRow(ResultSet rs) throws SQLException {
	        return new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"),
	                rs.getString("isbn"), rs.getInt("available_copies"));
	    }
	}


