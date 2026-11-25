package com.girish.controller;

import java.util.List;
import java.util.Scanner;

import com.girish.exception.DataAccessException;
import com.girish.model.Book;
import com.girish.service.LibraryService;

public class LibraryController {
	 private static final LibraryService service = new LibraryService();
	    private static final Scanner sc = new Scanner(System.in);

	    public static void main(String[] args) {
	        boolean running = true;

	        while (running) {
	            printMenu();
	            String choice = sc.nextLine().trim();

	            try {
	            	switch (choice) {
	                case "1":
	                    addBook();
	                    break;
	                case "2":
	                    printBooks(service.listBooks());
	                    break;
	                case "3":
	                    searchBooks();
	                    break;
	                case "4":
	                    issueBook();
	                    break;
	                case "5":
	                    returnBook();
	                    break;
	                case "0":
	                    running = false;
	                    break;
	                default:
	                    System.out.println("Invalid option. Try again.");
	            }
	            } catch (IllegalStateException e) {
	                System.out.println("Warning: " + e.getMessage());
	            } catch (DataAccessException e) {
	                System.out.println("Database error: " + e.getMessage());
	            } catch (NumberFormatException e) {
	                System.out.println("Warning: please enter a valid number.");
	            }
	        }

	        System.out.println("Goodbye!");
	    }

	    private static void printMenu() {
	        System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
	        System.out.println("1. Add book");
	        System.out.println("2. View all books");
	        System.out.println("3. Search books");
	        System.out.println("4. Issue book");
	        System.out.println("5. Return book");
	        System.out.println("0. Exit");
	        System.out.print("Choose: ");
	    }

	    private static void addBook() {
	        System.out.print("Title: ");
	        String title = sc.nextLine().trim();
	        System.out.print("Author: ");
	        String author = sc.nextLine().trim();
	        System.out.print("ISBN: ");
	        String isbn = sc.nextLine().trim();
	        System.out.print("Copies: ");
	        int copies = Integer.parseInt(sc.nextLine().trim());

	        service.addBook(new Book(0, title, author, isbn, copies));
	        System.out.println("Book added.");
	    }

	    private static void searchBooks() {
	        System.out.print("Keyword: ");
	        printBooks(service.searchBooks(sc.nextLine().trim()));
	    }

	    private static void issueBook() {
	        System.out.print("Book ID: ");
	        int bookId = Integer.parseInt(sc.nextLine().trim());
	        System.out.print("Member ID: ");
	        int memberId = Integer.parseInt(sc.nextLine().trim());

	        service.issueBook(bookId, memberId);
	        System.out.println("Book issued.");
	    }

	    private static void returnBook() {
	        System.out.print("Transaction ID: ");
	        int transactionId = Integer.parseInt(sc.nextLine().trim());

	        service.returnBook(transactionId);
	        System.out.println("Book returned.");
	    }

	    private static void printBooks(List<Book> books) {
	        if (books.isEmpty()) {
	            System.out.println("No books found.");
	        } else {
	            books.forEach(System.out::println);
	        }
	    }
	}

