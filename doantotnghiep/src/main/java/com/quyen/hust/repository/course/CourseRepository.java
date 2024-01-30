package com.quyen.hust.repository.course;

import com.quyen.hust.model.request.search.CourseSearchRequest;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.repository.teacher.TeacherJpaRepository;
import com.quyen.hust.security.SecurityUtils;
import com.quyen.hust.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CourseRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TeacherJpaRepository teacherJpaRepository;

    public List<CourseDataResponse> searchCourse(CourseSearchRequest request) {
        String sql = "WITH RECURSIVE data_course AS (\n" +
                "    SELECT\n" +
                "        co.id, co.title, co.description, co.learning_objectives, co.course_fee, co.course_fee_unit, " +
                "        co.course_status, co.difficulty_level, us.full_name teacherName, di.code_name discountCodeName, " +
                "        tr.field_name trainingFieldName, ra.rating_value ratingValue\n" +
                "    FROM courses co\n" +
                "    LEFT JOIN ratings ra ON ra.course_id = co.id\n" +
                "    LEFT JOIN discount_codes di ON co.discount_id = di.id\n" +
                "    LEFT JOIN teachers te ON co.teacher_id = te.id\n" +
                "    LEFT JOIN users us ON us.id = te.user_id\n" +
                "    LEFT JOIN courses_training_fields cotr ON co.id = cotr.course_id\n" +
                "    LEFT JOIN training_fields tr ON cotr.training_field_id = tr.id\n" +
                "    \n" +
                "    {search_condition}" +
                "    LIMIT {limit_number}\n" +
                "    OFFSET {offset_number}\n" +
                "),\n" +
                "count_course AS (\n" +
                "    SELECT COUNT(*) total_record\n" +
                "    FROM courses co\n" +
                "    LEFT JOIN ratings ra ON ra.course_id = co.id\n" +
                "    LEFT JOIN discount_codes di ON co.discount_id = di.id\n" +
                "    LEFT JOIN teachers te ON co.teacher_id = te.id\n" +
                "    LEFT JOIN users us ON us.id = te.user_id\n" +
                "    LEFT JOIN courses_training_fields cotr ON co.id = cotr.course_id\n" +
                "    LEFT JOIN training_fields tr ON cotr.training_field_id = tr.id\n" +
                "    {search_condition}" +
                ")\n" +
                "SELECT d.*, c.total_record totalRecord\n" +
                "FROM data_course d, count_course c";
        String searchCondition = " where 1 = 1 ";
        Map<String, Object> parameters = new HashMap<>();
        if (StringUtils.hasText(request.getCourseName())) {
            searchCondition += "and co.title like :title \n";
            parameters.put("title", "%" + StringUtil.escapeWildCardCharacter(request.getCourseName()) + "%");
        }
        if (!ObjectUtils.isEmpty(request.getCourseFeeStart()) && !ObjectUtils.isEmpty(request.getCourseFeeEnd())) {
            searchCondition += "and co.course_fee BETWEEN :start AND :end \n";
            parameters.put("start", request.getCourseFeeStart());
            parameters.put("end", request.getCourseFeeEnd());
        }
        if (!ObjectUtils.isEmpty(request.getRatingValueStart()) && !ObjectUtils.isEmpty(request.getRatingValueEnd())) {
            searchCondition += "and ra.rating_value BETWEEN :start AND :end \n";
            parameters.put("start", request.getRatingValueStart());
            parameters.put("end", request.getRatingValueEnd());
        }
        if (StringUtils.hasText(request.getDifficultyLevel())) {
            searchCondition += "and co.difficulty_level LIKE :difficulty_level \n";
            parameters.put("difficulty_level", request.getDifficultyLevel());
        }
        if (StringUtils.hasText(request.getTrainingFieldName())) {
            searchCondition += "and tr.field_name LIKE :field_name \n";
            parameters.put("field_name", request.getTrainingFieldName());
        }
        Optional<Long> userLoginId = SecurityUtils.getCurrentUserLoginId();
        if (userLoginId.isPresent()) {
            Long teacherId = teacherJpaRepository.findByUserId(userLoginId.get()).getId();
            if (!ObjectUtils.isEmpty(teacherId)) {
                searchCondition += "and te.id = :teacherId \n";
                parameters.put("teacherId", request.getTeacherId());
            }
        }

        sql = sql.replace("{search_condition}", searchCondition);
        sql = sql.replace("{limit_number}", Integer.valueOf(request.getPageSize()).toString());
        String offsetNumber = "0";
        if (request.getPageIndex() != 0) {
            offsetNumber = Integer.valueOf(request.getPageIndex() * request.getPageSize()).toString();
        }
        sql = sql.replace("{offset_number}", offsetNumber);

        return namedParameterJdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(CourseDataResponse.class));
    }


}
