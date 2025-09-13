package ar.edu.utn.frba.Server.ServicioEstadisticas;

final class CsvExporter {

    private CsvExporter() {}


    static String esc(String s) {
        if (s == null) return "";
        boolean needs = s.contains(",") || s.contains("\n") || s.contains("\r") || s.contains("\"");
        String v = s.replace("\"", "\"\"");
        return needs ? "\"" + v + "\"" : v;
    }

    static String row(String... cells) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(esc(cells[i]));
        }
        sb.append('\n');
        return sb.toString();
    }

    static String kvHeader(String k, String v) {
        return row(k, v);
    }

    static String kv(String key, String value) {
        return row(key, value);
    }

    static String kv(String key, long value) {
        return row(key, String.valueOf(value));
    }
}