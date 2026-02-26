package com.Khue.InventoryMgtSystem.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.Khue.InventoryMgtSystem.models.Transaction;


/*
    ByFilter tìm Keyword trong
    Các ENTITY và ByMonthAndYear
*/
public class TransactionFilter {
    /*
     * byFilter Function giống như SQL dưới ==>
     * WHERE
     * description LIKE '%abc%'
     * OR note LIKE '%abc%'
     * OR status LIKE '%abc%'
     * OR user.name LIKE '%abc%'
     * OR supplier.name LIKE '%abc%'
     * OR product.name LIKE '%abc%'
     * OR category.name LIKE '%abc%'
     */
    public static Specification<Transaction> byFilter(String searchvalue) {

        return ((root, query, criteriaBuilder) -> {
            if (searchvalue == null || searchvalue.isEmpty()) {
                return criteriaBuilder.conjunction(); // if searchValue is null , return all transaction
            }

            String searchPattern = "%" + searchvalue.toLowerCase() + "%"; // %iphone% ==> tìm các result liên quan đến
                                                                          // iphone

            // create a list to hold my predicates(điều kiện)
            List<Predicate> predicates = new ArrayList<>();

            // search within transaction fields
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchvalue));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("note")), searchvalue));
            predicates
                    .add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), searchvalue));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("transactiontype").as(String.class)),
                    searchvalue));

            // Safely join and check user fields using LEFT JOIN
            if (root.getJoins().stream().noneMatch(j -> j.getAttribute().getName().equals("user"))) {
                root.join("user", JoinType.LEFT);
            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("user", JoinType.LEFT).get("name")),
                    searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("user", JoinType.LEFT).get("email")),
                    searchPattern));
            predicates.add(criteriaBuilder
                    .like(criteriaBuilder.lower(root.join("user", JoinType.LEFT).get("phoneNumber")), searchPattern));

            // Safely join and check supplier fields using LEFT JOIN
            if (root.getJoins().stream().noneMatch(j -> j.getAttribute().getName().equals("supplier"))) {
                root.join("supplier", JoinType.LEFT);
            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("supplier", JoinType.LEFT).get("name")),
                    searchPattern));
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.join("supplier", JoinType.LEFT).get("contactInfo")), searchPattern));

            // Safely join and check product fields using LEFT JOIN
            if (root.getJoins().stream().noneMatch(j -> j.getAttribute().getName().equals("product"))) {
                root.join("product", JoinType.LEFT);
            }

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product", JoinType.LEFT).get("name")),
                    searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product", JoinType.LEFT).get("sku")),
                    searchPattern));
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.join("product", JoinType.LEFT).get("description")), searchPattern));

            // Safely join product category using LEFT JOIN
            if (root.getJoins().stream().noneMatch(j -> j.getAttribute().getName().equals("product")) &&
                    root.join("product").getJoins().stream()
                            .noneMatch(j -> j.getAttribute().getName().equals("category"))) {
                root.join("product", JoinType.LEFT).join("category", JoinType.LEFT);
            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product", JoinType.LEFT)
                    .join("category", JoinType.LEFT).get("name")), searchPattern));

            // Combine all predicates with OR
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        });
    }

    // Lọc theo tháng và năm 
    // New method for filtering transactions by month and year
    public static Specification<Transaction> byMonthAndYear(int month, int year) {
        return (root, query, criteriaBuilder) -> {
            // Use the month and year functions on the createdAt date field
            Expression<Integer> monthExpression = criteriaBuilder.function("month", Integer.class,
                    root.get("createdAt"));
            Expression<Integer> yearExpression = criteriaBuilder.function("year", Integer.class, root.get("createdAt"));

            // Create predicates for the month and year
            Predicate monthPredicate = criteriaBuilder.equal(monthExpression, month);
            Predicate yearPredicate = criteriaBuilder.equal(yearExpression, year);

            // Combine the month and year predicates
            return criteriaBuilder.and(monthPredicate, yearPredicate);
        };
    }
}

