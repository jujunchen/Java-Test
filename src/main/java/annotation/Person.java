package annotation;


/**
 * @author jujun chen
 * @date 2020/03/01
 */
@Name
@Age
public class Person implements Animal{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
