package mum.pmp.mstore.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import mum.pmp.mstore.model.Admin;

@Component
public class AdminValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	public void validate(Object target, Errors errors) {

		Admin admin = (Admin) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "field.required");
		
		if (admin.getFirstName().length() < 2 || admin.getFirstName().length() > 20) {
			errors.rejectValue("firstName", "First name must be 2 to 20 alpha numeric characters.");
		}
		
		if (admin.getLastName().length() < 2 || admin.getLastName().length() > 20) {
			errors.rejectValue("lastName", "Last name must be 2 to 20 alpha numeric characters.");
		}
		
		if (!admin.getPassword().equals(admin.getConfirmPassword())) {
			errors.rejectValue("password", "nomatch.password");
		}

	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(new AdminValidator());
	}

}
