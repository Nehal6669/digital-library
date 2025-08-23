import React, { useEffect, useState } from "react";
import { api } from "../api";

function FileList({ refreshKey }) {
    const [files, setFiles] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchFiles = () => {
        setLoading(true);
        api.get("/files")
            .then(res => setFiles(res.data))
            .finally(() => setLoading(false));
    };

    useEffect(() => { fetchFiles(); }, [refreshKey]);

    if (loading) return <p>Loading files...</p>;
    if (files.length === 0) return <p>No files uploaded yet.</p>;

    return (
        <div>
            <h2>Uploaded Files</h2>
            <ul>
                {files.map(f => (
                    <li key={f.id}>
                        {f.fileName || f.title} -
                        <a href={`http://localhost:8080/api/files/download/${f.filePath}`} target="_blank" rel="noopener noreferrer">Download</a>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default FileList;
