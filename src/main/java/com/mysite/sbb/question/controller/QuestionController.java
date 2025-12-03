package com.mysite.sbb.question.controller;

import com.mysite.sbb.answer.dto.AnswerDto;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.service.MemberService;
import com.mysite.sbb.question.dto.QuestionDto;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.service.QuestionService;
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
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String modify(@PathVariable("id") Long id, Principal principal) {
        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        questionService.delete(question);

        return "redirect:/question/lsit";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @Valid QuestionDto questionDto, BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            return "question/inputForm";
        }

        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        questionService.modify(question, questionDto);

        return "redirect:/question/detail/" + id;

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, QuestionDto questionDto, Principal principal) {
        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        questionDto.setSubject(question.getSubject());
        questionDto.setContent(question.getContent());

        return "question/inputForm";
    }


    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword){

        log.info("==============> page: {}, keyword : {}", page, keyword);

        Page<Question> paging = questionService.getList(page, keyword);
        model.addAttribute("paging", paging);
        return "question/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, AnswerDto answerDto){
        log.info("==============> id : {}", id);
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        model.addAttribute("answerDto", answerDto);
        log.info("==============> question : {}", question);
        return "question/detail";
    }

    @GetMapping("/create")
    public String createQuestion(QuestionDto questionDto, Model model){
        model.addAttribute("questionDto", questionDto);
        return "question/inputForm";
    }

    @PostMapping("/create")
    public String create(@Valid QuestionDto questionDto, BindingResult bindingResult, Principal principal){
        log.info("==============> {}", questionDto);

        if(bindingResult.hasErrors()){
            return "question/inputForm";
        }

        log.info("==============> principal : {}", principal.getName());

        Member member = memberService.getMember(principal.getName());

        questionService.create(questionDto, member);

        return "redirect:/question/list";
    }
}
