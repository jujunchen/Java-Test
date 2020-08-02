/*
import sun.misc.Unsafe;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

*/
/**
 * @author jujun chen
 * @date 2020/06/02
 *//*

public class BigLetterEndianTest {

    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception ex) {

        }
        return null;
    }

    */
/**
     * 大端小端测试
     *//*

    public static void main(String[] args) throws IOException {
        long a = getUnsafe().allocateMemory(8);
        getUnsafe().putLong(a, 0x0102030405060708L);

        byte b = getUnsafe().getByte(a);
        ByteOrder byteOrder = null;
        switch (b) {
            case 0x01:
                 byteOrder = ByteOrder.BIG_ENDIAN;
                break;
            case 0x08:
                byteOrder = ByteOrder.LITTLE_ENDIAN;
                break;
            default:
                assert false;
                byteOrder = null;
        }


        byteOrder = ByteOrder.nativeOrder();
        //LITTLE_ENDIAN
        System.out.println(byteOrder);

        byte[] arr = new byte[4];
        arr[0] = 0x78;
        arr[1] = 0x56;
        arr[2] = 0x34;
        arr[3] = 0x12;
        ByteArrayInputStream ba = new ByteArrayInputStream(arr);

        DataInputStream ds = new DataInputStream(ba);
        System.out.println(Integer.toHexString(ds.readInt()));

//        System.out.println(Integer.toHexString(10010));

        int x = 0x01020304;

        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[4]);
        byteBuffer.asIntBuffer().put(x);
        String before = Arrays.toString(byteBuffer.array());
        System.out.println("默认字节序："+byteBuffer.order().toString() + "," + "内存数据："+before);

       */
/* byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.asIntBuffer().put(x);
        String after = Arrays.toString(byteBuffer.array());
        System.out.println("修改字节序："+byteBuffer.order().toString()+","+"内存数据："+after);*//*



        IntBuffer intBuffer = IntBuffer.wrap(new int[4]);
        intBuffer.put(x);
        before = Arrays.toString(intBuffer.array());
        System.out.println("默认字节序："+intBuffer.order().toString() + "," + "内存数据："+before);

    }

}
*/
