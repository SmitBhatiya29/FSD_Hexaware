package com.springboot.easypay.service;

import com.springboot.easypay.dto.BenefitReqDto;
import com.springboot.easypay.dto.BenfitResDto;
import com.springboot.easypay.dto.PageBenefitResDto;
import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.mapper.BenefitMapper;
import com.springboot.easypay.mapper.EmployeeMapper;
import com.springboot.easypay.model.Benefit;
import com.springboot.easypay.repository.BenefitRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PageBenefitResDto getBenefit(int page, int size) {

        log.info("Fetching benefits list ");

        Pageable pageable = PageRequest.of(page,size);
        Page<Benefit> benefitPage = benefitRepository.findAll(pageable);
        long totalRecords = benefitPage.getTotalElements();
        int totalPages = benefitPage.getTotalPages();

        log.info("Benefits fetched successfully. ");

        List<BenfitResDto> list = benefitPage.stream().map(BenefitMapper::mapToBenefit).toList();
        return new PageBenefitResDto(list,totalRecords,totalPages);
    }

    public void updateBenefit(Long id, BenefitReqDto benefitReqDto) {
        log.info("Updating benefit with id: {}", id);
        Benefit benefit = benefitRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Benefit for this id is not found" + id));
        benefit.setBenefitName(benefitReqDto.benefitName());
        benefit.setDescription(benefitReqDto.description());
        benefitRepository.save(benefit);
        log.info("Successfully updated benefit: {}", benefitReqDto.benefitName());
    }


    public void deleteBenefit(Long id) {
        log.info("Deleting benefit with id: {}", id);
        if (!benefitRepository.existsById(id)) {
            throw new RuntimeException("Benefit not found with id: " + id);
        }
        benefitRepository.deleteById(id);
        log.info("Successfully deleted benefit with id: {}", id);
    }
}
