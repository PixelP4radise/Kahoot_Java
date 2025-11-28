package pt.isec.pd.server.manager;

import pt.isec.pd.a25_26.g34.common.constants.Constants;
import pt.isec.pd.a25_26.g34.common.model.ServerInfo;
import pt.isec.pd.a25_26.g34.common.protocol.payloads.Heartbeat;
import pt.isec.pd.a25_26.g34.common.utils.Log;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class HeartbeatService {
    private static final String TAG = "HearbeatService";

    private final ServerContext context;

    private Thread listenerThread;
    private Thread senderThread;

    private volatile boolean isRunning;

    public HeartbeatService(ServerContext context) {
        this.context = context;
        this.isRunning = false;
    }

    public void start() {
        if (isRunning) return;
        this.isRunning = true;

        listenerThread = new Thread(new ListeningTask(), "Hb-Listener");
        listenerThread.start();

        senderThread = new Thread(new SendingTask(), "Hb-Sender");
        senderThread.start();

        Log.info(TAG, "Serviço de Hearbeats iniciado.");
    }

    public void stop() {
        isRunning = false;
        if (senderThread != null) senderThread.interrupt();
        if (listenerThread != null) listenerThread.interrupt();
        Log.info(TAG, "A parar serviço de Heartbeats...");
    }

    private class ListeningTask implements Runnable {
        @Override
        public void run() {
            byte[] buffer = new byte[4096];
            while (isRunning) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    context.getMulticastSocket().receive(packet);

                    Heartbeat hb = deserialize(packet.getData());

                    if (isMyOwnHeartbeat(hb.serverInfo())) continue;

                    Log.info(TAG, "Recebido de " + hb.serverInfo().ip() + " (v" + hb.dbVersion() + ")");
                } catch (IOException e) {
                    if (isRunning) Log.error(TAG, "Erro I/O Listener: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    Log.error(TAG, "Pacote desconhecido recebido.");
                }
            }
        }
    }

    private class SendingTask implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    sendHeartbeatPulse();

                    Thread.sleep(Constants.HEARTBEAT_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Log.debug(TAG, "Sender interrompido.");
                    return;
                } catch (Exception e) {
                    Log.error(TAG, "Erro no envio: " + e.getMessage());
                }
            }
        }
    }

    private Heartbeat deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Heartbeat) ois.readObject();
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return baos.toByteArray();
    }

    private boolean isMyOwnHeartbeat(ServerInfo other) {
        return other.tcpClientPort() == context.getInfo().tcpClientPort();
    }

    private void sendHeartbeatPulse() {
        try {
            Heartbeat hb = new Heartbeat(context.getInfo(), context.getDbVersion());
            byte[] data = serialize(hb);

            sendUDP(data, Constants.MULTICAST_IP, Constants.MULTICAST_PORT);

            sendUDP(data, context.getConfig().getDirectoryIp(), context.getConfig().getDirectoryPort());
            Log.debug(TAG, "Pulse enviado (v" + context.getDbVersion() + ")");

        } catch (IOException e) {
            Log.error(TAG, "Falha de rede ao enviar pulse: " + e.getMessage());
        }
    }

    private void sendUDP(byte[] data, String ip, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(ip), port);
        context.getUnicastSocket().send(packet);
    }
}

