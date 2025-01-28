package moe.ku6.akamai.data.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Base class for ALL.Net game accounts.
 */
@Document
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ALLNetGameAccount {
    @Id
    private String id;
    private int aimeId;
    private int playCount;
    private PlaySessionData firstPlay;
    private PlaySessionData lastPlay;
}
