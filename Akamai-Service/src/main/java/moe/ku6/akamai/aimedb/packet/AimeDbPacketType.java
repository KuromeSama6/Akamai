package moe.ku6.akamai.aimedb.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/*
    Packet types for AimeDb packets
    See https://sega.bsnk.me/allnet/aimedb/packet_index/ for a list of known packet types.
 */
@AllArgsConstructor
@Getter
public enum AimeDbPacketType {
    FELICA_RESPONSE(3),

    GET_FELICA_ID(1, FELICA_RESPONSE),
    REGISTER_FELICA_ID(2, FELICA_RESPONSE),

    AIME_ACCOUNT_RESPONSE(6),
    GET_AIME_ACCOUNT(4, AIME_ACCOUNT_RESPONSE),
    REGISTER_AIME_ACCOUNT(5, AIME_ACCOUNT_RESPONSE),

    STATUS_LOG_RESPONSE(8),
    SEND_STATUS_LOG(7, STATUS_LOG_RESPONSE),

    AIME_LOG_RESPONSE(10),
    SEND_AIME_LOG(9, AIME_LOG_RESPONSE),

    CAMPAIGN_INFO_RESPONSE(12),
    GET_CAMPAIGN_INFO(11, CAMPAIGN_INFO_RESPONSE),

    CAMPAIGN_CLEAR_INFO_RESPONSE(14),
    GET_CAMPAIGN_CLEAR_INFO(13, CAMPAIGN_CLEAR_INFO_RESPONSE),

    ACCOUNT_EX_RESPONSE(16),
    GET_ACCOUNT_EX(15, ACCOUNT_EX_RESPONSE),

    ACCOUNT_EX_SECURE_RESPONSE(18),
    GET_ACCOUNT_EX_SECURE(17, ACCOUNT_EX_SECURE_RESPONSE),

    AIME_LOG_EX_RESPONSE(20),
    SEND_AIME_LOG_EX(19, AIME_LOG_EX_RESPONSE),

    SERVICE_ALIVE_CHECK(100),
    SERVICE_ALIVE_STATUS(101),
    CLIENT_END(102);

    private final int id;
    private final AimeDbPacketType responseType;

    AimeDbPacketType(int id) {
        this.id = id;
        this.responseType = null;
    }

    public static AimeDbPacketType GetById(int id) {
        return Arrays.stream(values())
                .filter(v -> v.id == id)
                .findFirst()
                .orElse(null);
    }
}
