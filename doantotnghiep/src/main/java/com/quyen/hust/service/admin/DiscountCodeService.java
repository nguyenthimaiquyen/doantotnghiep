package com.quyen.hust.service.admin;

import com.quyen.hust.entity.admin.DiscountCode;
import com.quyen.hust.exception.DiscountCodeNotFoundException;
import com.quyen.hust.model.request.admin.DiscountCodeRequest;
import com.quyen.hust.model.response.admin.DiscountCodeResponse;
import com.quyen.hust.model.response.admin.DiscountCodeUnitResponse;
import com.quyen.hust.repository.admin.DiscountCodeJpaRepository;
import com.quyen.hust.statics.Unit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DiscountCodeService {
    private final DiscountCodeJpaRepository discountCodeJpaRepository;

    public List<DiscountCodeResponse> getAll() {
        List<DiscountCode> discountCodes = discountCodeJpaRepository.findAll();
        return discountCodes.stream().map(
                discountCode -> DiscountCodeResponse.builder()
                        .id(discountCode.getId())
                        .codeName(discountCode.getCodeName())
                        .discountValue(discountCode.getDiscountValue())
                        .discountUnit(discountCode.getDiscountUnit())
                        .startDate(discountCode.getStartDate())
                        .endDate(discountCode.getEndDate())
                        .usageLimitationCount(discountCode.getUsageLimitationCount())
                        .usedCount(discountCode.getUsedCount())
                        .activated(discountCode.getActivated())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<DiscountCodeUnitResponse> getDiscountCodeUnit() {
        return List.of(
                DiscountCodeUnitResponse.builder().code(Unit.USD.getCode()).name(Unit.USD.getName()).build(),
                DiscountCodeUnitResponse.builder().code(Unit.VND.getCode()).name(Unit.VND.getName()).build()
        );
    }

    public DiscountCodeResponse getDiscountCodeDetails(Long id) throws DiscountCodeNotFoundException {
        return discountCodeJpaRepository.findById(id).map(
                discountCode -> DiscountCodeResponse.builder()
                        .codeName(discountCode.getCodeName())
                        .discountValue(discountCode.getDiscountValue())
                        .discountUnit(discountCode.getDiscountUnit())
                        .startDate(discountCode.getStartDate())
                        .endDate(discountCode.getEndDate())
                        .usageLimitationCount(discountCode.getUsageLimitationCount())
                        .build()
        ).orElseThrow(() -> new DiscountCodeNotFoundException("Discount Code with id " + id + " could not be found!"));
    }

    @Transactional
    public void saveDiscountCode(DiscountCodeRequest request) {
        DiscountCode discountCode = DiscountCode.builder()
                .codeName(request.getCodeName())
                .discountValue(request.getDiscountValue())
                .discountUnit(request.getDiscountUnit())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .usageLimitationCount(request.getUsageLimitationCount())
                .build();
        if (!ObjectUtils.isEmpty(request.getId())) {
            //update a discount code
            DiscountCode dc = discountCodeJpaRepository.findById(request.getId()).get();
            dc.setCodeName(request.getCodeName());
            dc.setDiscountValue(request.getDiscountValue());
            dc.setDiscountUnit(request.getDiscountUnit());
            dc.setStartDate(request.getStartDate());
            dc.setEndDate(request.getEndDate());
            dc.setUsageLimitationCount(request.getUsageLimitationCount());
            return;
        }
        //create a discount code
        discountCode.setActivated(true);
        discountCodeJpaRepository.save(discountCode);
    }

    public void deleteDiscountCode(Long id) {
        discountCodeJpaRepository.deleteById(id);
    }
}
