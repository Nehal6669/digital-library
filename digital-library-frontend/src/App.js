import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import FileUpload from "./components/FileUpload";
import FileList from "./components/FileList";
import BookList from "./components/BookList";

function App() {
    const [refreshKey, setRefreshKey] = useState(0);

    // Called after upload or any update to refresh file list
    const handleRefresh = () => setRefreshKey(oldKey => oldKey + 1);

    return (
        <Router>
            <div style={{ padding: "16px" }}>
                <h1>Digital Library</h1>
                <nav style={{ marginBottom: "16px" }}>
                    <Link to="/">Files</Link> |{" "}
                    <Link to="/books">Books</Link>
                </nav>

                <Routes>
                    {/* File upload & list */}
                    <Route
                        path="/"
                        element={
                            <div>
                                <FileUpload onUpload={handleRefresh} />
                                <FileList refreshKey={refreshKey} />
                            </div>
                        }
                    />

                    {/* Books page */}
                    <Route
                        path="/books"
                        element={<BookList />}
                    />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
