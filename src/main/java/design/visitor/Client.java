package design.visitor;

/**
 * @author jujun chen
 * @date 2020/07/20
 */
public class Client {

    public static void main(String[] args) {
        ObjectStructure objectStructure = new ObjectStructure();

        objectStructure.attach(new Man());
        objectStructure.attach(new Woman());

        Success success = new Success();
        objectStructure.display(success);


        Fail fail = new Fail();
        objectStructure.display(fail);
    }
}
