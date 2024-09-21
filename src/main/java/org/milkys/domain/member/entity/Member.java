package org.milkys.domain.member.entity;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.common.entity.BaseEntity;
import org.milkys.domain.member.dto.LoginDto;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_code")
    private Long memberCode;
    /*멤버식별코드*/

    @Column(name = "mem_id")
    private String memberId;

    @Column(name = "mem_pw")
    private String memberPw;

    @Column(name = "mem_name")
    private String memberName;

    @Column(name = "mem_nick")
    private String memberNickname;

    @Column(name = "mem_birth")
    private String memberBirthday;

    @Column(name = "mem_auth")
    @Enumerated(EnumType.STRING)
    private MilkysEnum.MemberRoleType memberAuth;


    public LoginDto bringMemberInfo(){
        return LoginDto.builder()
                .memberCode(memberCode)
                .memberId(memberId)
                .memberPw(memberPw)
                .memberName(memberName)
                .memberNickname(memberNickname)
                .memberBirthday(memberBirthday)
                .memberAuth(memberAuth)
                .build();
    }


}
