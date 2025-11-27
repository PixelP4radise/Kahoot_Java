package pt.isec.pd.server;

import pt.isec.pd.a25_26.g34.common.utils.Log;

public class ServerMain {
    private static final String TAG = "ServerMain";

    public static void main(String[] args) {
        Log.debug(TAG, "Argumentos recebidos (" + args.length + "): " + java.util.Arrays.toString(args));
        Log.info(TAG, "A processar configuração...");

        ServerArgs config = ServerArgs.parse(args);

        if (config == null) {
            Log.warn(TAG, "A encerrar devido a configuração inválida");
            return;
        }

        Log.success(TAG, "Configuração carregada: " + config.toString());
        Log.debug("CONFIG", config.toString());

        try {
            Log.info(TAG, "A iniciar sistema...");


        } catch (Exception e) {
            Log.error(TAG, "Erro fatal no servidor");
            Log.error(TAG, e);
        }
    }
}
