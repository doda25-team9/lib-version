# lib-version

A version-aware Maven library for **DODA25 Team 9**.  
It provides a simple `VersionUtil` class that allows applications to retrieve the library version at runtime.

---

## Features

- Reads version from **MANIFEST.MF** when packaged as a JAR  
- Falls back to `version.properties` during development  
- Returns `"unknown"` if no version information is available  
- Automatically published to **GitHub Packages** on releases
- Automatic version bumping after stable releases
- Automatic pre‑releases to **GitHub Packages** from feature branches

---

## Setup

### Authentication

GitHub Packages requires authentication to download packages. Configure Maven by creating or editing `~/.m2/settings.xml`:
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

**Get a token:**
1. Go to https://github.com/settings/tokens
2. Click "Generate new token (classic)"
3. Select scope: `read:packages`
4. Generate and copy the token (starts with `ghp_`)

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

### Stable Release
To create a stable release:

1. Go to **Actions** → **Release Stable Version**
2. Click **Run workflow**
3. Select branch: `main`
4. Click **Run workflow**

GitHub Actions will:
1. Read version from `pom.xml` (e.g., `2.0.1-SNAPSHOT`)
2. Remove `-SNAPSHOT` and update files to release version
3. Create git commit with release version
4. Create git tag (e.g., `v2.0.1`) pointing to that commit
5. Build and publish to **GitHub Packages**
6. Create GitHub Release with JAR attachment
7. Automatically bump to next version (e.g., `2.0.2-SNAPSHOT`)
8. Commit and push version bump to main

**Published versions:** https://github.com/doda25-team9/lib-version/packages

### Pre-releases (Feature Branches)
On pushing to any branch other than `main`, GitHub Actions will:

1. Read the version from `pom.xml`
2. Generate a pre‑release version string including the branch name and a timestamp  
3. Update `version.properties` and `pom.xml` with this pre‑release version for the build
4. Build the library  
5. Publish to **GitHub Packages**

**Example pre-release version:** `2.0.1-feature-auth-250115-143045`

---

## Project Structure
```
lib-version/
├── src/main/java/.../VersionUtil.java
├── src/main/resources/version.properties
├── .github/workflows/release.yml
├── .github/workflows/pre-release.yml
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

## Workflows

This repository contains two workflows in `.github/workflows/`. The workflow `release.yml` creates stable releases with git tagging and version bumping. The workflow `pre-release.yml` publishes a pre-release on every push to a feature branch. 

### `release.yml` – Stable Releases
1. **Trigger**  
    Manual via workflow_dispatch (Actions → "Release Stable Version" → Run workflow)

2. **Steps**  
  The workflow:
  - Reads the version from `pom.xml` and removes `-SNAPSHOT`
  - Updates `pom.xml` and `version.properties` to release version
  - Creates git commit with release version
  - Creates git tag (e.g., `v2.0.1`) pointing to that commit
  - Builds and deploys the library to **GitHub Packages**
  - Creates GitHub Release with JAR attachment
  - Calculates the next incremental version (`X.Y.(Z+1)-SNAPSHOT`)
  - Updates `pom.xml` and `version.properties` with the next version
  - Commits and pushes changes to main

### `pre-release.yml` – Branch Pre‑Releases
1. **Trigger**  
    Automatic on push to any branch except `main`

2. **Steps**  
  The workflow:
  - Reads the base version from `pom.xml`
  - Generates a pre‑release version string that includes branch name and timestamp
  - Updates `pom.xml` and `version.properties` with this pre‑release version for deployment
  - Deploys to **GitHub Packages**

---

## Troubleshooting

**401 Unauthorized Error**  
- Ensure `~/.m2/settings.xml` is configured with your GitHub token
- Verify token has `read:packages` scope
- Check that `<id>github</id>` matches in both `settings.xml` and `pom.xml`

---