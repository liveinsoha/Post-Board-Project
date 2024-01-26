package cos.blog.model.entity;

import cos.blog.dto.JoinFormDto;
import cos.blog.model.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@ToString(exclude = {"createdDate"})
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Member {

    public Member(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        addDefaultRole();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Member_ID")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    //Member, MANAGER, ADMIN
    private RoleType role;

    @CreatedDate
    LocalDateTime createdDate;

    public void updateMember(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    private void addDefaultRole() {
        this.role = RoleType.ROLE_MEMBER;
    }

    public static Member from(JoinFormDto joinFormDto) {
        return new Member(joinFormDto.getName(), joinFormDto.getPassword(), joinFormDto.getEmail());
    }
}
