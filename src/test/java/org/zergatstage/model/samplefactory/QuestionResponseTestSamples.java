package org.zergatstage.model.samplefactory;

import org.zergatstage.model.QuestionResponse;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionResponseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QuestionResponse getQuestionResponseSample1() {
        return new QuestionResponse().id(1L).freeTextAnswer("freeTextAnswer1").awardedPoints(1);
    }

    public static QuestionResponse getQuestionResponseSample2() {
        return new QuestionResponse().id(2L).freeTextAnswer("freeTextAnswer2").awardedPoints(2);
    }

    public static QuestionResponse getQuestionResponseRandomSampleGenerator() {
        return new QuestionResponse()
                .id(longCount.incrementAndGet())
                .freeTextAnswer(UUID.randomUUID().toString())
                .awardedPoints(intCount.incrementAndGet());
    }
}