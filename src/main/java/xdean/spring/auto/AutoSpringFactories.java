package xdean.spring.auto;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Auto generate 'spring.factories'.
 *
 * @author Dean Xu (XDean@github.com)
 */
@Retention(SOURCE)
@Target(TYPE)
@Documented
public @interface AutoSpringFactories {
  /**
   * The key in 'spring.factories'
   */
  Class<?>[] value();
}
