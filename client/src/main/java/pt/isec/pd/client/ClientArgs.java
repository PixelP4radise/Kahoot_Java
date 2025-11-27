package pt.isec.pd.client;

import pt.isec.pd.a25_26.g34.common.utils.ArgsParser;
import pt.isec.pd.a25_26.g34.common.utils.Log;

public class ClientArgs {
    private static final String TAG = "CONFIG";

    private final String directoryIp;
    private final int directoryPort;

    private ClientArgs(String directoryIp, int directoryPort) {
        this.directoryIp = directoryIp;
        this.directoryPort = directoryPort;
    }

    public static ClientArgs parse(String[] args) {
        ArgsParser parser = ArgsParser.parse(args);

        if (!parser.checkParam("-ip", "-p")) {
            printUsage();
            return null;
        }

        String ip = parser.getString("-ip");
        int port = parser.getInt("-p");

        if (port < 0 || port > 65535) {
            Log.error(TAG, "O porto da diretoria deve estar entre 1 e 65535");
            return null;
        }

        return new ClientArgs(ip, port);
    }

    private static void printUsage() {
        String tag = "USAGE";
        Log.error(tag, "Argumentos insuficientes.");
        Log.error(tag, "Sintaxe correta:");
        Log.error(tag, "java -jar client.jar [FLAGS]");
        Log.error(tag, "  -ip <IP>      Endereço IP do Serviço de Diretoria");
        Log.error(tag, "  -p  <PORT>    Porto UDP do Serviço de Diretoria");
    }

    public String getDirectoryIp() {
        return directoryIp;
    }

    public int getDirectoryPort() {
        return directoryPort;
    }

    @Override
    public String toString() {
        return String.format("Diretoria em %s:%d", directoryIp, directoryPort);
    }
}
