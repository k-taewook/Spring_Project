package com.mysite.career.review.resume.service;

import com.mysite.career.review.user.entity.User;
import com.mysite.career.review.resume.constant.ResumeStatus;
import com.mysite.career.review.resume.dto.ResumeDto;
import com.mysite.career.review.resume.entity.Resume;
import com.mysite.career.review.resume.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public Page<Resume> getList(int page, String keyword) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("created"));
        Pageable pageable = PageRequest.of(page, 10,  Sort.by(sorts));

        Page<Resume> paging = resumeRepository.findAllByKeyword(keyword, pageable);

        return paging;
    }

    public Resume getResume(Long id) {
        Resume resume = resumeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 이력서가 존재하지 않습니다."));
        return resume;
    }

    public void create(ResumeDto resumeDto, User user) {
        Resume resume = Resume.builder()
                .content(resumeDto.getContent())
                .subject(resumeDto.getSubject())
                .targetCompany(resumeDto.getTargetCompany())
                .status(ResumeStatus.WAITING)
                .author(user)
                .build();
        resumeRepository.save(resume);              // insert
    }

    public void modify(Resume resume, @Valid ResumeDto resumeDto) {
        resume.setSubject(resumeDto.getSubject());
        resume.setContent(resumeDto.getContent());
        resume.setTargetCompany(resumeDto.getTargetCompany());
        resume.setStatus(resumeDto.getStatus());
        resumeRepository.save(resume);              // update
    }

    public void delete(Resume resume) {
        resumeRepository.delete(resume);
    }
}
