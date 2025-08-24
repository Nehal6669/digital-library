\# 📚 Digital Library



&nbsp;	A full-stack \*\*Digital Library\*\* application where users can upload, read, track progress, and download PDF books.  

&nbsp;	Built with \*\*Spring Boot (Backend)\*\* and \*\*React (Frontend)\*\*.





\## 🚀 Features

&nbsp;	- Upload books (PDF format)

&nbsp;	- View \& read books inside the browser (PDF Reader with progress tracking)

&nbsp;	- Save \& resume last read page

&nbsp;	- Download books

&nbsp;	- User-specific progress tracking





\## 🛠️ Tech Stack

&nbsp;	### Frontend

&nbsp;		- React + Vite / Create React App

&nbsp;		- `react-pdf` for PDF viewing



&nbsp;	### Backend

&nbsp;		- Spring Boot (Java)

&nbsp;		- REST APIs for book upload, download, reading \& progress tracking



&nbsp;	### Database

&nbsp;		- (e.g., MySQL / PostgreSQL / H2) → update depending on what you’re using





\## ⚙️ Setup Instructions

&nbsp;	### 1. Clone the Repository

&nbsp;		```bash

&nbsp;		git clone https://github.com/Nehal6669/digital-library.git

&nbsp;		cd digital-library



&nbsp;	### 2. Backend Setup (Spring Boot)

&nbsp;		-cd digital-library-backend

&nbsp;		-mvn spring-boot:run



&nbsp;		Backend will start at http://localhost:8080



&nbsp;	### 3. Frontend Setup (React)

&nbsp;		-cd digital-library-frontend

&nbsp;		-npm install

&nbsp;		-npm start



&nbsp;		Frontend will start at http://localhost:3000





\##📂 Project Structure



&nbsp;	digital-library/

&nbsp;	│

&nbsp;	├── digital-library-backend/     # Spring Boot backend

&nbsp;	│   ├── src/main/java/...        # Java source code

&nbsp;	│   └── src/main/resources/      # Config \& properties

&nbsp;	│

&nbsp;	├── digital-library-frontend/    # React frontend

&nbsp;	│   ├── public/                  # Static files (pdf.worker.min.js here)

&nbsp;	│   └── src/                     # React components

&nbsp;	│

&nbsp;	├── uploads/                     # Uploaded PDF books

&nbsp;	└── README.md                    # Project documentation







&nbsp; 

