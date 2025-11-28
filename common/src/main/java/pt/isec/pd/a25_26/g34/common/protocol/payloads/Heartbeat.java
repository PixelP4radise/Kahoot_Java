package pt.isec.pd.a25_26.g34.common.protocol.payloads;

import pt.isec.pd.a25_26.g34.common.model.ServerInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

public record Heartbeat(
        ServerInfo serverInfo,
        long dbVersion,
        String lastSqlQuery
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Heartbeat(ServerInfo serverInfo, long dbVersion) {
        this(serverInfo, dbVersion, null);
    }

    public Optional<String> getSqlQuery() {
        return Optional.ofNullable(lastSqlQuery);
    }
}