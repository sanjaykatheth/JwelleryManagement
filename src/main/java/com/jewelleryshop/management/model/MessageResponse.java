package com.jewelleryshop.management.model;

import com.jewelleryshop.management.constants.ResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageResponse {
	private ResponseMessage message;
}