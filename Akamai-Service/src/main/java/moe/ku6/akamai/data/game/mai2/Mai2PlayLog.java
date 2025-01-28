package moe.ku6.akamai.data.game.mai2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moe.ku6.akamai.data.game.mai2.judgement.NoteType;
import moe.ku6.akamai.util.JsonWrapper;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Document("sega_game_mai2_playlog")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mai2PlayLog {
    @Id
    private String id;
    @DBRef
    private Mai2Account account;
    private JsonWrapper fullData;

    private int musicId;
    private int versionRaw;
    private DateTime date;
    private int trackNumber;
    private int dxScore;
    private int maxCombo, totalCombo;

    private int fastCount;
    private int lateCount;

    private boolean newRecord;
    private boolean dxScoreNewRecord;
    private boolean trackCleared;

    private int ratingBefore, ratingAfter;
}
