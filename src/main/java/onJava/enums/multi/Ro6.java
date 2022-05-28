package onJava.enums.multi;
import static onJava.enums.multi.Outcome.*;

public enum Ro6 implements Competitor<Ro6> {
    PAPER, SCISSORS, ROCK;

    //布、剪刀、石头
    private static Outcome[][] table = {
            {DRAW, LOSE, WIN},
            {WIN, DRAW, LOSE},
            {LOSE, WIN, DRAW}
    };

    /**
     *      布       剪刀     石头
     * 布    DRAW    LOSE    WIN
     * 剪刀   WIN     DRAW    LOSE
     * 石头   LOSE    WIN     DRAW
     *
     */

    @Override
    public Outcome compete(Ro6 other) {
        return table[this.ordinal()][other.ordinal()];
    }

    public static void main(String[] args) {
        Ro.play(Ro6.class, 20);
    }
}
