USE library_db;

INSERT INTO books (title, author, isbn, available_copies) VALUES
 ('Clean Code', 'Robert C. Martin', '9780132350884', 3),
 ('Effective Java', 'Joshua Bloch', '9780134685991', 2),
 ('Head First Java', 'Kathy Sierra', '9781491910771', 4);

INSERT INTO members (name, email, phone) VALUES
 ('Asha Patil', 'asha@example.com', '9876543210'),
 ('Rohan Deshmukh', 'rohan@example.com', '9123456780');