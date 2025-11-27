package pt.isec.pd.a25_26.g34.common.protocol.payloads;

import java.io.Serializable;
import java.util.Optional;

public class Heartbeat implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serverIp;
    private int tcpPortClients;
    private int tcpPortReplicas;

    private long dbVersion;

    private String sqlQuery;

    public Heartbeat(String serverIp, int tcpPortClients, int tcpPortReplicas, long dbVersion) {
        this.serverIp = serverIp;
        this.tcpPortClients = tcpPortClients;
        this.tcpPortReplicas = tcpPortReplicas;
        this.dbVersion = dbVersion;
        this.sqlQuery = null;
    }

    public Heartbeat(String serverIp, int tcpPortClients, int tcpPortReplicas, long dbVersion, String sqlQuery) {
        this.serverIp = serverIp;
        this.tcpPortClients = tcpPortClients;
        this.tcpPortReplicas = tcpPortReplicas;
        this.dbVersion = dbVersion;
        this.sqlQuery = sqlQuery;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getTcpPortClients() {
        return tcpPortClients;
    }

    public int getTcpPortReplica() {
        return tcpPortReplicas;
    }

    public long getDbVersion() {
        return dbVersion;
    }

    public Optional<String> getSqlQuery() {
        return Optional.ofNullable(sqlQuery);
    }

    @Override
    public String toString() {
        return String.format("Heartbeat{v=%d, query=%s, clientePort=%d, replicaPort=%d}",
                dbVersion,
                getSqlQuery().isPresent() ? getSqlQuery().get() : null,
                tcpPortClients,
                tcpPortReplicas);
    }
}
