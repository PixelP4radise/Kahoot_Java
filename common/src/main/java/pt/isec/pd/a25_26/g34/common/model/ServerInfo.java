package pt.isec.pd.a25_26.g34.common.model;

import java.io.Serial;
import java.io.Serializable;

public record ServerInfo(
        String ip,
        int tcpClientPort,
        int tcpReplicaPort,
        int udpPort
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
