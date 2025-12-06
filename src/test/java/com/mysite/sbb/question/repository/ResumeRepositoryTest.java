package com.mysite.sbb.question.repository;

import com.mysite.career.review.resume.entity.Resume;
import com.mysite.career.review.resume.repository.ResumeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResumeRepositoryTest {
    @Autowired
    private ResumeRepository resumeRepository;



    @Test
    public void testSave(){
        Resume q1 = new Resume();
        q1.setSubject("sbb가 뭔가요?");
        q1.setContent("sbb는 질의 응답 게시판 인가요?");
        System.out.println("q1 : " + q1);
        Resume q2 = resumeRepository.save(q1);
        System.out.println("q2 : " + q2);

        Resume q3 = Resume.builder()
                .content("질문 내용 입력입니다.")
                .subject("질문 제목")
                .build();
        Resume q4 = resumeRepository.save(q3);
        assertEquals(2, q4.getId());
    }

    @Test
    public void testFindAll(){
        List<Resume> resumeList = resumeRepository.findAll();
        assertEquals(2, resumeList.size());
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