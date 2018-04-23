package xdean.spring.auto;

import static org.junit.Assert.assertEquals;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.tools.StandardLocation;

import org.junit.Test;

import com.google.common.io.CharStreams;
import com.google.testing.compile.Compilation;

import xdean.annotation.processor.toolkit.CommonUtil;
import xdean.annotation.processor.toolkit.test.CompileTest;
import xdean.annotation.processor.toolkit.test.Compiled;

public class AutoSpringFactoriesTest extends CompileTest {
  @Test
  @Compiled(
      processors = AutoSpringFactoriesProcessor.class,
      sources = {
          "TestAutoConfig.java"
      })
  public void testName(Compilation compilation) throws Exception {
    Path golden = Paths.get(AutoSpringFactoriesTest.class.getResource("expect.factories").toURI());

    assertEquals(Files.readAllLines(golden),
        compilation.generatedFile(StandardLocation.CLASS_OUTPUT, AutoSpringFactoriesProcessor.META_INF_SPRING_FACTORIES)
            .map(f -> CommonUtil
                .uncheck(() -> CharStreams.readLines(new InputStreamReader(f.openInputStream(), StandardCharsets.UTF_8))))
            .orElseThrow(() -> new AssertionError()));
  }
}
