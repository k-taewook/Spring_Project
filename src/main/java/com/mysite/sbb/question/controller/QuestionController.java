package com.mysite.sbb.question.controller;

import com.mysite.sbb.question.dto.QuestionDto;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.repository.QuestionRepository;
import com.mysite.sbb.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/question")
@Slf4j
public class QuestionController {

    private final QuestionService questionService;



    @GetMapping("/list")
    public String list(Model model){
        List<Question> questionList = questionService.getList();
//        log.info("==============> list: {}", questionList);
        model.addAttribute("questionList", questionList);
        return "question/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        log.info("==============> id : {}", id);
        Question question = questionService.getQuestion(id);
         model.addAttribute("question", question);
         log.info("==============> question : {}", question);
        return "question/detail";
    }

    @GetMapping("/create")
    public String createQuestion(){
        return "question/inputForm";
    }

    @PostMapping("/create")
    public String create(QuestionDto questionDto){
        log.info("==============> {}", questionDto);

        return "redirect:/question/list";
    }
}
