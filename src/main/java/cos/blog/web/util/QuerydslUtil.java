package cos.blog.web.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.QBoard;
import cos.blog.web.model.entity.QMember;
import cos.blog.web.model.entity.QReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
public class QuerydslUtil {

    private static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

    public static List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                log.info("정렬 기준 고른다");
                log.info("direction = {}", direction);
                switch (order.getProperty()) {
                    case "board_createdTime":
                        log.info("정렬 기준 Board.createdTime");
                        OrderSpecifier<?> orderBoardByCreatedTime = QuerydslUtil.getSortedColumn(direction, QBoard.board, "createdTime");
                        ORDERS.add(orderBoardByCreatedTime);
                        break;
                    case "reply_createdTime":
                        log.info("정렬 기준 Reply.createdTime");
                        OrderSpecifier<?> orderReplyByCreatedTime = QuerydslUtil.getSortedColumn(direction, QReply.reply, "createdTime");
                        ORDERS.add(orderReplyByCreatedTime);
                        break;
                    case "member":
                        log.info("정렬 기준 Member.name");
                        OrderSpecifier<?> orderMemberName = QuerydslUtil.getSortedColumn(direction, QMember.member, "name");
                        ORDERS.add(orderMemberName);
                        //정렬기준 member클래스의 name 필드?
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }
}
