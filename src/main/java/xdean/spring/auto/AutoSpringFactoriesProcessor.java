package xdean.spring.auto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.google.auto.service.AutoService;

import xdean.annotation.processor.toolkit.AssertException;
import xdean.annotation.processor.toolkit.CommonUtil;
import xdean.annotation.processor.toolkit.ElementUtil;
import xdean.annotation.processor.toolkit.XAbstractProcessor;
import xdean.annotation.processor.toolkit.annotation.SupportedAnnotation;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotation(AutoSpringFactories.class)
public class AutoSpringFactoriesProcessor extends XAbstractProcessor {

  public static final String META_INF_SPRING_FACTORIES = "META-INF/spring.factories";
  private Map<String, Set<String>> map = new HashMap<>();

  @Override
  public boolean processActual(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
      throws AssertException {
    if (roundEnv.processingOver()) {
      try {
        generateFile();
      } catch (IOException e) {
        error().log("Fail to generate spring.factories: " + CommonUtil.getStackTraceString(e));
      }
    }
    collectClass(roundEnv);
    return false;
  }

  private void collectClass(RoundEnvironment roundEnv) {
    ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(AutoSpringFactories.class))
        .stream()
        .map(te -> assertThat(te, te.getNestingKind() == NestingKind.TOP_LEVEL)
            .todo(() -> error().log("Only can annotated on top-level type.", te)))
        .forEach(t -> {
          String name = t.getQualifiedName().toString();
          AutoSpringFactories anno = t.getAnnotation(AutoSpringFactories.class);
          ElementUtil.getAnnotationClassArray(elements, anno, a -> a.value())
              .forEach(tm -> map.computeIfAbsent(tm.toString(), k -> new LinkedHashSet<>()).add(name));
        });
  }

  private void generateFile() throws IOException {
    try {
      Properties p = new Properties();
      FileObject origin = filer.getResource(StandardLocation.CLASS_OUTPUT, "", META_INF_SPRING_FACTORIES);
      InputStream input = origin.openInputStream();
      p.load(input);
      p.forEach((k, v) -> map.computeIfAbsent(k.toString(), key -> new LinkedHashSet<>()).addAll(
          Arrays.stream(v.toString().split(","))
              .map(String::trim)
              .collect(Collectors.toSet())));
      input.close();
    } catch (Exception e) {
      debug().log("Error to read origin spring.factories: " + CommonUtil.getStackTraceString(e));
    }

    FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", META_INF_SPRING_FACTORIES);
    OutputStream output = resource.openOutputStream();
    PrintStream writer = new PrintStream(output, false, StandardCharsets.UTF_8.name());
    map.forEach((k, v) -> {
      writer.println(k + "=\\");
      writer.println(v.stream().collect(Collectors.joining(",\\\n")));
      writer.println();
    });
    writer.flush();
    writer.close();
  }
}
