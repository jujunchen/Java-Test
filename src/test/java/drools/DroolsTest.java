package drools;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/15
 */
public class DroolsTest {

    public KieContainer kieContainer;

    @Before
    public void init() {
        KieServices ks = KieServices.Factory.get();
        kieContainer = ks.getKieClasspathContainer();
    }

    @Test
    public void test1() {
        KieSession kSession = kieContainer.newKieSession("ksession-rule");

        Product product = new Product();
        product.setType(Product.GOLD);

        kSession.insert(product);
        int count = kSession.fireAllRules();
        System.out.println("命中了" + count + "条规则");
        System.out.println("商品" + product.getType() + "的商品折扣为" + product.getDiscount() + "%");
    }

    @Test
    public void containsTest() {
        KieSession kieSession = kieContainer.newKieSession("contains");
        School school = new School().setClassName("三班").setClassCount(30);
        Person person = new Person().setName("张三").setAge(18).setClassName("三班");

        kieSession.insert(school);
        kieSession.insert(person);

        int count = kieSession.fireAllRules();
        System.out.println("命中了" + count + "条规则");
    }

    @Test
    public void memberOfTest() {
        KieSession kieSession = kieContainer.newKieSession("memberof");
        School school = new School().setClassCount(10).setClassName("三班").setClassNameAry(new String[]{"一班", "二班", "三班"});
        Person person = new Person().setName("张三").setClassName("三班").setAge(18);

        kieSession.insert(school);
        kieSession.insert(person);

        int count = kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("一共执行了" + count + "条规则");
    }

    @Test
    public void matchesTest() {
        KieSession kieSession = kieContainer.newKieSession("matches");
        Person person = new Person().setName("zsss").setClassName("三班").setAge(18);
        kieSession.insert(person);
        int count = kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("一共执行了" + count + "条规则");
    }

    @Test
    public void soundsTest() {
        KieSession kieSession = kieContainer.newKieSession("soundslike");
        Person person = new Person().setName("halo").setClassName("三班").setAge(18);
        kieSession.insert(person);
        int count = kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("一共执行了" + count + "条规则");
    }


    @Test
    public void strTest() {
        KieSession kieSession = kieContainer.newKieSession("str");
        Person person = new Person().setName("张三").setClassName("三班").setAge(18);
        kieSession.insert(person);
        int count = kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("一共执行了" + count + "条规则");
    }


    @Test
    public void noloopTest() {
        KieSession kieSession = kieContainer.newKieSession("noloop");
        Person person = new Person().setName("张三").setClassName("三班").setAge(30);
        kieSession.insert(person);
        int count = kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("一共执行了" + count + "条规则");
    }


}
