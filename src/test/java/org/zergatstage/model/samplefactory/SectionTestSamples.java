package org.zergatstage.model.samplefactory;

import org.zergatstage.model.Section;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Section getSectionSample1() {
        return new Section().id(1L).name("name1").displayOrder(1);
    }

    public static Section getSectionSample2() {
        return new Section().id(2L).name("name2").displayOrder(2);
    }

    public static Section getSectionRandomSampleGenerator() {
        return new Section().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).displayOrder(intCount.incrementAndGet());
    }
}
