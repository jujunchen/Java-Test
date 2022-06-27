package onJava.clone;

import lombok.Data;

public class CloneTest {

    public static void main(String[] args) throws CloneNotSupportedException {
        Person person = new Person();
        person.setName("xxxx");
        Person wife = new Person();
        wife.setName("林志玲");
        person.setWife(wife);
        System.out.println(person.getName() + ":" + person.getWife().getName());

        //这里的修改不影响person
        Person person1 = person.clone();
        person1.setName("222");
        //对象类型拷贝的只是别名，引用还在
        person1.getWife().setName("赵薇");
        System.out.println(person1.getName() + ":" + person1.getWife().getName());
        System.out.println(person.getName() + ":" + person.getWife().getName());
    }
}

/*
输出
xxxx:林志玲
222:赵薇
xxxx:赵薇
*/

@Data
class Person implements Cloneable {
    private String name;
    private String age;
    private Person wife;

    @Override
    protected Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }
}
