package org.zergatstage.model.samplefactory;

import org.zergatstage.model.Quiz;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuizTestSamples {
    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Quiz getQuizSample1() {
        return Quiz.builder().id(1L).title("title1").description("description1").version(1).totalPoints(1).build();
    }
    public static Quiz getQuizSample2() {
        return Quiz.builder().id(2L).title("title2").description("description2").version(2).totalPoints(2).build();
    }

    public static Quiz getQuizRandomSampleGenerator(){
        return Quiz.builder()
                .id(longCount.incrementAndGet())
                .title(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .version(intCount.incrementAndGet())
                .totalPoints(intCount.incrementAndGet())
                .build();
    }
}
