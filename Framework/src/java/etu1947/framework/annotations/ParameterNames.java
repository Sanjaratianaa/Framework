package etu1947.framework.annotations;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterNames {
    String[] value();
}
