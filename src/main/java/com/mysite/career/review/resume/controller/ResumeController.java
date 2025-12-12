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
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriUtils;
import java.nio.charset.StandardCharsets;
import java.net.MalformedURLException;
import com.mysite.career.review.resume.entity.File;
import java.util.List;
import java.util.ArrayList;

import com.mysite.career.review.resume.dto.ResumeSelfIntroDto;
import com.mysite.career.review.resume.dto.ResumeCareerDto;
import com.mysite.career.review.resume.dto.ResumeEducationDto;
import com.mysite.career.review.resume.dto.ResumeProjectDto;
import com.mysite.career.review.resume.dto.ResumeSkillDto;
import com.mysite.career.review.resume.entity.ResumeSelfIntro;
import com.mysite.career.review.resume.constant.ResumeVisibility;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/resume")
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "mode", defaultValue = "version") String mode, Principal principal) {
        Resume resume = resumeService.getResume(id);

        if(!resume.getAuthor().getUsername().equals(principal.getName()) && !principal.getName().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        if ("all".equals(mode)) {
            resumeService.deleteAllVersions(resume);
        } else {
            resumeService.delete(resume);
        }

        return "redirect:/resume/list";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @Valid @ModelAttribute ResumeDto resumeDto, BindingResult bindingResult, Principal principal) throws IOException {

        if(bindingResult.hasErrors()) {
            Resume resume = resumeService.getResume(id);
            if (resume.getFile() != null) {
                resumeDto.setOriginalFileName(resume.getFile().getOriginalFileName());
            }
            resumeDto.setId(resume.getId());
            return "resume/inputForm";
        }

        Resume resume = resumeService.getResume(id);

        if(!resume.getAuthor().getUsername().equals(principal.getName()) && !principal.getName().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        Resume newResume = resumeService.modify(resume, resumeDto);

        // 수정 후에는 새로운 버전의 상세 페이지로 이동
        return "redirect:/resume/detail/" + newResume.getId();

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, ResumeDto resumeDto, Principal principal) {
        Resume resume = resumeService.getResume(id);

        if(!resume.getAuthor().getUsername().equals(principal.getName()) && !principal.getName().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        resumeDto.setSubject(resume.getSubject());
        resumeDto.setContent(resume.getContent());
        resumeDto.setTargetCompany(resume.getTargetCompany());
        resumeDto.setStatus(resume.getStatus());
        resumeDto.setVisibility(resume.getVisibility());
        
        // 자소서 항목 DTO 변환
        if (resume.getSelfIntroList() != null) {
            List<ResumeSelfIntroDto> selfIntroDtos = resume.getSelfIntroList().stream()
                    .map(intro -> new ResumeSelfIntroDto(intro.getQuestion(), intro.getAnswer()))
                    .collect(Collectors.toList());
            resumeDto.setSelfIntroList(selfIntroDtos);
        }

        // 경력 DTO 변환
        if (resume.getCareerList() != null) {
            List<ResumeCareerDto> careerDtos = resume.getCareerList().stream()
                    .map(career -> ResumeCareerDto.builder()
                            .companyName(career.getCompanyName())
                            .department(career.getDepartment())
                            .position(career.getPosition())
                            .startDate(career.getStartDate())
                            .endDate(career.getEndDate())
                            .description(career.getDescription())
                            .build())
                    .collect(Collectors.toList());
            resumeDto.setCareerList(careerDtos);
        }

        // 학력 DTO 변환
        if (resume.getEducationList() != null) {
            List<ResumeEducationDto> educationDtos = resume.getEducationList().stream()
                    .map(edu -> ResumeEducationDto.builder()
                            .schoolName(edu.getSchoolName())
                            .major(edu.getMajor())
                            .startDate(edu.getStartDate())
                            .endDate(edu.getEndDate())
                            .status(edu.getStatus())
                            .build())
                    .collect(Collectors.toList());
            resumeDto.setEducationList(educationDtos);
        }

        // 프로젝트 DTO 변환
        if (resume.getProjectList() != null) {
            List<ResumeProjectDto> projectDtos = resume.getProjectList().stream()
                    .map(project -> ResumeProjectDto.builder()
                            .projectName(project.getProjectName())
                            .startDate(project.getStartDate())
                            .endDate(project.getEndDate())
                            .description(project.getDescription())
                            .gitUrl(project.getGitUrl())
                            .demoUrl(project.getDemoUrl())
                            .build())
                    .collect(Collectors.toList());
            resumeDto.setProjectList(projectDtos);
        }

        // 스킬 DTO 변환
        if (resume.getSkillList() != null) {
            List<ResumeSkillDto> skillDtos = resume.getSkillList().stream()
                    .map(skill -> ResumeSkillDto.builder()
                            .skillName(skill.getSkillName())
                            .level(skill.getLevel())
                            .build())
                    .collect(Collectors.toList());
            resumeDto.setSkillList(skillDtos);
        }

        if (resume.getFile() != null) {
            resumeDto.setOriginalFileName(resume.getFile().getOriginalFileName());
        }
        resumeDto.setId(resume.getId());

        return "resume/inputForm";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
        Resume resume = resumeService.getResume(id);
        
        if (resume.getFile() == null) {
            return ResponseEntity.notFound().build();
        }
        
        File file = resume.getFile();
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        String encodedUploadFileName = UriUtils.encode(file.getOriginalFileName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                .body(resource);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> view(@PathVariable("id") Long id) {
        Resume resume = resumeService.getResume(id);

        if (resume.getFile() == null) {
            return ResponseEntity.notFound().build();
        }

        File file = resume.getFile();
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        String encodedUploadFileName = UriUtils.encode(file.getOriginalFileName(), StandardCharsets.UTF_8);
        String contentDisposition = "inline; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                .body(resource);
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
        
        // 이력서 버전 히스토리 조회
        List<Resume> history = new ArrayList<>();
        if (resume.getApplication() != null) {
            history = resume.getApplication().getResumes();
        }

        model.addAttribute("resume", resume);
        model.addAttribute("history", history);
        model.addAttribute("feedbackDto", feedbackDto);
        // log.info("==============> resume : {}", resume);
        return "resume/detail";
    }

    @GetMapping("/create")
    public String createQuestion(ResumeDto resumeDto, Model model){
        model.addAttribute("resumeDto", resumeDto);
        return "resume/inputForm";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute ResumeDto resumeDto, BindingResult bindingResult, Principal principal) throws IOException {
        // log.info("==============> {}", resumeDto);
        if (resumeDto.getResumeFile() != null) {
            log.info("==============> File Name: {}, Size: {}", resumeDto.getResumeFile().getOriginalFilename(), resumeDto.getResumeFile().getSize());
        } else {
            log.info("==============> File is NULL");
        }

        if(bindingResult.hasErrors()){
            return "resume/inputForm";
        }

        log.info("==============> principal : {}", principal.getName());

        User user = userService.getUser(principal.getName());

        resumeService.create(resumeDto, user);

        return "redirect:/resume/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public String myResumes(Model model, @RequestParam(value = "page", defaultValue = "0") int page, Principal principal) {
        User user = userService.getUser(principal.getName());
        Page<Resume> paging = resumeService.getMyResumes(page, user);
        model.addAttribute("paging", paging);
        return "resume/my_list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/visibility/{id}")
    public String updateVisibility(@PathVariable("id") Long id, @RequestParam("visibility") ResumeVisibility visibility, Principal principal) {
        Resume resume = resumeService.getResume(id);
        if(!resume.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        resumeService.updateVisibility(id, visibility);
        return "redirect:/resume/detail/" + id;
    }
}
