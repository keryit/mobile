package femi.core.test_rail_client.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.METHOD})
public @interface TestRailCase {

    //automation id in TestRail (if any).
    //NOTE: "automation_id" custom field needs to be added in TestRail
    String value() default "";
    String [] testCaseNames() default {};
    //if true, any value for automation id is ignored
    //lets the listener know that it should not raise a warning for no automation id
    boolean selfReporting() default false;

}
