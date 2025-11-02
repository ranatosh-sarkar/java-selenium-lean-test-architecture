package com.automationframework.utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Stream;

public final class AllureReportServer {

    private AllureReportServer() {}

    public static void main(String[] args) throws Exception {
        // 1) Resolve report root
        Path reportRoot = resolveReportRoot(args);
        if (!Files.isDirectory(reportRoot) || !Files.exists(reportRoot.resolve("index.html"))) {
            throw new IllegalArgumentException("Allure report not found at: " + reportRoot +
                    " (missing index.html)");
        }

        // 2) Resolve port (arg → -Dreport.port → default 8089), try a few if taken
        int port = resolvePort(args);
        HttpServer server = null;
        for (int attempt = 0; attempt < 8; attempt++) {
            try {
                server = HttpServer.create(new InetSocketAddress(port + attempt), 0);
                port = port + attempt;
                break;
            } catch (IOException bind) {
                // try next port
            }
        }
        if (server == null) throw new IOException("Could not bind to ports 8089..8096");

        // 3) Serve files
        final Path root = reportRoot.toAbsolutePath().normalize();
        server.createContext("/", ex -> serve(root, ex));
        server.setExecutor(null);
        server.start();

        System.out.println("[ReportServer] Serving: " + root);
        System.out.println("[ReportServer] URL:     http://localhost:" + port + "/");

        try { Desktop.getDesktop().browse(new URI("http://localhost:" + port + "/")); }
        catch (Exception ignored) {}
    }

    private static Path resolveReportRoot(String[] args) throws IOException {
        // Priority 1: program arg 0
        if (args != null && args.length >= 1 && args[0] != null && !args[0].isBlank()) {
            return Paths.get(args[0]).toAbsolutePath().normalize();
        }
        // Priority 2: system property
        String sysProp = System.getProperty("allure.report.dir");
        if (sysProp != null && !sysProp.isBlank()) {
            return Paths.get(sysProp).toAbsolutePath().normalize();
        }
        // Priority 3: newest reports/allure-report-*
        Path base = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
        Path reportsDir = base.resolve("reports");
        if (!Files.isDirectory(reportsDir)) {
            throw new IllegalStateException("No 'reports' directory under " + base);
        }
        
        try (Stream<Path> stream = Files.list(reportsDir)) {
        	  return stream
        	    .filter(p -> Files.isDirectory(p) &&
        	                 p.getFileName().toString().startsWith("allure-report-") &&
        	                 Files.exists(p.resolve("index.html")))
        	    .max(Comparator.comparingLong(p -> {
        	        try { return Files.getLastModifiedTime(p.resolve("index.html")).toMillis(); }
        	        catch (IOException e) { return 0L; }
        	    }))
        	    .orElseThrow(() -> new IllegalStateException(
        	        "No 'allure-report-*' folder with index.html under " + reportsDir));
        	}
        
//        try (Stream<Path> stream = Files.list(reportsDir)) {
//            return stream
//                    .filter(p -> Files.isDirectory(p) &&
//                                 p.getFileName().toString().startsWith("allure-report-"))
//                    .max(Comparator.comparingLong(p -> p.toFile().lastModified()))
//                    .orElseThrow(() -> new IllegalStateException(
//                            "No 'allure-report-*' folder found under " + reportsDir));
//        }
    }

    private static int resolvePort(String[] args) {
        if (args != null && args.length >= 2) {
            try { return Integer.parseInt(args[1]); } catch (Exception ignored) {}
        }
        String prop = System.getProperty("report.port");
        if (prop != null) {
            try { return Integer.parseInt(prop); } catch (Exception ignored) {}
        }
        return 8089;
    }

    private static void serve(Path root, HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        if (path.endsWith("/")) path += "index.html";
        Path file = root.resolve(path.substring(1)).normalize();

        if (!file.startsWith(root) || !Files.exists(file) || Files.isDirectory(file)) {
            ex.sendResponseHeaders(404, -1);
            return;
        }
        byte[] bytes = Files.readAllBytes(file);
        ex.getResponseHeaders().add("Content-Type", mime(file));
        ex.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    private static String mime(Path f) {
        String n = f.getFileName().toString().toLowerCase(Locale.ROOT);
        if (n.endsWith(".html")) return "text/html";
        if (n.endsWith(".js"))   return "application/javascript";
        if (n.endsWith(".css"))  return "text/css";
        if (n.endsWith(".json")) return "application/json";
        if (n.endsWith(".png"))  return "image/png";
        if (n.endsWith(".svg"))  return "image/svg+xml";
        if (n.endsWith(".jpg") || n.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream";
    }
}
