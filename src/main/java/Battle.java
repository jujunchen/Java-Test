import kilim.ExitMsg;
import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jujun chen
 * @date 2020/08/07
 */
public class Battle {
/*
    static Mailbox<String> mb = new Mailbox<>();
    static Mailbox<ExitMsg> exitmb = new Mailbox<>();
    int type = 0; //Task的类型区分，0表示接收者，1表示发送者

    public KilimTest(int type) {
        this.type = type;
    }

    public static void main(String[] args) {
        if (kilim.tools.Kilim.trampoline(false,args)) return;

        Task sender = new KilimTest(1).start();
        Task reciever = new KilimTest(0).start();
        //要求接收者在结束后，填写退出邮箱
        reciever.informOnExit(exitmb);
        //退出邮箱中的内容，表示接收者已经结束
        exitmb.getb();
        System.exit(0);
    }

    @Override
    public void execute() throws Pausable {
        if (type == 0) {
            while (true) {
                String s = mb.get(); //取得邮箱中的信息
                if (s.equals("over")) //如果是结束标记
                    break;
                System.out.println(s);
            }
        }else if (type == 1) {  //发送者
            mb.putnb("Hello ");  //向邮箱中传递信息
            mb.putnb("World\n");
            mb.putnb("over");
        }
    }*/

    static Random rand = new Random();
    int num = 1000;
    Actor [] actors = new Actor[num];
    AtomicInteger living = new AtomicInteger(num);


    public class Actor extends Task {
        Mailbox<Integer> damage = new Mailbox<>();
        int hp = 1 + rand.nextInt(10);

        @Override
        public void execute() throws Pausable {
            while (hp > 0) {
                hp -= damage.get();
                actors[rand.nextInt(num)].damage.putnb(1);
                actors[rand.nextInt(num)].damage.putnb(1);
                actors[rand.nextInt(num)].damage.putnb(1);
                Task.sleep(100);
            }
            living.decrementAndGet();
        }
    }

    void start() {
        for (int ii=0; ii < num; ii++) (actors[ii] = new Actor()).start();
        actors[0].damage.putb(1);

        for (int cnt, prev=num; (cnt=living.get()) > num/2 || cnt < prev; prev=cnt, sleep())
            System.out.println(cnt);

    }

    static void sleep() {
        try { Thread.sleep(100); }
        catch (InterruptedException ex) {}
    }

    public static void main(String [] args) {
//        if (kilim.tools.Kilim.trampoline(false,args)) return;
        Battle battle = new Battle();
        battle.start();
        Task.idledown();
        System.out.format("\n%d actors survived the Battle Royale\n\n",battle.living.get());
    }
}



