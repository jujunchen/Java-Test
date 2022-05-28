package onJava.enums.multi;
import static onJava.enums.multi.Outcome.*;

/**
 * 枚举分发，实现热比较器compete
 */
public enum Ro2 implements  Competitor<Ro2> {

    //3个值，分别为 “布”与“布”的胜负，“布”与“剪刀”胜负，“布”与“石头”胜负
    PAPER(DRAW, LOSE, WIN),
    //剪刀与布、剪刀、石头胜负
    SCISSORS(WIN,DRAW, LOSE),
    //石头与布，剪刀、石头胜负
    ROCK(LOSE, WIN, DRAW)
        ;


    private Outcome vPAPER, vSCISSORS, vROCK;

    Ro2(Outcome paper, Outcome scissors, Outcome rock) {
        this.vPAPER = paper;
        this.vSCISSORS = scissors;
        this.vROCK = rock;
    }

    @Override
    public Outcome compete(Ro2 it) {
        switch (it) {
            case PAPER: return vPAPER;
            case SCISSORS: return vSCISSORS;
            case ROCK: return vROCK;
            default:
        }
        return null;
    }

    public static void main(String[] args) {
        Ro.play(Ro2.class, 20);
    }

}
