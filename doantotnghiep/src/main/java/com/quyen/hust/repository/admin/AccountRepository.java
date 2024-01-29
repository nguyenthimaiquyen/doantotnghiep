package com.quyen.hust.repository.admin;

import com.quyen.hust.model.request.SearchRequest;
import com.quyen.hust.model.response.user.UserDataResponse;
import com.quyen.hust.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class AccountRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<UserDataResponse> searchUser(SearchRequest request) {
        String sql = "WITH RECURSIVE data_user AS (\n" +
                "    SELECT\n" +
                "        u.id,\n" +
                "        u.full_name,\n" +
                "        u.email,\n" +
                "        u.user_status,\n" +
                "        r.name AS role_name,\n" +
                "        ROW_NUMBER() OVER (PARTITION BY u.id ORDER BY ur.user_id) AS row_num\n" +
                "    FROM\n" +
                "        users u\n" +
                "    LEFT JOIN\n" +
                "        users_roles ur ON u.id = ur.user_id\n" +
                "    LEFT JOIN\n" +
                "        roles r ON ur.role_id = r.id\n" +
                "    {search_condition}\n" +
                "    LIMIT {limit_number}\n" +
                "    OFFSET {offset_number}\n" +
                "),\n" +
                "count_user AS (\n" +
                "    SELECT\n" +
                "        COUNT(DISTINCT u.id) total_record\n" +
                "    FROM\n" +
                "        users u\n" +
                "    LEFT JOIN\n" +
                "        users_roles ur ON u.id = ur.user_id\n" +
                "    LEFT JOIN\n" +
                "        roles r ON ur.role_id = r.id\n" +
                "    {search_condition}\n" +
                ")\n" +
                "SELECT\n" +
                "    d.id,\n" +
                "    d.full_name,\n" +
                "    d.email,\n" +
                "    d.user_status,\n" +
                "    d.role_name,\n" +
                "    c.total_record totalRecord\n" +
                "FROM\n" +
                "    data_user d\n" +
                "JOIN\n" +
                "    count_user c ON 1 = 1\n" +
                "WHERE\n" +
                "    d.row_num = 1";

        String searchCondition = " where 1 = 1 ";
        Map<String, Object> parameters = new HashMap<>();
        if (StringUtils.hasText(request.getName())) {
            searchCondition += "and u.full_name like :full_name \n";
            parameters.put("full_name", "%" + StringUtil.escapeWildCardCharacter(request.getName()) + "%");
        }

        sql = sql.replace("{search_condition}", searchCondition);
        sql = sql.replace("{limit_number}", Integer.valueOf(request.getPageSize()).toString());
        String offsetNumber = "0";
        if (request.getCurrentPage() != 0) {
            offsetNumber = Integer.valueOf(request.getCurrentPage() * request.getPageSize()).toString();
        }
        sql = sql.replace("{offset_number}", offsetNumber);

        return namedParameterJdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(UserDataResponse.class));
    }

}
