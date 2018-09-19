# Auto `spring.factories`
[![Build Status](https://travis-ci.org/XDean/auto-spring-factories.svg?branch=master)](https://travis-ci.org/XDean/auto-spring-factories)
[![codecov.io](http://codecov.io/github/XDean/auto-spring-factories/coverage.svg?branch=master)](https://codecov.io/gh/XDean/auto-spring-factories/branch/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.XDean/auto-spring-factories/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.XDean/auto-spring-factories)

Auto generate `spring.factories` in type-safe way.

# Get it

```xml
<dependency>
    <groupId>com.github.XDean</groupId>
    <artifactId>auto-spring-factories</artifactId>
    <version>0.1.2</version>
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

- Only support top-level class
