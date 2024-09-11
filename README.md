# Quiz App (s09-javaexaminator)
 ## Summary
The application is designed for use both as a standalone Spring Boot MVC application and as part of a microservice module (see the relevant section).
## Features
- **Quiz Taking:** Users can participate in quizzes featuring multiple-choice questions.
- **Answer Submission:** Users can submit their answers for each question in the quiz.
- **Submission Management:** User can view and delete submissions.
- **Answer Highlighting:** Incorrect answers are highlighted, and correct answers along with explanations are displayed.

## Data formats:
The main source of questions is questions.json:
[exam.json](./data/exam.json)
```plantuml
@startuml
title Exam JSON Structure

class Exam {
    exam_id
    sessionId
    examDate
    user
    sections
}

class User {
    id
    username
}

class Section {
    section_id
    sectionName
    userAnswers
}

class JavaQuizQuestion {
    question_id
    questionType
    questionHeader
    questionText
    difficultyLevel
    correctAnswer
    correctAnswerExplanation
    points
    choices
}

class UserAnswer {
    id: Long
    question: JavaQuizQuestion
    userAnswer: String
    correct
    pointsAwarded
}

Exam "1" -- "*" User : has
Exam "1" -- "*" Section : contains
Section "1" -- "*" UserAnswer : has
JavaQuizQuestion::question_id "1" -- "*" UserAnswer::question : has

@enduml
```

## Getting Started

To get started with the `s09-javaexaminator` module, follow these steps:

### Prerequisites
- Java 11 or higher
- Maven
- Spring Boot

### Installation

1. **Clone the Repository:**
```bash
   git clone https://github.com/zzergAtStage/s09-microservices.git
```

2. **Navigate to the Project Directory:**  
```bash
cd <project-directory>
```
3. ... do something with test errors
4. Run the application
```bash
mvn -f s09-javaexaminator spring-boot:run
```

## Screenshoots
1. Logon screen:
![Login page](./docs/images/logon.png)
2. Submissions page:
![Submissions](./docs/images/submissions.png)
3. Quiz page:
![Quiz page](./docs/images/quiz-question.png)
4. Results page:
![Resul](./docs/images/result.png)
6. Review page:
![Review](./docs/images/review-page.png)
6. Review with wrong answer:
![Review code](./docs/images/review-wrong-code.png)
7. Error page:
![Error page](./docs/images/error-page.png)
5. 