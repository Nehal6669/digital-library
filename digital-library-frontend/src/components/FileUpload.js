import React, { useState } from "react";

function FileUpload({ onUpload }) {
    const [file, setFile] = useState(null);
    const [title, setTitle] = useState("");
    const [author, setAuthor] = useState("");
    const [category, setCategory] = useState("");
    const [totalPages, setTotalPages] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!file) { alert("Select a file"); return; }

        const formData = new FormData();
        formData.append("file", file);
        formData.append("title", title);
        formData.append("author", author);
        if (category) formData.append("category", category);
        if (totalPages) formData.append("totalPages", totalPages);

        try {
            const res = await fetch("http://localhost:8080/api/files/upload", {
                method: "POST",
                body: formData,
            });
            if (res.ok) {
                const data = await res.json();
                alert("Uploaded!");
                onUpload(data);
                setFile(null); setTitle(""); setAuthor(""); setCategory(""); setTotalPages("");
            } else {
                const errorText = await res.text();
                alert("Upload failed: " + errorText);
            }
        } catch (err) { alert("Upload error: " + err.message); }
    };

    return (
        <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "8px", maxWidth: "400px" }}>
            <input type="text" placeholder="Title" value={title} onChange={e => setTitle(e.target.value)} required />
            <input type="text" placeholder="Author" value={author} onChange={e => setAuthor(e.target.value)} required />
            <input type="text" placeholder="Category (optional)" value={category} onChange={e => setCategory(e.target.value)} />
            <input type="number" placeholder="Total Pages (optional)" value={totalPages} onChange={e => setTotalPages(e.target.value)} />
            <input type="file" accept="application/pdf" onChange={e => setFile(e.target.files[0])} required />
            <button type="submit">Upload</button>
        </form>
    );
}

export default FileUpload;
