package tech.rent.be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.rent.be.dto.EmailDetail;
import tech.rent.be.services.EmailService;
@RequestMapping("/api/testEmail")
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class TestEmailController {
    private final EmailService emailService;
    @GetMapping
    public void testEmail(){
        emailService.testEmail("damchanphong@gmail.com");
    }

}
