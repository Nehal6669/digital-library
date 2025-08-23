import React, { useEffect, useState } from "react";
import Reader from "./Reader";

function BookList() {
    const [books, setBooks] = useState([]);
    const [currentBook, setCurrentBook] = useState(null);
    const userId = 1; // static user for demo

    // Fetch all books from backend
    useEffect(() => {
        fetch("http://localhost:8080/api/books")
            .then(res => res.json())
            .then(data => setBooks(data))
            .catch(err => console.error("Failed to fetch books:", err));
    }, []);

    // If a book is selected, show Reader
    if (currentBook) {
        return (
            <Reader
                book={currentBook}
                userId={userId}
                goBack={() => setCurrentBook(null)}
            />
        );
    }

    return (
        <div>
            <h2>Books</h2>
            {books.length === 0 ? (
                <p>No books available.</p>
            ) : (
                <ul>
                    {books.map(book => (
                        <li key={book.id} style={{ marginBottom: "12px" }}>
                            <strong>{book.title}</strong> by {book.author} ({book.totalPages || 0} pages)
                            <div style={{ marginTop: "4px" }}>
                                <button onClick={() => setCurrentBook(book)}>
                                    Read
                                </button>{" "}
                                <a
                                    href={`http://localhost:8080/api/files/download/${book.filePath}`}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                >
                                    <button>Download</button>
                                </a>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default BookList;
