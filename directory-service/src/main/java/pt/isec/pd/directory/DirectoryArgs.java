package pt.isec.pd.directory;

import pt.isec.pd.a25_26.g34.common.utils.ArgsParser;
import pt.isec.pd.a25_26.g34.common.utils.Log;

public class DirectoryArgs {
    private static final String TAG = "CONFIG";

    private final int listeningPort;

    private DirectoryArgs(int listeningPort) {
        this.listeningPort = listeningPort;
    }

    public static DirectoryArgs parse(String[] args) {
        ArgsParser parser = ArgsParser.parse(args);

        if (!parser.checkParam("-p")) {
            printUsage();
            return null;
        }

        int port = parser.getInt("-p");

        if (port < 1 || port > 65535) {
            Log.error(TAG, "O porto deve estar entre 1 e 65535 (Recebido: " + port + ").");
            return null;
        }

        return new DirectoryArgs(port);
    }

    private static void printUsage() {
        String tag = "USAGE";
        Log.error(tag, "Argumentos inválidos.");
        Log.error(tag, "Sintaxe correta:");
        Log.error(tag, "java -jar directory-service.jar -p <PORT>");
    }

    public int getListeningPort() {
        return listeningPort;
    }

    @Override
    public String toString() {
        return "Porto de Escuta: " + listeningPort;
    }
}
