import React, { useEffect, useState } from "react";
import { api } from "../api";

function ReadingProgress({ refreshKey }) {
    const [progress, setProgress] = useState([]);

    const fetchProgress = () => {
        api.get("/progress")
            .then(res => setProgress(res.data))
            .catch(err => console.error("Failed to fetch progress", err));
    };

    useEffect(() => {
        fetchProgress();
    }, [refreshKey]);

    return (
        <div>
            <h2>Reading Progress</h2>
            <ul>
                {progress.map(p => (
                    <li key={p.id}>
                        User: {p.user?.name}, Book: {p.book?.title}, {p.currentPage}/{p.totalPages} pages
                        ({p.percentageCompleted.toFixed(2)}%)
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ReadingProgress;
