package com.quyen.hust.repository.admin;

import com.quyen.hust.entity.admin.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountCodeJpaRepository extends JpaRepository<DiscountCode, Long> {




}
