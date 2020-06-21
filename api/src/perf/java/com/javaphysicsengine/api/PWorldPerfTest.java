package com.javaphysicsengine.api;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

public class PWorldPerfTest {

    @State(Scope.Benchmark)
    public class PWorldState {

        public PWorld pWorld;

        @Setup(Level.Invocation)
        public void setup() {
            pWorld = new PWorld();

        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void measureSimulate_onBoxes(Blackhole blackhole) {

    }
}
