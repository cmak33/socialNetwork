package com.project.socialnetwork.auxiliaryMethods;

import com.project.socialnetwork.models.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAuxiliaryMethods {
    public static void setLoggedInUserToSprintContext(User user){
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(user);
        SecurityContext mockContext = mock(SecurityContext.class);
        when(mockContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockContext);
    }
}
