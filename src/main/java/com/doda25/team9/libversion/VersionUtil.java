package com.doda25.team9.libversion;

import java.io.InputStream;
import java.util.Properties;

public class VersionUtil {

    private static final String UNKNOWN = "unknown";

    public static String getVersion() {
        Package pkg = VersionUtil.class.getPackage();
        String version = pkg.getImplementationVersion();

        if (version != null && !version.isBlank()) {
            return version;
        }

        return loadFromProperties();
    }

    private static String loadFromProperties() {
        try (InputStream input = VersionUtil.class
                .getClassLoader()
                .getResourceAsStream("version.properties")) {

            if (input == null) {
                return UNKNOWN;
            }

            Properties p = new Properties();
            p.load(input);
            return p.getProperty("version", UNKNOWN);

        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public static void printVersion() {
        System.out.println("lib-version = " + getVersion());
    }
}
