package pt.isec.pd.a25_26.g34.common.protocol.payloads;

import java.io.Serializable;

public class ServerRegistration implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int tcpPortForClients;
    private final int tcpPortForServers;

    public ServerRegistration(int tcpPortForCLients, int tcpPortForServers) {
        this.tcpPortForClients = tcpPortForCLients;
        this.tcpPortForServers = tcpPortForServers;
    }

    public int getTcpPortForClients() {
        return this.tcpPortForClients;
    }

    public int getTcpPortForServers() {
        return this.tcpPortForServers;
    }

    @Override
    public String toString() {
        return "Registo{PortoClientes=" + tcpPortForClients + ", PortoReplica=" + tcpPortForServers + "}";
    }
}
