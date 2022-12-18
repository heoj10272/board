package com.heoj10272.security;


import com.heoj10272.entity.MemberRole;
import com.heoj10272.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.heoj10272.entity.Member;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {

        //1 - 80까지는 USER만 지정
        //81- 90까지는 USER,MANAGER
        //91- 100까지는 USER,MANAGER,ADMIN

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member clubMember = Member.builder()
                    .email("user"+i+"@sk.com")
                    .name("사용자"+i)
                    .fromSocial(false)
                    .roleSet(new HashSet<MemberRole>())
                    .password(  passwordEncoder.encode("1111") )
                    .build();

            //default role
            clubMember.addMemberRole(MemberRole.USER);

            if(i > 80){
                clubMember.addMemberRole(MemberRole.MANAGER);
            }

            if(i > 90){
                clubMember.addMemberRole(MemberRole.ADMIN);
            }

            repository.save(clubMember);

        });

    }

    @Test
    public void testRead() {

        Optional<Member> result = repository.findByEmail("user95@sk.com", false);

        Member Member = result.get();

        System.out.println(Member);

    }
}
