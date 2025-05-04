package org.zergatstage.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.zergatstage.services.ExamService;

/** Writing the test to check model and its service layer
 * @author father
 */
@SpringBootTest
@Disabled
public class QuestionsTest {
  private Question question;

  @MockBean
  private final ExamService examService;

  public QuestionsTest(ExamService examService) {
    this.examService = examService;
  }

  @BeforeEach
  void init(){
    Question question = Question.builder()
            .questionType(QuestionType.SIMPLE)
            .questionHeader("Can you inherit the java.lang.String class?")
            .typeOfAnswer(AnswerFormat.SINGLE)
            .correctAnswer("No")
            .build();

  }
  @Test
  void whenSingleCorrectAnswerTest(){

  }

}
