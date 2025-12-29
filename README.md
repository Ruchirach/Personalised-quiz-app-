# Personalized Quiz Application 🎯
_A JavaFX-based role-specific quiz platform with secure access codes and SQLite database support_

## 📌 Overview
The **Personalized Quiz Application** is an interactive quiz platform designed for **teachers and students** with role-specific features. Teachers can create quizzes, add questions, generate secure access codes, and track student performance. Students can register, log in, join quizzes using access codes, attempt questions, and view their results.

The application is built using **JavaFX (GUI)** and **SQLite (database)** — making it lightweight, scalable, and easy to run across systems.

---

## 👥 User Roles & Features

### 🧑‍🏫 Teacher Features
- Create quizzes with titles & descriptions  
- Add multiple-choice questions  
- Auto-generate **secure access codes**  
- View all quizzes created  
- Monitor student performance & results  

### 👨‍🎓 Student Features
- Register & log in securely  
- Join quizzes using access codes  
- Attempt MCQ quizzes  
- View score instantly  
- Track previous quiz attempts  

---

## 🛠️ Tech Stack

| Component | Technology |
|----------|------------|
| Language | Java |
| GUI Framework | JavaFX |
| Database | SQLite (JDBC driver) |
| IDE (Recommended) | IntelliJ IDEA / VS Code |
| Java Version | JDK 21+ |

---

## 📂 Database Schema

The application uses **SQLite** with the following tables:

- **Users**
  - user_id, username, password, role  
- **Quiz**
  - quiz_id, title, access_code, creator_id, created_on  
- **Questions**
  - question_id, quiz_id, question_text, option1-4, correct_option  
- **Results**
  - result_id, student_id, quiz_id, score, attempt_date  

---

## 🚀 Running the Project (Locally)

### ✅ Prerequisites
- Install **JDK 21+**
- Install **JavaFX SDK**
- Ensure SQLite JDBC library is available in project

### ▶️ Steps
1. Clone the repository  
   ```bash
   git clone https://github.com/Ruchirach/Personalised-quiz-app-.git
   cd Personalised-quiz-app-
