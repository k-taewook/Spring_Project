package com.mysite.career.review.resume.controller;

import com.mysite.career.review.feedback.dto.FeedbackDto;
import com.mysite.career.review.user.entity.User;
import com.mysite.career.review.user.service.UserService;
import com.mysite.career.review.resume.dto.ResumeDto;
import com.mysite.career.review.resume.entity.Resume;
import com.mysite.career.review.resume.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/resume")
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String modify(@PathVariable("id") Long id, Principal principal) {
        Resume resume = resumeService.getResume(id);

        if(!resume.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        resumeService.delete(resume);

        return "redirect:/resume/list";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @Valid ResumeDto resumeDto, BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            return "resume/inputForm";
        }

        Resume resume = resumeService.getResume(id);

        if(!resume.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        resumeService.modify(resume, resumeDto);

        return "redirect:/resume/detail/" + id;

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, ResumeDto resumeDto, Principal principal) {
        Resume resume = resumeService.getResume(id);

        if(!resume.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        resumeDto.setSubject(resume.getSubject());
        resumeDto.setContent(resume.getContent());
        resumeDto.setTargetCompany(resume.getTargetCompany());
        resumeDto.setStatus(resume.getStatus());

        return "resume/inputForm";
    }


    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword){

        log.info("==============> page: {}, keyword : {}", page, keyword);

        Page<Resume> paging = resumeService.getList(page, keyword);
        model.addAttribute("paging", paging);
        return "resume/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, FeedbackDto feedbackDto){
        log.info("==============> id : {}", id);
        Resume resume = resumeService.getResume(id);
        model.addAttribute("resume", resume);
        model.addAttribute("feedbackDto", feedbackDto);
        log.info("==============> resume : {}", resume);
        return "resume/detail";
    }

    @GetMapping("/create")
    public String createQuestion(ResumeDto resumeDto, Model model){
        model.addAttribute("resumeDto", resumeDto);
        return "resume/inputForm";
    }

    @PostMapping("/create")
    public String create(@Valid ResumeDto resumeDto, BindingResult bindingResult, Principal principal){
        log.info("==============> {}", resumeDto);

        if(bindingResult.hasErrors()){
            return "resume/inputForm";
        }

        log.info("==============> principal : {}", principal.getName());

        User user = userService.getUser(principal.getName());

        resumeService.create(resumeDto, user);

        return "redirect:/resume/list";
    }
}
