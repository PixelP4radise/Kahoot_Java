package pt.isec.pd.a25_26.g34.common.utils;

import java.util.HashMap;
import java.util.Map;

public class ArgsParser {
    private final Map<String, String> parameters;
    private static final String TAG = "ARGS";

    private ArgsParser() {
        parameters = new HashMap<>();
    }

    public static ArgsParser parse(String[] args) {
        ArgsParser parser = new ArgsParser();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.startsWith("--")) {
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    parser.parameters.put(arg, args[i + 1]);
                } else {
                    parser.parameters.put(arg, "true");
                }
            }
        }

        return parser;
    }

    public String getString(String key) {
        return parameters.get(key);
    }

    public int getInt(String key) {
        String value = parameters.get(key);
        if (value == null) return -1;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Log.error(TAG, "O argumento " + key + " espera um número inteiro (recebido: " + value + ").");
            return -1;
        }
    }

    public boolean has(String key) {
        return parameters.containsKey(key);
    }

    public boolean checkParam(String... requiredKeys) {
        for (String key : requiredKeys) {
            if (!parameters.containsKey(key)) {
                Log.error(TAG, "Falta o argumento obrigatório: " + key);
                return false;
            }
        }
        return true;
    }
}
