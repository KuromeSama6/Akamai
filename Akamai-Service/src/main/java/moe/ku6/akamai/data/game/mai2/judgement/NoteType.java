package moe.ku6.akamai.data.game.mai2.judgement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NoteType {
    TAP("Tap"),
    HOLD("Hold"),
    SLIDE("Slide"),
    TOUCH("Touch"),
    BREAK("Break");

    private String noteTypeSuffix;
}
