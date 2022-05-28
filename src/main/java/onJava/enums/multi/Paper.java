package onJava.enums.multi;
import static onJava.enums.multi.Outcome.*;

/**
 * 布
 */
public class Paper implements Item {
    @Override
    public Outcome compete(Item it) {
        //传入参数与本类比较，返回传入参数的比较结果
        return it.eval(this);
    }

    @Override
    public Outcome eval(Paper p) {
        return DRAW;
    }

    @Override
    public Outcome eval(Scissors s) {
        return WIN;
    }

    @Override
    public Outcome eval(Rock r) {
        return LOSE;
    }

    @Override
    public String toString() {
        return "Paper";
    }
}
