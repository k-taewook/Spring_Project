package com.mysite.career.review.resume.service;

import com.mysite.career.review.user.entity.User;
import com.mysite.career.review.resume.constant.ResumeStatus;
import com.mysite.career.review.resume.dto.ResumeDto;
import com.mysite.career.review.resume.entity.File;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private void validateFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isEmpty()) {
            String extension = "";
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex >= 0) {
                extension = fileName.substring(dotIndex + 1).toLowerCase();
            }
            
            List<String> allowedExtensions = Arrays.asList("zip", "pdf", "docx", "hwp");
            if (!allowedExtensions.contains(extension)) {
                throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. (.zip, .pdf, .docx, .hwp 만 가능)");
            }
        }
    }

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

    public void create(ResumeDto resumeDto, User user) throws IOException {
        File file = null;

        if (resumeDto.getResumeFile() != null && !resumeDto.getResumeFile().isEmpty()) {
            validateFileExtension(resumeDto.getResumeFile());
            
            file = File.builder()
                    .originalFileName(resumeDto.getResumeFile().getOriginalFilename())
                    .contentType(resumeDto.getResumeFile().getContentType())
                    .fileData(resumeDto.getResumeFile().getBytes())
                    .build();
        }

        Resume resume = Resume.builder()
                .content(resumeDto.getContent())
                .subject(resumeDto.getSubject())
                .targetCompany(resumeDto.getTargetCompany())
                .status(ResumeStatus.WAITING)
                .author(user)
                .file(file)
                .build();
        resumeRepository.save(resume);              // insert
    }

    public void modify(Resume resume, @Valid ResumeDto resumeDto) throws IOException {
        if (resumeDto.getResumeFile() != null && !resumeDto.getResumeFile().isEmpty()) {
            validateFileExtension(resumeDto.getResumeFile());

            File file = File.builder()
                    .originalFileName(resumeDto.getResumeFile().getOriginalFilename())
                    .contentType(resumeDto.getResumeFile().getContentType())
                    .fileData(resumeDto.getResumeFile().getBytes())
                    .build();
            
            resume.setFile(file);
        }

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
