package com.clup.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    public static List<String[]> readAll(String path) throws IOException {
        List<String[]> rows = new ArrayList<>();
        Path p = Paths.get(path);
        if (!Files.exists(p)) return rows;
        try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(parse(line));
            }
        }
        return rows;
    }

    public static void writeAll(String path, List<String[]> rows) throws IOException {
        Path p = Paths.get(path);
        Files.createDirectories(p.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
            for (String[] r : rows) {
                bw.write(serialize(r));
                bw.newLine();
            }
        }
    }

    // Very simple CSV escaping: wrap in quotes if contains comma/quote/semicolon
    private static String serialize(String[] r) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < r.length; i++) {
            if (i > 0) sb.append(",");
            String v = r[i] == null ? "" : r[i];
            if (v.contains(",") || v.contains("\"") || v.contains(";")) {
                v = "\"" + v.replace("\"", "\"\"") + "\"";
            }
            sb.append(v);
        }
        return sb.toString();
    }

    private static String[] parse(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        sb.append('"'); i++;
                    } else {
                        inQuotes = false;
                    }
                } else sb.append(c);
            } else {
                if (c == '"') inQuotes = true;
                else if (c == ',') { result.add(sb.toString()); sb.setLength(0); }
                else sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }
}
