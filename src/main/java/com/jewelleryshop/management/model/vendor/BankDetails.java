package com.jewelleryshop.management.model.vendor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BankDetails {

	private String id;
	@NotNull(message = "Bank name cannot be null")
	private String bankName;

	@NotNull(message = "IFSC code cannot be null")
	private String ifscCode;
	@NotNull(message = "Account number cannot be null")
	@Size(min = 6, max = 20, message = "Account number must be between 6 and 20 characters")
	private String accountNumber;

	@NotNull(message = "Repeated account number cannot be null")
	@Size(min = 6, max = 20, message = "Repeated account number must be between 6 and 20 characters")
	private String repeatedAccountNumber;
	
	public void validateAccountNumbers() {
        if (!accountNumber.equals(repeatedAccountNumber)) {
            throw new IllegalArgumentException("Account number and repeated account number do not match");
        }
    }

}
