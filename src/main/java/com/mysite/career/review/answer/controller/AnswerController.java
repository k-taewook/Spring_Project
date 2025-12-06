package com.mysite.career.review.answer.controller;

import com.mysite.career.review.answer.dto.AnswerDto;
import com.mysite.career.review.answer.entity.Answer;
import com.mysite.career.review.answer.service.AnswerService;
import com.mysite.career.review.member.entity.Member;
import com.mysite.career.review.member.service.MemberService;
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
@RequestMapping("/answer")
@Slf4j
@RequiredArgsConstructor
public class AnswerController {

    private final ResumeService resumeService;

    private final AnswerService answerService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String modify(@PathVariable("id") Long id, Principal principal) {

        Answer answer = answerService.getAnswer(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        answerService.delete(answer);

        return "redirect:/question/detail/" + answer.getResume().getId();

    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @Valid AnswerDto answerDto, BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            return "answer/inputForm";
        }

        Answer answer = answerService.getAnswer(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        answerService.modify(answer, answerDto);

        return "redirect:/question/detail/" + answer.getResume().getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @ModelAttribute AnswerDto answerDto, Principal principal) {
        Answer answer = answerService.getAnswer(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        answerDto.setContent(answer.getContent());

        return "answer/inputForm";
    }


    @PostMapping("/create/{id}")
    public String create(@PathVariable("id") Long id, @Valid AnswerDto answerDto, BindingResult bindingResult, Principal principal, Model model) {

        Resume resume = resumeService.getQuestion(id);

        if(bindingResult.hasErrors()) {
            model.addAttribute("question", resume);
            model.addAttribute("answerDto", answerDto);
            return "Detail";
        }

        Member member = memberService.getMember(principal.getName());

        answerService.create(resume, answerDto, member);

        return "redirect:/question/detail/" + id;
    }
}