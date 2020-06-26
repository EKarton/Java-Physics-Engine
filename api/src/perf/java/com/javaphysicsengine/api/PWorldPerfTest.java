package com.javaphysicsengine.api;

import com.javaphysicsengine.api.body.PPolygon;
import com.javaphysicsengine.utils.Vector;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class PWorldPerfTest {

    @State(Scope.Benchmark)
    public static class PWorldCollapsingBoxesState {

        public final static double FPS = 64;

        public PWorld pWorld;
        public double timeEllapsed;

        @Setup(Level.Trial)
        public void setup() {
            timeEllapsed = (1000.0 / FPS) / 1000.0;
            pWorld = new PWorld();

            // The walls
            PPolygon ground = new PPolygon("Ground");
            ground.setMoveable(false);
            ground.getVertices().add(Vector.of(100, 100));
            ground.getVertices().add(Vector.of(100, 50));
            ground.getVertices().add(Vector.of(800, 50));
            ground.getVertices().add(Vector.of(800, 100));
            ground.computeCenterOfMass();
            pWorld.getBodies().add(ground);

            PPolygon leftWall = new PPolygon("Left Wall");
            leftWall.setMoveable(false);
            leftWall.getVertices().add(Vector.of(50, 50));
            leftWall.getVertices().add(Vector.of(100, 50));
            leftWall.getVertices().add(Vector.of(100, 500));
            leftWall.getVertices().add(Vector.of(50, 500));
            leftWall.computeCenterOfMass();
            pWorld.getBodies().add(leftWall);

            PPolygon rightWall = new PPolygon("Right Wall");
            rightWall.setMoveable(false);
            rightWall.getVertices().add(Vector.of(800, 50));
            rightWall.getVertices().add(Vector.of(850, 50));
            rightWall.getVertices().add(Vector.of(850, 500));
            rightWall.getVertices().add(Vector.of(800, 500));
            rightWall.computeCenterOfMass();
            pWorld.getBodies().add(rightWall);

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    PPolygon box = new PPolygon("Box");
                    box.setMass(10);

                    box.getVertices().add(Vector.of(110 + 40 * i, 110 + 40 * j + 30));
                    box.getVertices().add(Vector.of(110 + 40 * i + 30, 110 + 40 * j + 30));
                    box.getVertices().add(Vector.of(110 + 40 * i + 30, 110 + 40 * j + 60));
                    box.getVertices().add(Vector.of(110 + 40 * i, 110 + 40 * j + 60));

                    box.computeCenterOfMass();
                    pWorld.getBodies().add(box);
                }
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureSimulate_onBoxes(PWorldCollapsingBoxesState state, Blackhole blackhole) {
        for (int i = 0; i < 400; i++) {
            state.pWorld.simulate(state.timeEllapsed);
        }

        blackhole.consume(state.pWorld);
    }

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(PWorldPerfTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
