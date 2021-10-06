import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ExprGen {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: generate_ast <files>");
            System.exit(64);
        }

        for (String path : args) {
            defineAst("", "", Files.readAllLines(Paths.get(path)));
        }
    }

    private static void defineAst(
            String outputDir, String baseName, List<String> types)
            throws IOException {
        for (String line : types) {
            if (line != null && !line.isEmpty()) {
                if (line.startsWith("@")) {
                    String[] parts = line.split("=");

                    switch (parts[0].trim()) {
                        case "@outdir":
                            outputDir = parts[1];
                            break;

                        case "@basename":
                            baseName = parts[1];
                    }
                }
            }
        }
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");




        writer.println("package io.github.npex42.jlox.backend;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("public abstract class " + baseName + " {");



        defineVisitor(writer, baseName, types);

        // The AST classes.
        for (String type : types) {
            if (type == null) {continue;}
            if (type.isBlank() || type.isEmpty()) {continue;}
            if (type.startsWith("@")) {continue;}


            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();

            System.out.println("Defining Type ["+className+" | "+fields+"]");

            defineType(writer, baseName, className, fields);
        }

        // The base accept() method.
        writer.println();
        writer.println(" public abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineType(
            PrintWriter writer, String baseName,
            String className, String fieldList) {
        writer.println(" public static class " + className + " extends " +
                baseName + " {");

        // Constructor.
        writer.println(" public " + className + "(" + fieldList + ") {");

        // Store parameters in fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // Fields.
        writer.println();
        for (String field : fields) {
            writer.println(" public final " + field + ";");
        }

        writer.println();
        writer.println("    @Override");
        writer.println("    public <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" +
                className + baseName + "(this);");
        writer.println("    }");

        writer.println("  }");
    }

    private static void defineVisitor(
            PrintWriter writer, String baseName, List<String> types) {
        writer.println(" public interface Visitor<R> {");

        for (String type : types) {
            if (type == null) {continue;}
            if (type.isBlank() || type.isEmpty()) {continue;}
            if (type.startsWith("@")) {continue;}

            String typeName = type.split(":")[0].trim();
            writer.println("  public R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("  }");
    }
}