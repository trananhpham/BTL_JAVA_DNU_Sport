package com.clup.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvUtill {
    public static List<String[]> readAll(String var0) throws IOException {
        ArrayList var1 = new ArrayList();
        Path var2 = Paths.get(var0);
        if (!Files.exists(var2, new LinkOption[0])) {
            return var1;
        } else {
            String var4;
            try (BufferedReader var3 = Files.newBufferedReader(var2, StandardCharsets.UTF_8)) {
                while((var4 = var3.readLine()) != null) {
                    var1.add(parse(var4));
                }
            }

            return var1;
        }
    }

    public static void writeAll(String var0, List<String[]> var1) throws IOException {
        Path var2 = Paths.get(var0);
        Files.createDirectories(var2.getParent());

        try (BufferedWriter var3 = Files.newBufferedWriter(var2, StandardCharsets.UTF_8)) {
            for(String[] var5 : var1) {
                var3.write(serialize(var5));
                var3.newLine();
            }
        }

    }

    private static String serialize(String[] var0) {
        StringBuilder var1 = new StringBuilder();

        for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var2 > 0) {
                var1.append(",");
            }

            String var3 = var0[var2] == null ? "" : var0[var2];
            if (var3.contains(",") || var3.contains("\"") || var3.contains(";")) {
                var3 = "\"" + var3.replace("\"", "\"\"") + "\"";
            }

            var1.append(var3);
        }

        return var1.toString();
    }

    private static String[] parse(String var0) {
        ArrayList var1 = new ArrayList();
        boolean var2 = false;
        StringBuilder var3 = new StringBuilder();

        for(int var4 = 0; var4 < var0.length(); ++var4) {
            char var5 = var0.charAt(var4);
            if (var2) {
                if (var5 == '"') {
                    if (var4 + 1 < var0.length() && var0.charAt(var4 + 1) == '"') {
                        var3.append('"');
                        ++var4;
                    } else {
                        var2 = false;
                    }
                } else {
                    var3.append(var5);
                }
            } else if (var5 == '"') {
                var2 = true;
            } else if (var5 == ',') {
                var1.add(var3.toString());
                var3.setLength(0);
            } else {
                var3.append(var5);
            }
        }

        var1.add(var3.toString());
        return (String[])var1.toArray(new String[0]);
    }
}
