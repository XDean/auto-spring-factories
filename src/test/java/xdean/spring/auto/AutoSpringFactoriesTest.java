package xdean.spring.auto;

import static org.junit.Assert.assertEquals;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.tools.StandardLocation;

import com.google.common.io.CharStreams;
import com.google.testing.compile.Compilation;

import xdean.annotation.processor.toolkit.CommonUtil;
import xdean.test.compile.CompileTestCase;
import xdean.test.compile.Compiled;

public class AutoSpringFactoriesTest extends CompileTestCase {
  @Compiled(
      processors = AutoSpringFactoriesProcessor.class,
      sources = {
          "TestAutoConfig.java",
          "TestAutoConfig2.java"
      })
  public void testName(Compilation compilation) throws Exception {
    Path golden = Paths.get(AutoSpringFactoriesTest.class.getResource("expect.factories").toURI());

    assertEquals(Files.readAllLines(golden).stream().collect(Collectors.joining("\n")),
        compilation.generatedFile(StandardLocation.CLASS_OUTPUT, AutoSpringFactoriesProcessor.META_INF_SPRING_FACTORIES)
            .map(f -> CommonUtil.uncheck(() -> CharStreams
                .readLines(new InputStreamReader(f.openInputStream(), StandardCharsets.UTF_8))).stream()
                .collect(Collectors.joining("\n")))
            .orElseThrow(() -> new AssertionError()));
  }
}
