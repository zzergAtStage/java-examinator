// renderers/rendererFactory.js

import { MultipleChoiceRenderer } from './multipleChoiceRenderer.js';
import { FillGapRenderer }       from './fillGapRenderer.js';
import { CodeRenderer }          from './CodeRenderer.js';

export class RendererFactory {
  static create(questionWrapper) {
    switch (questionWrapper.question.questionType) {
      case 'SIMPLE':
        return new MultipleChoiceRenderer(questionWrapper);
      case 'FILL_THE_GAP':
        return new FillGapRenderer(questionWrapper);
      case 'CODE':
        return new CodeRenderer(questionWrapper);
      // add new types here...
      default:
        throw new Error(`Unknown question type: ${questionWrapper.question.questionType}`);
    }
  }
}
