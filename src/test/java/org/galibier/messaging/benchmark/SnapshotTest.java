package org.galibier.messaging.benchmark;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class SnapshotTest {
    private Snapshot snapshot;

    @Before
    public void setUp() throws Exception {
        snapshot = new Snapshot();
    }

    @Test
    public void countUpを1回呼ぶとtotalが1を返す() {
        snapshot.countUp();

        assertThat(snapshot.getTotal(), is(1L));
    }

    @Test
    public void countUpを2回呼ぶとtotalが2を返す() {
        snapshot.countUp();
        snapshot.countUp();

        assertThat(snapshot.getTotal(), is(2L));
    }

    @Test
    public void takeを呼んでからcountUpを1回呼ぶと1を返す() {
        snapshot.countUp();

        assertThat(snapshot.take(), is(1L));
    }

    @Test
    public void takeを呼んでからcountUpを2回呼ぶと2を返す() {
        snapshot.countUp();
        snapshot.take();
        snapshot.countUp();
        snapshot.countUp();

        assertThat(snapshot.take(), is(2L));
    }
}
