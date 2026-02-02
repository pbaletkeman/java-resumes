package ca.letkeman.resumes;

import ca.letkeman.resumes.model.Optimize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BackgroundResumeTest {

    @Test
    void testBackgroundResumeDefaultConstructor() {
        BackgroundResume backgroundResume = new BackgroundResume();
        Assertions.assertNotNull(backgroundResume);
        Assertions.assertNull(backgroundResume.getOptimize());
    }

    @Test
    void testSetOptimize() {
        BackgroundResume backgroundResume = new BackgroundResume();

        Optimize optimize = new Optimize();
        optimize.setResume("Test resume");

        backgroundResume.setOptimize(optimize);
        Assertions.assertEquals(optimize, backgroundResume.getOptimize());
    }

    @Test
    void testGetOptimize() {
        BackgroundResume backgroundResume = new BackgroundResume();
        Assertions.assertNull(backgroundResume.getOptimize());

        Optimize optimize = new Optimize();
        backgroundResume.setOptimize(optimize);
        // Note: getOptimize() returns a defensive copy, so we use assertEquals instead of assertSame
        Assertions.assertEquals(optimize, backgroundResume.getOptimize());
    }
}
