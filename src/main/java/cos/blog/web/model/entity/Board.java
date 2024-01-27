package cos.blog.web.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = {"replies", "createdDate", "member"})
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    public Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    Long id;

    String title;

    @Lob
    String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Member_ID")
    Member member;

    @OneToMany(mappedBy = "board")
    List<Reply> replies = new ArrayList<>();

    @CreatedDate
    LocalDateTime createdDate;
}
