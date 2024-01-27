package cos.blog.web.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.QBoard;
import cos.blog.web.model.entity.QMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

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
                switch (order.getProperty()) {
                    case "createdDate":
                        OrderSpecifier<?> orderDate = QuerydslUtil.getSortedColumn(direction, QBoard.board, "createdDate");
                        ORDERS.add(orderDate);
                        break;
                    case "member":
                        OrderSpecifier<?> orderWriter = QuerydslUtil.getSortedColumn(direction, QMember.member, "name");
                        ORDERS.add(orderWriter);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }
}
