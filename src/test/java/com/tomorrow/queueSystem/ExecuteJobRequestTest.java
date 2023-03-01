package com.tomorrow.queueSystem;

import com.tomorrow.queueSystem.persistence.Job;
import com.tomorrow.queueSystem.persistence.JobRequest;
import com.tomorrow.queueSystem.persistence.Role;
import com.tomorrow.queueSystem.persistence.User;
import com.tomorrow.queueSystem.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;

@Nested
@SpringBootTest
@Order(1)
public class ExecuteJobRequestTest extends AbstractTest {

    @Mock
    UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    public void submitFirstUserTest() throws Exception {
        passwordEncoder = new BCryptPasswordEncoder();
        String uri = "http://localhost:8080/userManagement/firstUser";
        User user = new User();
        user.setName("TEST_ADMIN");
        user.setPassword("TEST_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_ADMIN"));
        user.setRoles(roles);
        String encryptedPassword =  passwordEncoder.encode("TEST_ADMIN");
        user.setPassword(encryptedPassword);
        user.setEmailAddress("test@test.com");

        String inputJson = super.mapToJson(user);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @WithMockUser(username = "TEST_ADMIN", password = "TEST_ADMIN", roles = {"ADMIN"})
    public void submitUserTest() throws Exception {
        passwordEncoder = new BCryptPasswordEncoder();
        String uri = "http://localhost:8080/userManagement/user";
        User user = new User();
        user.setName("TEST_MANAGER");
        user.setPassword("TEST_MANAGER");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_MANAGER"));
        user.setRoles(roles);
        String encryptedPassword =  passwordEncoder.encode("TEST_MANAGER");
        user.setPassword(encryptedPassword);
        user.setEmailAddress("test@test.com");
        String inputJson = super.mapToJson(user);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @WithMockUser(username = "TEST_ADMIN", password = "TEST_ADMIN", roles = {"TESTER"})
    public void submitUserNegativeTest() throws Exception {
        passwordEncoder = new BCryptPasswordEncoder();
        String uri = "http://localhost:8080/userManagement/user";
        User user = new User();
        user.setName("TEST_MANAGER");
        user.setPassword("TEST_MANAGER");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_MANAGER"));
        user.setRoles(roles);
        String encryptedPassword =  passwordEncoder.encode("TEST_MANAGER");
        user.setPassword(encryptedPassword);
        user.setEmailAddress("test@test.com");
        String inputJson = super.mapToJson(user);
        Assert.assertThrows(NestedServletException.class, () -> {
                    MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
                    System.out.println(mvcResult.getResponse().getContentAsString());
                    int status = mvcResult.getResponse().getStatus();
                });

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @WithMockUser(username = "TEST_MANAGER", password = "TEST_MANAGER", roles = {"MANAGER"})
    public void submitJobTest() throws Exception {
        passwordEncoder = new BCryptPasswordEncoder();
        String uri = "http://localhost:8080/jobManagement/job";
        Job job = new Job();
        job.setName("TEST_JOB");
        job.setDescription("TESTING JOB");
        String inputJson = super.mapToJson(job);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    @WithMockUser(username = "TEST_MANAGER", password = "TEST_MANAGER", roles = {"MANAGER"})
    public void submitJobRequestTest() throws Exception {
        passwordEncoder = new BCryptPasswordEncoder();
        String uri = "http://localhost:8080/jobRequest/jobRequest";
        JobRequest jobRequest = new JobRequest();
        jobRequest.setJobId(1L);
        jobRequest.setUserId(2L);
        jobRequest.setDuration(10L);
        String inputJson = super.mapToJson(jobRequest);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @Order(6)
    @Rollback(value = false)
    @WithMockUser(username = "TEST_MANAGER", password = "TEST_MANAGER", roles = {"MANAGER"})
    public void submitJobRequestNegativeTest() throws Exception {
        passwordEncoder = new BCryptPasswordEncoder();
        String uri = "http://localhost:8080/jobRequest/jobRequest";
        JobRequest jobRequest = new JobRequest();
        jobRequest.setJobId(1L);
        jobRequest.setUserId(2L);
        String inputJson = super.mapToJson(jobRequest);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }
}
