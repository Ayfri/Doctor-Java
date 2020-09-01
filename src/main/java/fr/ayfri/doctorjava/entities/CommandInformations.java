package fr.ayfri.doctorjava.entities;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInformations {
	String name();
	
	String description() default "";
	
	Category category() default Category.OWNER;
	
	Tag[] tags() default {};
	
	String usage() default "";
	
	String[] aliases() default {};
}
