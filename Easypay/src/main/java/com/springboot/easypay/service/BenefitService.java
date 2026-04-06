package com.springboot.easypay.service;

import com.springboot.easypay.dto.BenefitReqDto;
import com.springboot.easypay.model.Benefit;
import com.springboot.easypay.repository.BenefitRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class BenefitService {
    private BenefitRepository benefitRepository;
    public void addBenefit(BenefitReqDto benefitReqDto) {
        log.info("Adding new benefit: {}", benefitReqDto.benefitName());
        Benefit benefit = new Benefit();
        benefit.setBenefitName(benefitReqDto.benefitName());
        benefit.setDescription(benefitReqDto.description());
        benefitRepository.save(benefit);
        log.info("Successfully added benefit: {}", benefitReqDto.benefitName());
    }
}
