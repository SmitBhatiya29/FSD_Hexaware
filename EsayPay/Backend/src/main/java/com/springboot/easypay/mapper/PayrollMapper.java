package com.springboot.easypay.mapper;

import com.springboot.easypay.dto.PayrollRecordResDto;
import com.springboot.easypay.model.PayrollRecord;

public class PayrollMapper {
    public static PayrollRecordResDto mapToDto(PayrollRecord payrollRecord) {
        return new PayrollRecordResDto(
                payrollRecord.getId(),
                payrollRecord.getEmployee().getId(),
                payrollRecord.getEmployee().getFirstName() + " "+ payrollRecord.getEmployee().getLastName(),
                payrollRecord.getPayMonth(),
                payrollRecord.getTotalEarnings(),
                payrollRecord.getTotalDeductions(),
                payrollRecord.getNetPayable(),
                payrollRecord.getStatus(),
                payrollRecord.getProcessedDate()


        );
    }
}
