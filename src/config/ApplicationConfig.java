package config;

import enums.Role;

/**
 * used to maintain any default configuration
 * such as default role/ default password for users
 * or some other defaults
 */
public class ApplicationConfig {
    public static String getDefaultPassword(){
        return "123";
    }

    public static Role getDefaultRole(){
        return Role.VIEWER;
    }
}
