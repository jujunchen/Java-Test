package spring;
import	java.lang.annotation.ElementType;
import	java.lang.annotation.Target;
import	java.lang.annotation.RetentionPolicy;
import	java.lang.annotation.Retention;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ClientName {
}
