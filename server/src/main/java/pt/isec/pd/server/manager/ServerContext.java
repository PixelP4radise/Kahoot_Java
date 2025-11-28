package pt.isec.pd.server.manager;

import pt.isec.pd.a25_26.g34.common.constants.Constants;
import pt.isec.pd.a25_26.g34.common.model.ServerInfo;
import pt.isec.pd.a25_26.g34.common.utils.Log;
import pt.isec.pd.server.ServerArgs;

import java.io.IOException;
import java.net.*;

public class ServerContext {
    private static final String TAG = "ServerContext";

    private final ServerArgs config;

    private ServerInfo serverInfo;
    private long dbVersion;

    private ServerSocket tcpClientSocket;
    private ServerSocket tcpReplicaSocket;
    private MulticastSocket multicastSocket;
    private DatagramSocket unicastSocket;

    public ServerContext(ServerArgs config) {
        this.config = config;
        this.dbVersion = 0;
    }

    public void initialize() throws IOException {
        Log.info(TAG, "A inicializar recursos de rede...");

        this.tcpClientSocket = new ServerSocket(0);
        Log.debug(TAG, "Socket TCP Clientes aberto na porta: " + tcpClientSocket.getLocalPort());

        this.tcpReplicaSocket = new ServerSocket(0);
        Log.debug(TAG, "Socket TCP Réplicas aberto na porta: " + tcpReplicaSocket.getLocalPort());

        this.unicastSocket = new DatagramSocket();
        Log.debug(TAG, "Socket UDP Unicast (Diretoria) aberto na porta: " + unicastSocket.getLocalPort());

        setupMulticast();

        String myIp = InetAddress.getLocalHost().getHostAddress();
        this.serverInfo = new ServerInfo(myIp, tcpClientSocket.getLocalPort(), tcpReplicaSocket.getLocalPort(), unicastSocket.getLocalPort());

        Log.success(TAG, "Contexto inicializado com sucesso. Identidade: " + serverInfo);
    }

    private void setupMulticast() throws IOException {
        Log.debug(TAG, "A configurar Multicast para o grupo " + Constants.MULTICAST_IP + ":" + Constants.MULTICAST_PORT);

        this.multicastSocket = new MulticastSocket(Constants.MULTICAST_PORT);

        InetAddress multicastInterfaceIp = InetAddress.getByName(config.getMulticastInterfaceIp());
        NetworkInterface netIf = NetworkInterface.getByInetAddress(multicastInterfaceIp);

        if (netIf == null) {
            Log.error(TAG, "Interface de rede não encontrada para o IP: " + config.getMulticastInterfaceIp());
            throw new IOException("Interface de rede não encontrada");
        }

        Log.debug(TAG, "Interface de rede selecionada: " + netIf.getName() + " (" + netIf.getDisplayName() + ")");

        InetSocketAddress group = new InetSocketAddress(Constants.MULTICAST_IP, Constants.MULTICAST_PORT);
        this.multicastSocket.joinGroup(group, netIf);

        Log.success(TAG, "Juntou-se ao grupo Multicast com sucesso");
    }

    public ServerInfo getInfo() {
        return serverInfo;
    }

    public ServerArgs getConfig() {
        return config;
    }

    public long getDbVersion() {
        return dbVersion;
    }

    public MulticastSocket getMulticastSocket() {
        return multicastSocket;
    }

    public DatagramSocket getUnicastSocket() {
        return unicastSocket;
    }

    public synchronized void incrementDbVersion() {
        this.dbVersion++;
    }
}
