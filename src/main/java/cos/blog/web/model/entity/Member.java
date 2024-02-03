package cos.blog.web.model.entity;

import cos.blog.web.dto.MemberFormDto;
import cos.blog.web.dto.UpdateMemberDto;
import cos.blog.web.model.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.sql.rowset.spi.SyncResolver;
import java.time.LocalDateTime;

@ToString(exclude = {"createdTime"})
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Member extends BaseEntity{

    public Member(String account, String name, String password, String email) {
        this.account = account;
        this.name = name;
        this.password = password;
        this.email = email;
        addDefaultRole();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    //Member, MANAGER, ADMIN
    private RoleType role;


    public void updateMember(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    private void addDefaultRole() {
        this.role = RoleType.ROLE_MEMBER;
    }

    public static Member of(String account, String name, String password, String email) {
        return new Member(account, name, password, email);
    }

    public void editMember(UpdateMemberDto updateForm) {
        this.account = updateForm.getAccount();
        this.name = updateForm.getName();
        this.password = updateForm.getPassword();
        this.email = updateForm.getEmail();
    }

    public void setEncodedPw(String encoded) {
        this.password = encoded;
    }

}
