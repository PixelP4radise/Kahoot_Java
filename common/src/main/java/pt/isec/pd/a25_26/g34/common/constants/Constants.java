package pt.isec.pd.a25_26.g34.common.constants;

public class Constants {
    // Multicast
    public static final String MULTICAST_IP = "230.30.30.30";
    public static final int MULTICAST_PORT = 3030;

    // Timeouts
    public static final int DIRECTORY_TIMEOUT_MS = 17000; // 17 segundos [cite: 149]
    public static final int AUTH_TIMEOUT_MS = 30000;      // 30 segundos [cite: 158]
    public static final int HEARTBEAT_INTERVAL_MS = 5000; // 5 segundos [cite: 125]

    // Caminhos
    public static final String DB_FILE_EXTENSION = ".db";
}
