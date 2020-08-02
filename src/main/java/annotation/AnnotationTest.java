package annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author jujun chen
 * @date 2020/03/01
 */
public class AnnotationTest {
    public static void main(String[] args) {
        Annotation[] annotations = ChinesePeople.class.getAnnotations();
        System.out.println(Arrays.toString(annotations));
        Annotation[] annotations1 = ChinesePeople.class.getDeclaredAnnotations();
        System.out.println(Arrays.toString(annotations1));
        Name nameAnnotation = ChinesePeople.class.getAnnotation(Name.class);
        assert nameAnnotation == null;
    }
}
