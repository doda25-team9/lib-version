# lib-version

A version-aware Maven library for **DODA25 Team 9**.  
It provides a simple `VersionUtil` class that allows applications to retrieve the library version at runtime.

---

## Features

- Reads version from **MANIFEST.MF** when packaged as a JAR  
- Falls back to `version.properties` during development  
- Returns `"unknown"` if no version information is available  
- Automatically published to **GitHub Packages** on tagged releases

---

## Usage

### Add to Your Project

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/doda25-team9/lib-version</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.doda25.team9</groupId>
        <artifactId>lib-version</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### In Your Code

```java
import com.doda25.team9.libversion.VersionUtil;

String version = VersionUtil.getVersion();
System.out.println("Library version: " + version);
```

---

## Build

```bash
mvn clean package
```

Output JAR:

```
target/lib-version.jar
```

---

## Release (Automated)

Tag a version:

```bash
git tag v1.0.0
git push origin v1.0.0
```

GitHub Actions will:

1. Update the Maven version  
2. Update `version.properties`  
3. Build the library  
4. Publish to **GitHub Packages**

---

## Project Structure

```
lib-version/
├── src/main/java/.../VersionUtil.java
├── src/main/resources/version.properties
├── .github/workflows/release.yml
└── pom.xml
```

---

## How Version Detection Works

1. **Packaged JAR**  
   Reads `Implementation-Version` from the JAR manifest.

2. **During Development**  
   Uses `src/main/resources/version.properties`.

3. **Fallback**  
   Returns `"unknown"` when neither source is available.

---
