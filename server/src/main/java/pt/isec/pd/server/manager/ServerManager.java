package pt.isec.pd.server.manager;

import pt.isec.pd.a25_26.g34.common.utils.Log;
import pt.isec.pd.server.ServerArgs;

import java.io.IOException;

public class ServerManager {
    private static final String TAG = "ServerManager";

    private final ServerContext serverContext;
    private final HeartbeatService heartbeatService;

    public ServerManager(ServerArgs config) {
        this.serverContext = new ServerContext(config);
        this.heartbeatService = new HeartbeatService(serverContext);
    }

    public void start() {
        try {
            Log.info(TAG, "A inicializar contexto e recursos...");

            serverContext.initialize();

            Log.success(TAG, "Identidade definida: " + serverContext.getInfo());

            Log.info(TAG, "A arrancar serviços de rede...");
            heartbeatService.start();

            // Futuro: Iniciar serviço de Base de Dados
            // Futuro: Iniciar serviço de atendimento a clientes (TCP)

        } catch (IOException e) {
            Log.error(TAG, "Falha crítica no arranque do servidor.");
            Log.error(TAG, e);
            stop();
        }
    }

    public void stop() {
        heartbeatService.stop();
        // serverContext.close(); // Implementar fecho de sockets se necessário
    }
}