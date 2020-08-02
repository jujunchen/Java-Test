import java.util.concurrent.atomic.AtomicInteger;

public class SNUtil {

    private final static char[] BASE_CHARS=new char[]{'0','1','2','3','4','5','6','7','8','9'} ;
    private final static AtomicInteger SEQ2= new AtomicInteger(Integer.MAX_VALUE);
    private final static AtomicInteger SEQ3= new AtomicInteger(Integer.MAX_VALUE);

    public static String nextseq2(){
        int nextSequence=SEQ2.incrementAndGet();
        if(nextSequence>=0){
            nextSequence=Integer.MAX_VALUE-nextSequence;
        }else{
            nextSequence=Integer.MAX_VALUE+1+nextSequence;
        }
        int mod=nextSequence%(BASE_CHARS.length*BASE_CHARS.length);
        return BASE_CHARS[mod/ BASE_CHARS.length]+""+BASE_CHARS[mod% BASE_CHARS.length];
    }

    public static String nextseq3(){
        int nextSequence=SEQ3.incrementAndGet();
        if(nextSequence>=0){
            nextSequence=Integer.MAX_VALUE-nextSequence;
        }else{
            nextSequence=Integer.MAX_VALUE+1+nextSequence;
        }
        int mod=nextSequence%(BASE_CHARS.length*BASE_CHARS.length*BASE_CHARS.length);
        int mod1=nextSequence%(BASE_CHARS.length*BASE_CHARS.length);
        return BASE_CHARS[mod/(BASE_CHARS.length* BASE_CHARS.length)]+""+ BASE_CHARS[mod1/ BASE_CHARS.length]+""+BASE_CHARS[mod1% BASE_CHARS.length];
    }


    /**
     * 16位流水编码
     * 格式：13位时间串+3位顺序码,保证流水的唯一性
     * 毫秒时间内并发达到62*62*62=238328这个数量编号才会重复 （目前适合单机，集群可以考虑添加机器标示码）
     * @return
     */
    public static String createSN3(){
        StringBuffer sb=new StringBuffer();
        sb.append(System.currentTimeMillis());//系统当前毫秒值
        sb.append(nextseq3());//3位顺序码
        return sb.toString();
    }

    /**
     * 15位流水编码
     * 格式：13位时间串+2位顺序码,保证流水的唯一性
     * 毫秒时间内并发达到62*62=3844这个数量编号才会重复 （目前适合单机，集群可以考虑添加机器标示码）
     * @return
     */
    public static String createSN2(){
        StringBuffer sb=new StringBuffer();
        sb.append(System.currentTimeMillis());//系统当前毫秒值
        sb.append(nextseq2());//2位顺序码
        return sb.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(){
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        System.out.println(SNUtil.createSN3());
                        System.out.println(SNUtil.createSN2());
                    }
                }

            }.start();

        }

    }

}