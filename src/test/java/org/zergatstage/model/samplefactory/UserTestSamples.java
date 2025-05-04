package org.zergatstage.model.samplefactory;

import org.zergatstage.model.User;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static User getUserSample1() {
        return User.builder().id(1L).username("username1").email("email1").build();
    }
    public static User getUserSample2() {
        return User.builder().id(2L).username("username2").email("email2").build();
    }

    public static User getUserRandomSampleGenerator() {
        return User.builder().id(longCount.incrementAndGet()).username(UUID.randomUUID().toString()).email(UUID.randomUUID().toString()).build();
    }
}
