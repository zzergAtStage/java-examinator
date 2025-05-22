// quizManager.js

import { RendererFactory } from './renderers/rendererFactory.js';

/**
 * Manages quiz flow: loading data, navigation, rendering, and submission.
 */
class QuizManager {
  constructor(formId, dataScriptId, containerId) {
    this.form = document.getElementById(formId);
    this.dataScript = document.getElementById(dataScriptId);
    this.container = document.getElementById(containerId);
    this.currentSection = 0;
    this.currentQuestion = 0;
    this.totalQuestions = 0;

    this._loadData();
    this._wireEvents();
    this._showQuestion();
  }

  /**
   * Parses the embedded JSON into examData structure.
   */
  _loadData() {
    this.examData = JSON.parse(this.dataScript.textContent);
    this.totalQuestions = this.examData.sections.reduce((sum, section) => sum + section.questions.length, 0);
    document.getElementById('totalQuestions').innerText = this.totalQuestions;
  }

  /**
   * Attaches navigation and submission event listeners.
   */
  _wireEvents() {
    document.getElementById('prevButton')
      .addEventListener('click', () => this._prev());
    document.getElementById('nextButton')
      .addEventListener('click', () => this._next());
    this.form.addEventListener('submit', e => this._submit(e));
  }

  /**
   * Renders the current question via the appropriate renderer.
   */
  _showQuestion() {
    const section = this.examData.sections[this.currentSection];
    let questionData = section.questions[this.currentQuestion];
    questionData.sectionName = section.sectionName; // Pass section name to renderer
    const questionWrapper = questionData;

    // Delegate rendering
    const renderer = RendererFactory.create(questionWrapper);
    this.container.innerHTML = renderer.render();

    // Bind answer‐saving logic
    renderer.bindEvents(answer => {
      questionWrapper.userAnswers = renderer.getUserAnswers();
      this._updateNav();
    });

    Prism.highlightAll();
    this._updateNav();
    this._updateProgress();
  }

  /**
   * Moves to the next question or section.
   */
  _next() {
    const sect = this.examData.sections[this.currentSection];
    if (this.currentQuestion < sect.questions.length - 1) {
      this.currentQuestion++;
    } else if (this.currentSection < this.examData.sections.length - 1) {
      this.currentSection++;
      this.currentQuestion = 0;
    }
    this._showQuestion();
  }

  /**
   * Moves to the previous question or section.
   */
  _prev() {
    if (this.currentQuestion > 0) {
      this.currentQuestion--;
    } else if (this.currentSection > 0) {
      this.currentSection--;
      this.currentQuestion = this.examData.sections[this.currentSection].questions.length - 1;
    }
    this._showQuestion();
  }

  /**
   * Toggles nav buttons and submit visibility.
   */
  _updateNav() {
    const atFirst = this.currentSection === 0 && this.currentQuestion === 0;
    const lastSection = this.currentSection === this.examData.sections.length - 1;
    const lastQuestion = this.currentQuestion === this.examData.sections[this.currentSection].questions.length - 1;

    document.getElementById('prevButton').disabled = atFirst;
    document.getElementById('nextButton').disabled = lastSection && lastQuestion;
    document.getElementById('submitButton').classList.toggle('d-none', !(lastSection && lastQuestion));
  }

  /**
   * Updates progress bar text and percentage.
   */
  _updateProgress() {
    const questionIndex = this.examData.sections
      .slice(0, this.currentSection)
      .reduce((sum, section) => sum + section.questions.length, 0) + this.currentQuestion + 1;

    document.getElementById('currentQuestionNum').innerText = questionIndex;
    const progressPercent = (questionIndex / this.totalQuestions) * 100;
    const progressBar = document.getElementById('quizProgressBar');
    progressBar.style.width = `${progressPercent}%`;
    progressBar.setAttribute('aria-valuenow', progressPercent);
  }

  /**
  /**
   * Serializes answers and POSTs the full JSON to the server.
   */
  _submit(event) {
    event.preventDefault();
    fetch('/submitQuiz', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(this.examData)
    })
    .then(res => res.ok ? window.location.href = '/result'
                        : Promise.reject('Submission failed'));
  }
}

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', () => {
  new QuizManager('quizForm', 'questionData', 'question-container');
});
