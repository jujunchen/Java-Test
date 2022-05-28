package onJava.enums.multi;
import onJava.enums.multi.Enums.*;

import static onJava.enums.multi.Outcome.*;

/**
 * 枚举类，实现比较器接口，通过switch来判断传入参数
 */
public enum Ro3 implements Competitor<Ro3> {

    PAPER{
        @Override
        public Outcome compete(Ro3 it) {
            switch (it) {
                case PAPER: return DRAW;
                case SCISSORS:return LOSE;
                case ROCK: return WIN;
                default: return null;
            }
        }
    },
    SCISSORS {
        @Override
        public Outcome compete(Ro3 it) {
            switch (it) {
                case PAPER: return WIN;
                case SCISSORS: return DRAW;
                case ROCK: return LOSE;
                default: return null;
            }
        }
    },
    ROCK {
        @Override
        public Outcome compete(Ro3 it) {
            switch (it) {
                case PAPER: return LOSE;
                case SCISSORS: return WIN;
                case ROCK: return DRAW;
                default: return null;
            }
        }
    }
    ;


    @Override
    public abstract Outcome compete(Ro3 competitor);

    public static void main(String[] args) {
        Ro.play(Ro3.class, 20);
    }
}
