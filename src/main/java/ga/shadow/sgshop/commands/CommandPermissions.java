package ga.shadow.sgshop.commands;

import me.totalfreedom.totalfreedommod.rank.Rank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermissions {
    //Credit to TFM developers
    Rank level();

    SourceType source();

}