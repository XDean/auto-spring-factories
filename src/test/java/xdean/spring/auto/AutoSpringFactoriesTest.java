package xdean.spring.auto;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.google.common.io.CharStreams;
import com.google.testing.compile.Compilation;

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
    compilation.generatedFiles().forEach(jf -> {
      try {
        System.out.println(CharStreams.readLines(new InputStreamReader(jf.openInputStream(), StandardCharsets.UTF_8)));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }
}
