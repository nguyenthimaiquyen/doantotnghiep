package com.quyen.hust.repository.course;

import com.quyen.hust.entity.admin.Role;
import com.quyen.hust.model.request.search.CourseSearchRequest;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.repository.teacher.TeacherJpaRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.security.SecurityUtils;
import com.quyen.hust.statics.Roles;
import com.quyen.hust.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Repository
@AllArgsConstructor
public class CourseRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TeacherJpaRepository teacherJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public List<CourseDataResponse> searchCourse(CourseSearchRequest request) {
        String sql = "WITH RECURSIVE data_course AS (\n" +
                "    SELECT\n" +
                "        co.id, co.title, co.description, co.learning_objectives, co.course_fee, co.course_fee_unit, " +
                "        co.course_status, co.difficulty_level, co.image_url, co.learner_count, co.rating, " +
                "        te.id teacherId, us.full_name teacherName, di.code_name discountCodeName, " +
                "        GROUP_CONCAT(tr.field_name SEPARATOR ', ') trainingFields, ra.rating_value ratingValue\n" +
                "    FROM courses co\n" +
                "    LEFT JOIN ratings ra ON ra.course_id = co.id\n" +
                "    LEFT JOIN discount_codes di ON co.discount_id = di.id\n" +
                "    LEFT JOIN teachers te ON co.teacher_id = te.id\n" +
                "    LEFT JOIN users us ON us.id = te.user_id\n" +
                "    LEFT JOIN courses_training_fields cotr ON co.id = cotr.course_id\n" +
                "    LEFT JOIN training_fields tr ON cotr.training_field_id = tr.id\n" +
                "    \n" +
                "    {search_condition}" +
                "   GROUP BY co.id, te.id, us.full_name, di.code_name, ra.rating_value \n" +
                "    LIMIT {limit_number}\n" +
                "    OFFSET {offset_number}\n" +
                "),\n" +
                "count_course AS (\n" +
                "    SELECT COUNT(DISTINCT co.id) total_record\n" +
                "    FROM courses co\n" +
                "    LEFT JOIN ratings ra ON ra.course_id = co.id\n" +
                "    LEFT JOIN discount_codes di ON co.discount_id = di.id\n" +
                "    LEFT JOIN teachers te ON co.teacher_id = te.id\n" +
                "    LEFT JOIN users us ON us.id = te.user_id\n" +
                "    LEFT JOIN courses_training_fields cotr ON co.id = cotr.course_id\n" +
                "    LEFT JOIN training_fields tr ON cotr.training_field_id = tr.id\n" +
                "    {search_condition} " +
                ")\n" +
                "SELECT d.*, c.total_record totalRecord\n" +
                "FROM data_course d, count_course c";
        String searchCondition = " where 1 = 1 and co.course_status LIKE \"PUBLISHED\" ";
        Map<String, Object> parameters = new HashMap<>();
        if (StringUtils.hasText(request.getCourseName())) {
            searchCondition += "and co.title like :title \n";
            parameters.put("title", "%" + StringUtil.escapeWildCardCharacter(request.getCourseName()) + "%");
        }
        if (!ObjectUtils.isEmpty(request.getCourseFee())) {
            if (request.getCourseFee().contains(0d) && !request.getCourseFee().contains(1d)) {
                searchCondition += "and co.course_fee = :course_fee \n";
                parameters.put("course_fee", 0);
            }
            if (request.getCourseFee().contains(1d) && !request.getCourseFee().contains(0d)) {
                searchCondition += "and co.course_fee >= :course_fee \n";
                parameters.put("course_fee", 1);
            }
            if (request.getCourseFee().contains(1d) && request.getCourseFee().contains(0d)) {
                searchCondition += "and co.course_fee >= :course_fee \n";
                parameters.put("course_fee", 0);
            }
        }
        if (!ObjectUtils.isEmpty(request.getRatingValue())) {
            searchCondition += "and co.rating >= :rating_value \n";
            parameters.put("rating_value", request.getRatingValue());
        }
        if (!ObjectUtils.isEmpty(request.getDifficultyLevel())) {
            if (request.getDifficultyLevel().size() == 1) {
                searchCondition += "and co.difficulty_level LIKE :difficulty_level \n";
                parameters.put("difficulty_level", request.getDifficultyLevel().get(0));
            } else {
                searchCondition += "and (";
                for (int i = 0; i < request.getDifficultyLevel().size(); i++) {
                    searchCondition += " co.difficulty_level LIKE :difficulty_level_" + i;
                    parameters.put("difficulty_level_" + i, request.getDifficultyLevel().get(i));
                    if (i < request.getDifficultyLevel().size() - 1) {
                        searchCondition += " or ";
                    }
                }
                searchCondition += ") \n";
            }
        }
        if (!ObjectUtils.isEmpty(request.getTrainingFieldId())) {
            if (request.getTrainingFieldId().size() == 1) {
                searchCondition += "and tr.id = :id \n";
                parameters.put("id", request.getTrainingFieldId().get(0));
            } else {
                searchCondition += "and (";
                for (int i = 0; i < request.getTrainingFieldId().size(); i++) {
                    searchCondition += " tr.id = :id_" + i;
                    parameters.put("id_" + i, request.getTrainingFieldId().get(i));
                    if (i < request.getTrainingFieldId().size() - 1) {
                        searchCondition += " or ";
                    }
                }
                searchCondition += ") \n";
            }
        }
        if (!ObjectUtils.isEmpty(request.getTeacherId())) {
            if (request.getTeacherId().size() == 1) {
                searchCondition += "and co.teacher_id = :teacherId \n";
                parameters.put("teacherId", request.getTeacherId().get(0));
            } else {
                searchCondition += "and (";
                for (int i = 0; i < request.getTeacherId().size(); i++) {
                    searchCondition += " co.teacher_id = :teacherId_" + i;
                    parameters.put("teacherId_" + i, request.getTeacherId().get(i));
                    if (i < request.getTeacherId().size() - 1) {
                        searchCondition += " or ";
                    }
                }
                searchCondition += ") \n";
            }
        }
        Optional<Long> userLoginId = SecurityUtils.getCurrentUserLoginId();
        boolean hasAdminRole = false;
        boolean hasTeacherRole = false;
        if (userLoginId.isPresent()) {
            //check role
            Set<Role> roles = userJpaRepository.findById(userLoginId.get()).get().getRoles();
            for (Role role : roles ) {
                if (Roles.TEACHER.getCode().equals(role.getName().getCode())) {
                    hasTeacherRole = true;
                } else if (Roles.ADMIN.getCode().equals(role.getName().getCode())) {
                    hasAdminRole = true;
                }
            }
            if (hasTeacherRole && !hasAdminRole) {
                Long teacherId = teacherJpaRepository.findByUserId(userLoginId.get()).getId();
                if (!ObjectUtils.isEmpty(teacherId)) {
                    searchCondition += "and co.teacher_id = :teacherId \n";
                    parameters.put("teacherId", teacherId);
                }
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
