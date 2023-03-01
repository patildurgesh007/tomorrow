package com.tomorrow.queueSystem.utility;

import com.tomorrow.queueSystem.persistence.User;

/**
 * This is Static Utility class to perform Utility tasks which is reusable in entire application
 */
public class UtilsImpl {

    public static boolean isCurrentUserRoleWithin(User user, RoleEnum... roles){
        if(user != null && !user.getRoles().isEmpty()){
            for(RoleEnum role : roles){
                if(user.getRoles().stream().anyMatch(roleUser -> roleUser.getName().equalsIgnoreCase(role.name()))){
                    return true;
                }
            }
        }
        return false;
    }
}
