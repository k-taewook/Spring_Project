package com.mysite.career.review.resume.service;

import com.mysite.career.review.resume.dto.*;
import com.mysite.career.review.resume.entity.*;
import com.mysite.career.review.user.entity.User;
import com.mysite.career.review.resume.constant.ResumeStatus;
import com.mysite.career.review.resume.constant.ResumeVisibility;
import com.mysite.career.review.resume.repository.ApplicationRepository;
import com.mysite.career.review.resume.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ApplicationRepository applicationRepository;

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

    private void mapResumeDetails(Resume resume, ResumeDto resumeDto) {
        // Career
        if (resumeDto.getCareerList() != null) {
            List<ResumeCareer> careerList = resumeDto.getCareerList().stream()
                .map(dto -> ResumeCareer.builder()
                    .companyName(dto.getCompanyName())
                    .department(dto.getDepartment())
                    .position(dto.getPosition())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .description(dto.getDescription())
                    .resume(resume)
                    .build())
                .collect(Collectors.toList());
            resume.setCareerList(careerList);
        }

        // Education
        if (resumeDto.getEducationList() != null) {
            List<ResumeEducation> educationList = resumeDto.getEducationList().stream()
                .map(dto -> ResumeEducation.builder()
                    .schoolName(dto.getSchoolName())
                    .major(dto.getMajor())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .status(dto.getStatus())
                    .resume(resume)
                    .build())
                .collect(Collectors.toList());
            resume.setEducationList(educationList);
        }

        // Project
        if (resumeDto.getProjectList() != null) {
            List<ResumeProject> projectList = resumeDto.getProjectList().stream()
                .map(dto -> ResumeProject.builder()
                    .projectName(dto.getProjectName())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .description(dto.getDescription())
                    .gitUrl(dto.getGitUrl())
                    .demoUrl(dto.getDemoUrl())
                    .resume(resume)
                    .build())
                .collect(Collectors.toList());
            resume.setProjectList(projectList);
        }

        // Skill
        if (resumeDto.getSkillList() != null) {
            List<ResumeSkill> skillList = resumeDto.getSkillList().stream()
                .map(dto -> ResumeSkill.builder()
                    .skillName(dto.getSkillName())
                    .level(dto.getLevel())
                    .resume(resume)
                    .build())
                .collect(Collectors.toList());
            resume.setSkillList(skillList);
        }

        // Self Intro
        if (resumeDto.getSelfIntroList() != null) {
            List<ResumeSelfIntro> selfIntroList = new ArrayList<>();
            for (ResumeSelfIntroDto introDto : resumeDto.getSelfIntroList()) {
                if (introDto.getQuestion() != null && !introDto.getQuestion().isEmpty()) {
                    ResumeSelfIntro selfIntro = ResumeSelfIntro.builder()
                            .question(introDto.getQuestion())
                            .answer(introDto.getAnswer())
                            .resume(resume)
                            .build();
                    selfIntroList.add(selfIntro);
                }
            }
            resume.setSelfIntroList(selfIntroList);
        }
    }

    @Transactional
    public void create(ResumeDto resumeDto, User user) throws IOException {
        File file = null;

        if (resumeDto.getResumeFile() != null && !resumeDto.getResumeFile().isEmpty()) {
            validateFileExtension(resumeDto.getResumeFile());
            
            file = File.builder()
                    .originalFileName(resumeDto.getResumeFile().getOriginalFilename())
                    .contentType(resumeDto.getResumeFile().getContentType())
                    .fileData(resumeDto.getResumeFile().getBytes())
                    .fileSize(resumeDto.getResumeFile().getSize())
                    .build();
        }

        // 1. Application 생성
        Application application = Application.builder()
                .targetCompany(resumeDto.getTargetCompany())
                .status(ResumeStatus.WAITING)
                .author(user)
                .build();
        applicationRepository.save(application);

        // 2. Resume (v1) 생성
        Resume resume = Resume.builder()
                .content(resumeDto.getContent())
                .subject(resumeDto.getSubject())
                .targetCompany(resumeDto.getTargetCompany())
                .status(ResumeStatus.WAITING)
                .author(user)
                .file(file)
                .application(application)
                .version(1)
                .commitMessage("Initial Commit")
                .visibility(resumeDto.getVisibility() != null ? resumeDto.getVisibility() : ResumeVisibility.PUBLIC)
                .build();

        mapResumeDetails(resume, resumeDto);

        resumeRepository.save(resume);              // insert
    }

    @Transactional
    public Resume modify(Resume oldResume, @Valid ResumeDto resumeDto) throws IOException {
        File file = null;

        // 파일 처리: 새 파일이 있으면 사용, 없으면 기존 파일 복사(참조)
        if (resumeDto.getResumeFile() != null && !resumeDto.getResumeFile().isEmpty()) {
            validateFileExtension(resumeDto.getResumeFile());

            file = File.builder()
                    .originalFileName(resumeDto.getResumeFile().getOriginalFilename())
                    .contentType(resumeDto.getResumeFile().getContentType())
                    .fileData(resumeDto.getResumeFile().getBytes())
                    .fileSize(resumeDto.getResumeFile().getSize())
                    .build();
        } else if (oldResume.getFile() != null) {
            // 기존 파일 정보를 그대로 사용하여 새 File 엔티티 생성
            File oldFile = oldResume.getFile();
            file = File.builder()
                    .originalFileName(oldFile.getOriginalFileName())
                    .contentType(oldFile.getContentType())
                    .fileData(oldFile.getFileData())
                    .fileSize(oldFile.getFileSize())
                    .build();
        }

        // 새 버전 생성 (기존 Resume 수정 아님)
        Application application = oldResume.getApplication();
        
        int maxVersion = 0;
        if (application != null && application.getResumes() != null) {
            maxVersion = application.getResumes().stream()
                    .mapToInt(r -> r.getVersion() == null ? 0 : r.getVersion())
                    .max()
                    .orElse(0);
        }
        int nextVersion = maxVersion + 1;

        Resume newResume = Resume.builder()
                .subject(resumeDto.getSubject())
                .content(resumeDto.getContent())
                .targetCompany(resumeDto.getTargetCompany())
                .status(resumeDto.getStatus())
                .author(oldResume.getAuthor())
                .file(file)
                .application(application)
                .version(nextVersion)
                .commitMessage(resumeDto.getCommitMessage())
                .visibility(resumeDto.getVisibility() != null ? resumeDto.getVisibility() : oldResume.getVisibility())
                .build();

        mapResumeDetails(newResume, resumeDto);

        return resumeRepository.save(newResume);
    }

    public Page<Resume> getMyResumes(int page, User user) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("created"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return resumeRepository.findLatestByAuthor(user, pageable);
    }

    @Transactional
    public void updateVisibility(Long id, ResumeVisibility visibility) {
        Resume resume = getResume(id);
        resume.setVisibility(visibility);
        resumeRepository.save(resume);
    }

    @Transactional
    public void delete(Resume resume) {
        resumeRepository.delete(resume);
    }

    @Transactional
    public void deleteAllVersions(Resume resume) {
        Application application = resume.getApplication();
        if (application != null) {
            applicationRepository.delete(application);
        } else {
            resumeRepository.delete(resume);
        }
    }
}
