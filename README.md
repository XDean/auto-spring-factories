# Auto `spring.factories`
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.XDean/auto-spring-factories/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.XDean/auto-spring-factories)

Auto generate `spring.factories` in type-safe way.

# Get it

```xml
<dependency>
    <groupId>com.github.XDean</groupId>
    <artifactId>auto-spring-factories</artifactId>
    <version>x.x</version>
    <scope>provided</scope>
</dependency>
```

# Usage

For example, you write a custom auto-configuration `MyAutoConfiguration`.

You may create `META-INF/spring.factories` in `resource` and write

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
your.package.MyAutoConfiguration
```

But now you can use `auto-spring-factories`:

```java
@AutoSpringFactories(EnableAutoConfiguration.class)
public class MyAutoConfiguration{
}
```

It will auto generate `spring.factories`.

# Notice

- Don't manually add `spring.factories` if use this tool.
- Only support top-level class now.