// renderers/fillGapRenderer.js

/**
 * Renders a “fill the gap” question by replacing placeholders like {{1}} with input fields,
 * pre-filling them if the user has already answered, and collecting their values.
 */
export class FillGapRenderer {
  /**
   * @param {Object} questionData - The full question wrapper object,
   *   including question.questionText and question.userAnswers.
   */
  constructor(questionData) {
    this.data = questionData;
  }

  /**
   * Replaces each {{n}} placeholder with a text input,
   * injecting any existing answer as the input’s value.
   *
   * @returns {string} HTML string for this question’s UI.
   */
  render() {
    const rawText    = this.data.question.questionText;
    const prevAnswers= Array.isArray(this.data.userAnswers)
                       ? this.data.userAnswers
                       : [];

    // Replace each {{n}} with an <input> having data-idx and value
    const htmlWithInputs = rawText.replace(/{{(\d+)}}/g, (_, idx) => {
      const val = prevAnswers[idx] || '';
      return `<input type="text"
                     class="gap-input"
                     data-idx="${idx}"
                     value="${val}"
                     placeholder="…">`;
    });

    return `
      <h3>${this.data.sectionName}</h3>
      <p>${this.data.question.questionHeader}</p>
      <div class="ques-box">
        <p>${htmlWithInputs}</p>
      </div>
    `;
  }

  /**
   * Finds all gap inputs and wires their `input` events
   * so that onChange receives the latest answers array.
   *
   * @param {Function} onChange - Called with an array of current gap answers.
   */
  bindEvents(onChange) {
    // Must select *after* render() has injected the inputs
    this.inputs = Array.from(document.querySelectorAll('.gap-input'));

    this.inputs.forEach(input =>
      input.addEventListener('input', () => {
        onChange(this.getUserAnswers());
      })
    );
  }

  /**
   * Extracts each input’s value in numeric order into an array.
   *
   * @returns {string[]} e.g. ['first gap', 'second gap', …]
   */
  getUserAnswers() {
    // Initialize an array as big as the highest index + 1
    const answers = [];

    this.inputs.forEach(inp => {
      const idx = parseInt(inp.dataset.idx, 10);
      answers[idx] = inp.value;
    });

    return answers;
  }
}
