package xdean.spring.auto;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import xdean.annotation.processor.toolkit.ElementUtil;
import xdean.annotation.processor.toolkit.XAbstractProcessor;
import xdean.annotation.processor.toolkit.annotation.SupportedAnnotation;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotation(AutoSpringFactories.class)
public class AutoSpringFactoriesProcessor extends XAbstractProcessor {

  public static final String META_INF_SPRING_FACTORIES = "META-INF/spring.factories";
  private Map<String, List<String>> map = new HashMap<>();

  @Override
  public boolean processActual(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
      throws AssertException {
    if (roundEnv.processingOver()) {
      try {
        generateFile();
      } catch (IOException e) {
        e.printStackTrace();
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
              .forEach(tm -> map.computeIfAbsent(tm.toString(), k -> new ArrayList<>()).add(name));
        });
  }

  private void generateFile() throws IOException {
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

  protected <T> Assert<T> assertThat(T t, boolean b) {
    return new Assert<>(t, b);
  }
}
