package com.mysite.sbb.member.dto;

import com.mysite.sbb.member.constant.Department;
import com.mysite.sbb.member.constant.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    @NotEmpty(message = "사용자 ID는 필수 항목입니다.")
    @Size(min = 3, max = 30, message = "사용자 ID는 3~30자로 입력하세요")
    private String username;        // 사용자 id

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 4, message = "비밀번호는 4자 이상이어야 합니다.")
    private String password1;        // 비밀번호

    @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
    private String password2;        // 확인용 비밀번호

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;           // 이메일

    @NotNull(message = "성별을 선택하세요")
    private Gender gender;          // 성별

    @NotNull(message = "학과를 선택하세요")
    private Department department;      // 학과

    @AssertTrue(message = "등록(이용) 확인에 동의해야 가입할 수 있습니다.")
    private Boolean registration;   // 등록 여부
}
