package com.mysite.career.review.resume.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;
    private String startDate; // YYYY-MM
    private String endDate;   // YYYY-MM

    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String gitUrl;
    private String demoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;
}
