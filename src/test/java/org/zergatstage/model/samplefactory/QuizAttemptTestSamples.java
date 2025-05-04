package org.zergatstage.model.samplefactory;

import org.zergatstage.model.QuizAttempt;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuizAttemptTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QuizAttempt getQuizAttemptSample1() {
        return new QuizAttempt().id(1L).sessionId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).score(1).maxScore(1);
    }

    public static QuizAttempt getQuizAttemptSample2() {
        return new QuizAttempt().id(2L).sessionId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).score(2).maxScore(2);
    }

    public static QuizAttempt getQuizAttemptRandomSampleGenerator() {
        return new QuizAttempt()
            .id(longCount.incrementAndGet())
            .sessionId(UUID.randomUUID())
            .score(intCount.incrementAndGet())
            .maxScore(intCount.incrementAndGet());
    }
}
