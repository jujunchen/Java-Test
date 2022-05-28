package onJava.enums.multi;

import java.util.EnumMap;
import onJava.enums.multi.Enums.*;

import static onJava.enums.multi.Outcome.*;

public enum Ro5 implements Competitor<Ro5> {

    PAPER, SCISSORS, ROCK;

    static EnumMap<Ro5, EnumMap<Ro5, Outcome>> table = new EnumMap<Ro5, EnumMap<Ro5, Outcome>>(Ro5.class);

    static {
        for (Ro5 it : Ro5.values()) {
            table.put(it, new EnumMap<Ro5, Outcome>(Ro5.class));
        }
        //初始化好预期的类型和结果
        initRow(PAPER, DRAW, LOSE, WIN);
        initRow(SCISSORS, WIN, DRAW, LOSE);
        initRow(ROCK, LOSE, WIN, DRAW);
    }

    static void initRow(Ro5 it, Outcome vPAPER, Outcome vSCISSORS, Outcome vROCK) {
        EnumMap<Ro5, Outcome> row = Ro5.table.get(it);

        row.put(Ro5.PAPER, vPAPER);
        row.put(Ro5.SCISSORS, vSCISSORS);
        row.put(Ro5.ROCK, vROCK);
    }


    @Override
    public Outcome compete(Ro5 it) {
        return table.get(this).get(it);
    }

    public static void main(String[] args) {
        Ro.play(Ro5.class, 20);
    }
}
