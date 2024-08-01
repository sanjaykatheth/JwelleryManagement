package com.jewelleryshop.management.model.request;

import java.util.List;

import com.jewelleryshop.management.model.vendor.AccountDepartment;
import com.jewelleryshop.management.model.vendor.BankDetails;
import com.jewelleryshop.management.model.vendor.ContactDetails;
import com.jewelleryshop.management.model.vendor.DealerInfo;
import com.jewelleryshop.management.model.vendor.FirmDetail;
import com.jewelleryshop.management.model.vendor.PaymentTerms;

import lombok.Data;

@Data
public class UpdateVendorRequest {
    private ContactDetails contactDetails;
    private FirmDetail firmDetail;
    private List<DealerInfo> dealerInfoList; // Example of another field
    private List<BankDetails> bankDetailList; // Example of another field
    private AccountDepartment accountDepartment; // Example of another field
    private PaymentTerms paymentTerms; // Example of another field

}
