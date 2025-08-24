import React, { useState, useEffect } from "react";
import { Document, Page, pdfjs } from "react-pdf";

// Use local worker from public folder (we copied it earlier)
pdfjs.GlobalWorkerOptions.workerSrc = `${process.env.PUBLIC_URL}/pdf.worker.min.js`;

function Reader({ book, userId, goBack }) {
    const [numPages, setNumPages] = useState(null);
    const [pageNumber, setPageNumber] = useState(1);
    const [error, setError] = useState(null);

    // Load last reading progress
    useEffect(() => {
        fetch(`http://localhost:8080/api/progress/${book.id}/${userId}`)
            .then(res => res.json())
            .then(data => { if (data.currentPage) setPageNumber(data.currentPage); })
            .catch(err => console.error("Progress fetch error:", err));
    }, [book.id, userId]);

    // Save progress whenever page changes
    useEffect(() => {
        if (!numPages) return;
        fetch(`http://localhost:8080/api/progress/`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                user: { id: userId },
                book: { id: book.id },
                currentPage: pageNumber,
                totalPages: numPages,
            }),
        }).catch(err => console.error("Progress save error:", err));
    }, [pageNumber, numPages, book.id, userId]);

    function onDocumentLoadSuccess({ numPages }) {
        setNumPages(numPages);
        setError(null); // Clear any previous error
    }

    function onDocumentLoadError(err) {
        console.error("PDF load error:", err);
        setError("Failed to load PDF file.");
    }

    // Encode filename to handle spaces and special characters
    const fileUrl = `http://localhost:8080/api/files/read/${encodeURIComponent(book.filePath)}`;

    return (
        <div>
            <h2>{book.title} by {book.author}</h2>

            {error && <p style={{ color: "red" }}>{error}</p>}

            <Document
                file={fileUrl}
                onLoadSuccess={onDocumentLoadSuccess}
                onLoadError={onDocumentLoadError}
            >
                <Page pageNumber={pageNumber} />
            </Document>

            <p>
                Page {pageNumber} of {numPages || 0} (
                {numPages ? ((pageNumber / numPages) * 100).toFixed(1) : 0}%)
            </p>

            <button onClick={() => setPageNumber(prev => Math.max(prev - 1, 1))} disabled={pageNumber <= 1}>
                Prev
            </button>
            <button onClick={() => setPageNumber(prev => Math.min(prev + 1, numPages))} disabled={pageNumber >= numPages}>
                Next
            </button>

            <a
                href={`http://localhost:8080/api/files/download/${encodeURIComponent(book.filePath)}`}
                target="_blank" rel="noopener noreferrer"
            >
                <button>Download</button>
            </a>

            <button onClick={goBack}>Back to List</button>
        </div>
    );
}

export default Reader;
