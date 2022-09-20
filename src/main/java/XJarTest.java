import io.xjar.XCryptos;

public class XJarTest {

    public static void main(String[] args) throws Exception {
        String path = "/Users/chenjujun/greentown-projects/business-middle-platform/bmp-ability-iot/bmp-ability-iot-service-platform-model/target/";
        XCryptos.encryption()
                .from(path + "bmp-ability-iot-service-platform-model.jar")
                .use("passwad")
                .exclude("/static/**/*")
                .exclude("/templates/**/*")
                .exclude("/META-INF/resources/**/*")
                .to(path + "bmp-ability-iot-service-platform-model-encryption.jar");
        System.out.println("success");
    }
}
