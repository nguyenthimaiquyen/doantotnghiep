package com.quyen.hust.repository.course;

import com.quyen.hust.model.request.SearchRequest;
import com.quyen.hust.model.response.course.CourseDataResponse;
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
public class CourseRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<CourseDataResponse> searchCourse(SearchRequest request) {
        String sql = "WITH RECURSIVE data_course AS (\n" +
                "\tSELECT id, title, description, learning_objectives, course_fee, course_fee_unit, course_status, " +
                "difficulty_level, teacher_id, discount_id, training_field_id \n" +
                "\tFROM courses co\n" +
                "\t{search_condition}\n" +
                "\tLIMIT {limit_number}\n" +
                "\tOFFSET {offset_number}\n" +
                "),\n" +
                "count_course AS (\n" +
                "\tSELECT COUNT(*) total_record\n" +
                "\tFROM courses co\n" +
                "\t{search_condition}\n" +
                ")\n" +
                "SELECT d.*, c.total_record totalRecord \n" +
                "FROM data_course d, count_course c";

        String searchCondition = " where 1 = 1 ";
        Map<String, Object> parameters = new HashMap<>();
        if (StringUtils.hasText(request.getName())) {
            searchCondition += "and co.title like :title \n";
            parameters.put("title", "%" + StringUtil.escapeWildCardCharacter(request.getName()) + "%");
        }

        sql = sql.replace("{search_condition}", searchCondition);
        sql = sql.replace("{limit_number}", Integer.valueOf(request.getPageSize()).toString());
        String offsetNumber = "0";
        if (request.getCurrentPage() != 0) {
            offsetNumber = Integer.valueOf(request.getCurrentPage() * request.getPageSize()).toString();
        }
        sql = sql.replace("{offset_number}", offsetNumber);

        return namedParameterJdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(CourseDataResponse.class));
    }


}
