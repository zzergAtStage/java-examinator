// renderers/multipleChoiceRenderer.js

/**
 * Renders and manages a single‐choice or multiple‐choice question.
 */
export class MultipleChoiceRenderer {
    /**
     * @param {Object} questionData Full question object
     */
    constructor(questionData) {
      this.data = questionData;
    }
  
    /**
     * Returns HTML for the question prompt and choices.
     */
    render() {
      const {question, userAnswers=[]} = this.data;
      const inputType = question.typeOfAnswer === 'MULTIPLE' ? 'checkbox' : 'radio';
      const nameAttr  = question.typeOfAnswer === 'MULTIPLE'
        ? `q_${question.id}[]` : `q_${question.id}`;
  
      return `
        <h3>${this.data.sectionName}</h3>
        <p>${question.questionHeader}</p>
        <div class="ques-box">
          ${question.questionType === 'CODE'
            ? `<pre><code class="language-java">${question.questionText}</code></pre>`
            : `<p>${question.questionText}</p>`}
          <hr>
          <div>
            ${question.choices.map((opt, i) => `
              <div class="form-check">
                <input class="form-check-input"
                       type="${inputType}"
                       name="${nameAttr}"
                       id="opt_${i}"
                       value="${opt}"
                       ${userAnswers.includes(opt)?'checked':''}>
                <label class="form-check-label" for="opt_${i}">${opt}</label>
              </div>
            `).join('')}
          </div>
        </div>
      `;
    }
  
    /**
     * Binds input change events to notify QuizManager of new answers.
     * @param {Function} onChange Callback with the updated answer list.
     */
    bindEvents(onChange) {
      this.inputs = Array.from(document.querySelectorAll('input[name^="q_"]'));
      this.inputs.forEach(input =>
        input.addEventListener('change', () => onChange(this.getUserAnswers()))
      );
    }
  
    /**
     * Gathers all checked values as the current answers.
     * @returns {string[]}
     */
    getUserAnswers() {
      return this.inputs
        .filter(i => i.checked)
        .map(i => i.value);
    }
  }
  