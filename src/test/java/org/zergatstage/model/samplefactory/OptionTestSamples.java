package org.zergatstage.model.samplefactory;

import org.zergatstage.model.Option;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OptionTestSamples {
    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Option getOptionSample1() {
        return new Option().id(1L).displayOrder(1).text("text1");
    }

    public static Option getOptionSample2() {
        return new Option().id(2L).displayOrder(2).text("text2");
    }

    public static Option getOptionRandomSampleGenerator() {
        return new Option().id(longCount.incrementAndGet()).displayOrder(intCount.incrementAndGet()).text(UUID.randomUUID().toString());
    }
}
