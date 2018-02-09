package com.enjin.java_commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>Operations on {@link File}.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in detail.</p>
 *
 * @since 1.0
 */
public class FileUtils {

    /**
     * <p>{@code FileUtils} instances should NOT be constructed in standard programming.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public FileUtils() {
        super();
    }

    /**
     * <p>Attempts to read the UTF-8 contents of a file at the provided path
     * and returns the contents as a {@link String} or {@code null} if the
     * path or file is empty.</p>
     *
     * @param path the path of the file to read
     *
     * @return a {@link String} of the file contents or null if {@code path} or the file is empty
     *
     * @throws InvalidPathException if the provided path is invalid
     * @throws IOException          if an I/O error occurs opening or reading the file
     * @throws SecurityException    if a security violation is detected
     * @since 1.0
     */
    public static String getFileContents(final String path) throws IOException {
        String contents = null;

        if (!StringUtils.isEmpty(path)) {
            StringBuilder builder = new StringBuilder();
            Path p = Paths.get(path);
            try (BufferedReader reader = Files.newBufferedReader(p)) {
                int ch;
                while ((ch = reader.read()) >= 0)
                    builder.append((char) ch);
            }

            if (builder.length() > 0)
                contents = builder.toString();
        }

        return contents;
    }

}
