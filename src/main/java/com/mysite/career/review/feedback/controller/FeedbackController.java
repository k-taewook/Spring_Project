package com.mysite.career.review.feedback.controller;

import com.mysite.career.review.feedback.dto.FeedbackDto;
import com.mysite.career.review.feedback.entity.Feedback;
import com.mysite.career.review.feedback.service.FeedbackService;
import com.mysite.career.review.user.entity.User;
import com.mysite.career.review.user.service.UserService;
import com.mysite.career.review.resume.entity.Resume;
import com.mysite.career.review.resume.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/feedback")
@Slf4j
@RequiredArgsConstructor
public class FeedbackController {

    private final ResumeService resumeService;

    private final FeedbackService feedbackService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {

        Feedback feedback = feedbackService.getFeedback(id);

        if(!feedback.getAuthor().getUsername().equals(principal.getName()) && !principal.getName().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        feedbackService.delete(feedback);

        return "redirect:/resume/detail/" + feedback.getResume().getId();

    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @Valid FeedbackDto feedbackDto, BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            return "feedback/inputForm";
        }

        Feedback feedback = feedbackService.getFeedback(id);

        if(!feedback.getAuthor().getUsername().equals(principal.getName()) && !principal.getName().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        feedbackService.modify(feedback, feedbackDto);

        return "redirect:/resume/detail/" + feedback.getResume().getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @ModelAttribute FeedbackDto feedbackDto, Principal principal) {
        Feedback feedback = feedbackService.getFeedback(id);

        if(!feedback.getAuthor().getUsername().equals(principal.getName()) && !principal.getName().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        feedbackDto.setContent(feedback.getContent());
        feedbackDto.setRating(feedback.getRating());

        return "feedback/inputForm";
    }


    @PostMapping("/create/{id}")
    public String create(@PathVariable("id") Long id, @Valid FeedbackDto feedbackDto, BindingResult bindingResult, Principal principal, Model model) {

        Resume resume = resumeService.getResume(id);

        if(bindingResult.hasErrors()) {
            model.addAttribute("resume", resume);
            model.addAttribute("feedbackDto", feedbackDto);
            return "resume/detail";
        }

        User user = userService.getUser(principal.getName());

        feedbackService.create(resume, feedbackDto, user);

        return "redirect:/resume/detail/" + id;
    }
}