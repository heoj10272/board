package com.heoj10272.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity {

    @Id
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();



//    @ElementCollection(fetch = FetchType.LAZY)
//    private Set<MemberRole> roleSet;

    public void addMemberRole(MemberRole memberRole){
        roleSet.add(memberRole);
    }
}
