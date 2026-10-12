package com.mysterybox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysterybox.dto.LoginRequest;
import com.mysterybox.dto.RegisterRequest;
import com.mysterybox.dto.SubmitAnswerRequest;
import com.mysterybox.entity.MysteryCase;
import com.mysterybox.repository.MysteryCaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MysteryCaseRepository caseRepository;

    private String userToken;
    private String adminToken;
    private Long sampleCaseId;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Authenticate default admin
        LoginRequest adminLogin = new LoginRequest("admin@mysterybox.com", "Admin@123");
        MvcResult adminResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn();

        String adminResponseStr = adminResult.getResponse().getContentAsString();
        adminToken = objectMapper.readTree(adminResponseStr).get("token").asText();

        // 2. Register / login investigator user
        RegisterRequest userRegister = new RegisterRequest("Detective Holmes", "holmes@test.com", "Secret@123", "USER");
        // May already exist in repeated runs, so attempt register or fallback to login
        try {
            mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRegister)));
        } catch (Exception ignored) {}

        LoginRequest userLogin = new LoginRequest("holmes@test.com", "Secret@123");
        MvcResult userResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLogin)))
                .andExpect(status().isOk())
                .andReturn();

        String userResponseStr = userResult.getResponse().getContentAsString();
        userToken = objectMapper.readTree(userResponseStr).get("token").asText();

        // Find sample case
        List<MysteryCase> cases = caseRepository.findAll();
        if (!cases.isEmpty()) {
            sampleCaseId = cases.get(0).getId();
        }
    }

    @Test
    void testPublicCasesApiDoesNotExposeCorrectAnswer() throws Exception {
        mockMvc.perform(get("/api/cases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].correctAnswer").doesNotExist())
                .andExpect(jsonPath("$[0].solution").doesNotExist());
    }

    @Test
    void testCaseDetailsDoesNotExposeCorrectAnswer() throws Exception {
        mockMvc.perform(get("/api/cases/" + sampleCaseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleCaseId))
                .andExpect(jsonPath("$.clues", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.evidence", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.correctAnswer").doesNotExist())
                .andExpect(jsonPath("$.solution").doesNotExist());
    }

    @Test
    void testAnswerSubmissionAndDuplicateProtection() throws Exception {
        // Incorrect answer first
        SubmitAnswerRequest wrongAnswer = new SubmitAnswerRequest("Incorrect Suspect");
        mockMvc.perform(post("/api/cases/" + sampleCaseId + "/submit")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongAnswer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(false))
                .andExpect(jsonPath("$.scoreAwarded").value(0));

        // Correct answer ("Charles Thornwood" for Case 1)
        SubmitAnswerRequest correctAnswer = new SubmitAnswerRequest("Charles Thornwood");
        mockMvc.perform(post("/api/cases/" + sampleCaseId + "/submit")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(correctAnswer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true));

        // Submit again - must prevent duplicate score award!
        mockMvc.perform(post("/api/cases/" + sampleCaseId + "/submit")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(correctAnswer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alreadySolved").value(true))
                .andExpect(jsonPath("$.scoreAwarded").value(0));
    }

    @Test
    void testAdminAuthorizationProtection() throws Exception {
        // Regular user should get 403 Forbidden
        mockMvc.perform(get("/api/admin/cases")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        // Admin user should get 200 OK with solutions
        mockMvc.perform(get("/api/admin/cases")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].correctAnswer").exists());
    }

    @Test
    void testUserProgressApi() throws Exception {
        mockMvc.perform(get("/api/progress")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalScore").isNumber())
                .andExpect(jsonPath("$.detectiveRank").isString());
    }
}
