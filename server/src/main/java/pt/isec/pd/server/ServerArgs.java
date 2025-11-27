package pt.isec.pd.server;

import pt.isec.pd.a25_26.g34.common.utils.ArgsParser;
import pt.isec.pd.a25_26.g34.common.utils.Log;

public class ServerArgs {
    private static final String TAG = "CONFIG";

    private final String directoryIp;
    private final int directoryPort;
    private final String dbDirectory;
    private final String multicastInterfaceIp;

    private ServerArgs(String directoryIp, int directoryPort, String dbDirectory, String multicastInterfaceIp) {
        this.directoryIp = directoryIp;
        this.directoryPort = directoryPort;
        this.dbDirectory = dbDirectory;
        this.multicastInterfaceIp = multicastInterfaceIp;
    }

    public static ServerArgs parse(String[] args) {
        ArgsParser parser = ArgsParser.parse(args);

        if (!parser.checkParam("-ip", "-p", "-c", "-i")) {
            printUsage();
            return null;
        }

        String dirIp = parser.getString("-ip");
        int dirPort = parser.getInt("-p");
        String dbDir = parser.getString("-c");
        String multicastInterfaceIp = parser.getString("-i");

        if (dirPort < 1 || dirPort > 65535) {
            Log.error(TAG, "O porto da diretoria deve estar entre 1 e 65535.");
            return null;
        }

        return new ServerArgs(dirIp, dirPort, dbDir, multicastInterfaceIp);
    }

    private static void printUsage() {
        String tag = "USAGE";

        // Cabeçalho a indicar o problema
        Log.error(tag, "Sintaxe incorreta ou argumentos insuficientes.");
        Log.error(tag, "Comando correto:");
        Log.error(tag, "java -jar server.jar [FLAGS]");

        // Lista de flags
        Log.error(tag, "  -ip  <IP>      Endereço IP do Serviço de Diretoria");
        Log.error(tag, "  -p   <PORT>    Porto UDP do Serviço de Diretoria");
        Log.error(tag, "  -c   <PATH>    Caminho da diretoria da Base de Dados");
        Log.error(tag, "  -i <IP>      IP da interface local para Multicast");
    }

    public String getDirectoryIp() {
        return directoryIp;
    }

    public int getDirectoryPort() {
        return directoryPort;
    }

    public String getDbDirectory() {
        return dbDirectory;
    }

    public String getMulticastInterfaceIp() {
        return multicastInterfaceIp;
    }

    @Override
    public String toString() {
        return String.format("Dir[%s:%d] | BD[%s] | Multi[%s]",
                directoryIp, directoryPort, dbDirectory, multicastInterfaceIp);
    }
}
