package com.mysite.sbb.question.repository;

import com.mysite.resume_clinic.question.entity.Question;
import com.mysite.resume_clinic.question.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;



    @Test
    public void testSave(){
        Question q1 = new Question();
        q1.setSubject("sbb가 뭔가요?");
        q1.setContent("sbb는 질의 응답 게시판 인가요?");
        System.out.println("q1 : " + q1);
        Question q2 = questionRepository.save(q1);
        System.out.println("q2 : " + q2);

        Question q3 = Question.builder()
                .content("질문 내용 입력입니다.")
                .subject("질문 제목")
                .build();
        Question q4 = questionRepository.save(q3);
        assertEquals(2, q4.getId());
    }

    @Test
    public void testFindAll(){
        List<Question> questionList = questionRepository.findAll();
        assertEquals(2, questionList.size());
    }

    @Test
    public void testFindEntity(){

//        Question q1 =  questionRepository.findBySubjectLike("%sbb%").orElseThrow(EntityNotFoundException::new);
//        assertEquals("sbb가 뭔가요?", q1.getSubject());



//        Question q1 = questionRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("해당 Entity 존재 X"));
//        assertEquals("sbb가 뭔가요?", q1.getSubject());
//
//        Optional<Question> q = questionRepository.findById(3L);
//        if(q.isPresent()){
//            Question question = q.get();
//            assertEquals("sbb가 뭔가요?", question.getSubject());
//        } else {
//            throw new EntityNotFoundException("해당 Entity 존재 X");
//        }
    }
}