# lib-version

A version-aware Maven library for **DODA25 Team 9**.  
It provides a simple `VersionUtil` class that allows applications to retrieve the library version at runtime.

---

## Features

- Reads version from **MANIFEST.MF** when packaged as a JAR  
- Falls back to `version.properties` during development  
- Returns `"unknown"` if no version information is available  
- Automatically published to **GitHub Packages** on tagged releases
- Incremental versioning on pushes to main
- Automatic pre‑releases to **GitHub Packages** of feature branches
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
Note: Often the `main` branch is protected and you cannot push directly to it. To allow the `release.yml` workflow to push the version bump to `main`, you must create a Personal Access Token with scope `repo` and add it as a repository secret with name `GH_PAT`. You also need to whitelist the account the PAT is linked to in your branch protection rule for main. 

### Tag a version
On running:
```bash
git tag v1.0.0
git push origin v1.0.0
```

GitHub Actions will:
1. Update `version.properties` and `pom.xml` for the build with the tag version
2. Build the library  
3. Publish to **GitHub Packages**
4. Bump `version.properties` and `pom.xml` on main to the next incremental version

### Pushing to main
On pushing to `main`, like when merging a pull request, GitHub Actions will:

1. Read the version from `pom.xml` and remove "-SNAPSHOT"
2. Update `version.properties` and `pom.xml` for the build
3. Build the library  
4. Publish to **GitHub Packages**
5. Bump `version.properties` and `pom.xml` on main to the next incremental version

### Pushing to any branch except main
On pushing to any branch other than `main`, GitHub Actions will:

1. Read the version from `pom.xml`
2. Generate a pre‑release version string including the branch name and a timestamp  
3. Update `version.properties` and `pom.xml` with this pre‑release version for the build
4. Build the library  
5. Publish to **GitHub Packages**

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

This repository contains two workflows in `.github/workflows/`. The workflow `release.yml` publishes on a push to main or a tag, it automatically increases the version number in `version.properties` and `pom.xml`. The workflow`pre-release.yml` publishes a pre-release on every push to a feature branch. 

### `release.yml`
1. **Trigger** 
    The workflow is triggered by a Git tag matching `v*.*.*` or a push to the `main` branch.  
2. **Steps**
  The workflow:
  - Extracts the version number from the tag if triggered by a tag or from `pom.xml` if triggered by a push.  
  - Updates `pom.xml` and `version.properties` to that version number without "-SNAPSHOT".  
  - Builds and deploys the library to **GitHub Packages**.  
  - Calculates the next incremental version (`X.Y.(Z+1)-SNAPSHOT`).
  - Updates `pom.xml` and `version.properties` with the next incremental version.
  - Commits the updates files to main. 

### `pre-release.yml` – Branch Pre‑Releases
1. **Trigger** 
    The workflow is triggered by a push to any branch except `main`.  
2. **Steps**
  The workflow:
  - Reads the base version from `pom.xml`.  
  - Generates a pre‑release version string that includes branch name and timestamp.
  - Updates `pom.xml` and `version.properties` with this pre‑release version for deployment.  
  - Deploys to **GitHub Packages**.  


---

## Troubleshooting

**401 Unauthorized Error**  
- Ensure `~/.m2/settings.xml` is configured with your GitHub token
- Verify token has `read:packages` scope
- Check that `<id>github</id>` matches in both `settings.xml` and `pom.xml`

---