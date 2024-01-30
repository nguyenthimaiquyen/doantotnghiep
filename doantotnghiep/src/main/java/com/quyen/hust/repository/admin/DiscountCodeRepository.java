package com.quyen.hust.repository.admin;

import com.quyen.hust.model.request.search.CourseSearchRequest;
import com.quyen.hust.model.request.search.DiscountCodeSearchRequest;
import com.quyen.hust.model.response.admin.DiscountCodeDataResponse;
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
public class DiscountCodeRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<DiscountCodeDataResponse> searchDiscountCode(DiscountCodeSearchRequest request) {
        String sql = "WITH RECURSIVE data_discount_code AS (\n" +
                "\tSELECT id, code_name, discount_value, discount_unit, start_date, end_date, " +
                "usage_limitation_count, used_count, activated \n" +
                "\tFROM discount_codes dc\n" +
                "\t{search_condition}\n" +
                "\tLIMIT {limit_number}\n" +
                "\tOFFSET {offset_number}\n" +
                "),\n" +
                "count_discount_code AS (\n" +
                "\tSELECT COUNT(*) total_record\n" +
                "\tFROM discount_codes dc\n" +
                "\t{search_condition}\n" +
                ")\n" +
                "SELECT d.*, c.total_record totalRecord \n" +
                "FROM data_discount_code d, count_discount_code c";

        String searchCondition = " where 1 = 1 ";
        Map<String, Object> parameters = new HashMap<>();
        if (StringUtils.hasText(request.getDiscountCodeName())) {
            searchCondition += "and dc.code_name like :code_name \n";
            parameters.put("code_name", "%" + StringUtil.escapeWildCardCharacter(request.getDiscountCodeName()) + "%");
        }

        sql = sql.replace("{search_condition}", searchCondition);
        sql = sql.replace("{limit_number}", Integer.valueOf(request.getPageSize()).toString());
        String offsetNumber = "0";
        if (request.getPageIndex() != 0) {
            offsetNumber = Integer.valueOf(request.getPageIndex() * request.getPageSize()).toString();
        }
        sql = sql.replace("{offset_number}", offsetNumber);

        return namedParameterJdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(DiscountCodeDataResponse.class));
    }
}
