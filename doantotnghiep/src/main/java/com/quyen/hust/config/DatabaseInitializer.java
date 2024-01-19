package com.quyen.hust.config;

import com.quyen.hust.entity.admin.DiscountCode;
import com.quyen.hust.entity.admin.Role;
import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.repository.admin.DiscountCodeJpaRepository;
import com.quyen.hust.repository.admin.RoleJpaRepository;
import com.quyen.hust.repository.admin.TrainingFieldJpaRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.statics.Roles;
import com.quyen.hust.statics.Unit;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;

@Component
@AllArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TrainingFieldJpaRepository trainingFieldJpaRepository;
    private final DiscountCodeJpaRepository discountCodeJpaRepository;

    @Override
    public void run(String... args) throws Exception {
        //khởi tại các role
        Optional<Role> userRoleOptional = roleJpaRepository.findByName(Roles.USER);
        Optional<Role> adminRoleOptional = roleJpaRepository.findByName(Roles.ADMIN);
        Optional<Role> teacherRoleOptional = roleJpaRepository.findByName(Roles.TEACHER);
        if (userRoleOptional.isEmpty()) {
            Role userRole = Role.builder().name(Roles.USER).build();
            roleJpaRepository.save(userRole);
        }
        if (adminRoleOptional.isEmpty()) {
            Role adminRole = Role.builder().name(Roles.ADMIN).build();
            roleJpaRepository.save(adminRole);
        }
        if (teacherRoleOptional.isEmpty()) {
            Role teacherRole = Role.builder().name(Roles.TEACHER).build();
            roleJpaRepository.save(teacherRole);
        }

        //khởi tạo dữ liệu lĩnh vực đào tạo
        TrainingField codeFieldName = trainingFieldJpaRepository.findByFieldName("Lập trình");
        TrainingField securityFieldName = trainingFieldJpaRepository.findByFieldName("Bảo mật");
        TrainingField generalFieldName = trainingFieldJpaRepository.findByFieldName("Đại cương");
        if (ObjectUtils.isEmpty(codeFieldName)) {
            TrainingField codeField = TrainingField.builder()
                    .fieldName("Lập trình")
                    .description("Bao gồm các khóa học: lập trình Java, JavaScript, C, C++, C#, Python, HTML, CSS, ...")
                    .build();
            trainingFieldJpaRepository.save(codeField);
        }
        if (ObjectUtils.isEmpty(securityFieldName)) {
            TrainingField securityField = TrainingField.builder()
                    .fieldName("Bảo mật")
                    .description("Bao gồm các khóa học: an ninh mạng, bảo mật ứng dụng web, kiểm thử xâm nhập, ...")
                    .build();
            trainingFieldJpaRepository.save(securityField);
        }
        if (ObjectUtils.isEmpty(generalFieldName)) {
            TrainingField generalField = TrainingField.builder()
                    .fieldName("Đại cương")
                    .description("Bao gồm các khóa học: Cấu trúc dữ liệu và giải thuật, Cơ sở dữ liệu, Hệ điều hành, Toán cao cấp, ...")
                    .build();
            trainingFieldJpaRepository.save(generalField);
        }

        //khởi tạo mã giảm giá
        List<DiscountCode> discountCodes = discountCodeJpaRepository.findAll();
        if (ObjectUtils.isEmpty(discountCodes)) {
            DiscountCode discountCode = DiscountCode.builder()
                    .codeName("HAPPYNEWYEAR")
                    .discountValue(900000d)
                    .discountUnit(Unit.VND)
                    .activated(true)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(30l))
                    .build();
            discountCodeJpaRepository.save(discountCode);
        }


    }



}
