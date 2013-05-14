package org.galibier.messaging.benchmark;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TargetGeneratorTest {
    private TargetGenerator generator;

    @Test
    public void testMultipleItems() {
        List<String> targets = Arrays.asList("1", "2", "3", "4");

        generator = new TargetGenerator(targets);

        assertThat(generator.getTargets(), is(targets));
        assertThat(generator.next(), is("1"));
        assertThat(generator.next(), is("2"));
        assertThat(generator.next(), is("3"));
        assertThat(generator.next(), is("4"));
        assertThat(generator.next(), is("1"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testVacancy() {
        List<String> targets = Arrays.asList();

        generator = new TargetGenerator(targets);
    }
}
