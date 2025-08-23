import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

function BookDetail() {
    const { id } = useParams();
    const [book, setBook] = useState(null);

    useEffect(() => {
        fetch(`http://localhost:8080/api/books/${id}`)
            .then((res) => res.json())
            .then((data) => setBook(data))
            .catch(err => console.error(err));
    }, [id]);

    if (!book) return <p>Loading...</p>;

    return (
        <div>
            <h2>{book.title} by {book.author}</h2>
            <iframe
                src={`http://localhost:8080/api/files/download/${book.filePath}`}
                width="100%"
                height="600px"
                title={book.title}
            />
            <br/>
            <a href={`http://localhost:8080/api/files/download/${book.filePath}`} target="_blank" rel="noopener noreferrer">
                <button>Download</button>
            </a>
        </div>
    );
}

export default BookDetail;
