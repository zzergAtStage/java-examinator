let currentQuestionIndex = 0;
let currentSectionIndex = 0; // Add a current section index
let examData; // Will store the parsed exam data

// Load the questions from the JSON document when the page is ready
document.addEventListener("DOMContentLoaded", function () {
  examData = JSON.parse(document.getElementById("questionData").textContent);

  // Display the first question if there are any sections and questions
  if (examData.sections.length > 0 && examData.sections[0].questions.length > 0) {
    displayQuestion(currentSectionIndex, currentQuestionIndex);
  }
  // Attach the form submission handler
  const quizForm = document.getElementById('quizForm');
  quizForm.addEventListener('submit', function (event) {
    event.preventDefault();  // Prevent the default form submission

    submitExamData();  // Call the function to submit the JSON data
  });
});

// Function to display the current question
function displayQuestion(sectionIndex, questionIndex) {
  const section = examData.sections[sectionIndex];
  const question = section.questions[questionIndex].question;
  const userAnswers = section.questions[questionIndex].userAnswers && section.questions[questionIndex].userAnswers.length != 0 ? section.questions[questionIndex].userAnswers: [];
  const questionContainer = document.getElementById("question-container");

  // Determine the input type based on AnswerType
  console.log('question.answerType: ' + question.typeOfAnswer);

  const inputType = question.typeOfAnswer != 'MULTIPLE' ? 'radio' : 'checkbox';
  const nameAttribute = question.typeOfAnswer != 'MULTIPLE' ? `question_${question.id}` : `question_${question.id}[]`;


  // Display the section title and the question
  questionContainer.innerHTML = `
    <h3>${section.sectionName}</h3> <!-- Display section name -->
    <p>Question id:${question.id}</p>
    <div class="ques-box bg-dark text-light">
      <p class="title">${question.questionHeader}</p>

      <!-- If the question type is CODE, render it with Prism.js for code highlighting -->
      ${question.questionType === 'CODE' ? `
        <pre><code class="language-java">${question.questionText}</code></pre>
      ` : ''}
      <hr>
      <div class="option">
          ${question.choices.map((choice, i) => `
            <div class="option-item">
              <input type="${inputType}" name="${nameAttribute}"
                     value="${choice}"
                     id="choice_${sectionIndex}_${questionIndex}_${i}"
                     ${userAnswers.includes(choice) ? 'checked' : ''}
                     onchange="saveAnswer(${sectionIndex}, ${questionIndex}, '${choice}')">
              <label for="choice_${sectionIndex}_${questionIndex}_${i}">${choice}</label>
            </div>
          `).join('')}
        </div>
    </div>
  `;

  Prism.highlightAll(); // Re-highlight code if necessary
  updateNavigationButtons();
}


// Save the selected answer to the exam data
function saveAnswer(sectionIndex, questionIndex, selectedAnswer) {
  const section = examData.sections[sectionIndex];
  console.log("selectedAnswers: " + selectedAnswer + "\n" +
    "userAnswers:[" + questionIndex + "] " + section.questions[questionIndex].userAnswers);

  section.questions[questionIndex].userAnswers.push(selectedAnswer);
}

// Navigate to the next question
function nextQuestion() {
  const currentSection = examData.sections[currentSectionIndex];

  // Move to the next question in the current section, or move to the next section if needed
  if (currentQuestionIndex < currentSection.questions.length - 1) {
    currentQuestionIndex++;
  } else if (currentSectionIndex < examData.sections.length - 1) {
    currentSectionIndex++;
    currentQuestionIndex = 0; // Reset to the first question of the next section
  }

  displayQuestion(currentSectionIndex, currentQuestionIndex);
}

// Navigate to the previous question
function prevQuestion() {
  if (currentQuestionIndex > 0) {
    currentQuestionIndex--;
  } else if (currentSectionIndex > 0) {
    currentSectionIndex--;
    currentQuestionIndex = examData.sections[currentSectionIndex].questions.length - 1;
  }

  displayQuestion(currentSectionIndex, currentQuestionIndex);
}

// Update navigation buttons based on the current question index and section
function updateNavigationButtons() {
  document.getElementById("prevButton").disabled = currentSectionIndex === 0 && currentQuestionIndex === 0;
  document.getElementById("nextButton").disabled = currentSectionIndex === examData.sections.length - 1 &&
    currentQuestionIndex === examData.sections[examData.sections.length - 1].questions.length - 1;
  document.getElementById("submitButton").classList.toggle('d-none', !(currentSectionIndex === examData.sections.length - 1 && currentQuestionIndex === examData.sections[currentSectionIndex].questions.length - 1));
}

// Prepare the form for submission by adding user answers from all sections
function prepareFormForSubmission() {
  const form = document.getElementById('quizForm');

  examData.sections.forEach((section, sectionIndex) => {
    section.questions.forEach((question, questionIndex) => {
      const input = document.createElement('input');
      input.type = 'hidden';
      input.name = `sections[${sectionIndex}].questions[${questionIndex}].userAnswer`;
      input.value = question.userAnswer || ''; // Default to an empty string if no answer
      form.appendChild(input);
    });
  });
}


// Function to send the entire examData JSON to the server
function submitExamData() {
  fetch('/submitQuiz', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(examData)  // Send the JSON object
  })
    .then(response => {
      if (response.ok) {
        window.location.href = '/result';
      } else {
        throw new Error('Something went wrong during submission');
      }
    })
    .then(data => {
      console.log('Success:', data);
      window.location.href = '/result';  // Redirect to result page after successful submission
    })
    .catch(error => {
      console.error('Error:', error);
    });
}