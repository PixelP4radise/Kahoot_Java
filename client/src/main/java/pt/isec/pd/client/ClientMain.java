package pt.isec.pd.client;

import pt.isec.pd.a25_26.g34.common.utils.Log;

public class ClientMain {
    private static final String TAG = "ClientMain";

    public static void main(String[] args) {
        Log.debug(TAG, "Argumentos recebidos (" + args.length + "): " + java.util.Arrays.toString(args));
        Log.info(TAG, "A processar configuração...");

        ClientArgs config = ClientArgs.parse(args);

        if (config == null) {
            Log.warn(TAG, "A encerrar devido a configuração inválida");
            return;
        }

        Log.success(TAG, "Configuração carregada: " + config.toString());

        try {
            Log.info(TAG, "A iniciar sistema...");
            
        } catch (Exception e) {
            Log.error(TAG, "Erro fatal no serviço de diretoria");
            Log.error(TAG, e);
        }
    }
}
