package com.springboot.easypay.mapper;

import com.springboot.easypay.dto.BenfitResDto;
import com.springboot.easypay.model.Benefit;

public class BenefitMapper {
    public static BenfitResDto mapToBenefit(Benefit benefit) {
        return new BenfitResDto(
                benefit.getId(),
                benefit.getBenefitName(),
                benefit.getDescription());
    }
}
