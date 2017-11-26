package ga.shadow.sgshop.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters {
    //Credit to TFM developers
    String description();

    String usage();

    String aliases() default ""; // "alias1,alias2,alias3" - no spaces
}