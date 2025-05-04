package org.zergatstage.model.samplefactory;

import org.zergatstage.model.Question;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionTestSamples {
    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Question getQuestionSample1() {
        return new Question().id(1L).difficulty(1).points(1);
    }

    public static Question getQuestionSample2() {
        return new Question().id(2L).difficulty(2).points(2);
    }

    public static Question getQuestionRandomSampleGenerator() {
        return new Question().id(longCount.incrementAndGet()).difficulty(intCount.incrementAndGet()).points(intCount.incrementAndGet());
    }
}
