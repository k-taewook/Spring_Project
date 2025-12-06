package com.mysite.career.review.resume.controller;

import com.mysite.career.review.answer.dto.AnswerDto;
import com.mysite.career.review.member.entity.Member;
import com.mysite.career.review.member.service.MemberService;
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
@RequestMapping(value = "/question")
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String modify(@PathVariable("id") Long id, Principal principal) {
        Resume resume = resumeService.getQuestion(id);

        if(!resume.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        resumeService.delete(resume);

        return "redirect:/question/lsit";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @Valid ResumeDto resumeDto, BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            return "question/inputForm";
        }

        Resume resume = resumeService.getQuestion(id);

        if(!resume.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        resumeService.modify(resume, resumeDto);

        return "redirect:/question/detail/" + id;

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, ResumeDto resumeDto, Principal principal) {
        Resume resume = resumeService.getQuestion(id);

        if(!resume.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        resumeDto.setSubject(resume.getSubject());
        resumeDto.setContent(resume.getContent());

        return "question/inputForm";
    }


    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword){

        log.info("==============> page: {}, keyword : {}", page, keyword);

        Page<Resume> paging = resumeService.getList(page, keyword);
        model.addAttribute("paging", paging);
        return "question/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, AnswerDto answerDto){
        log.info("==============> id : {}", id);
        Resume resume = resumeService.getQuestion(id);
        model.addAttribute("question", resume);
        model.addAttribute("answerDto", answerDto);
        log.info("==============> question : {}", resume);
        return "question/detail";
    }

    @GetMapping("/create")
    public String createQuestion(ResumeDto resumeDto, Model model){
        model.addAttribute("questionDto", resumeDto);
        return "question/inputForm";
    }

    @PostMapping("/create")
    public String create(@Valid ResumeDto resumeDto, BindingResult bindingResult, Principal principal){
        log.info("==============> {}", resumeDto);

        if(bindingResult.hasErrors()){
            return "question/inputForm";
        }

        log.info("==============> principal : {}", principal.getName());

        Member member = memberService.getMember(principal.getName());

        resumeService.create(resumeDto, member);

        return "redirect:/question/list";
    }
}
