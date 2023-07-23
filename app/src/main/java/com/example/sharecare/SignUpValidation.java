package com.example.sharecare;

import android.util.Patterns;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.regex.Pattern;

public class SignUpValidation {

    // Validation pattern for user name (alphanumeric, underscore, and hyphen)
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{2,20}$");

            //^(?=.*\\d): At least one digit is required.
            //(?=.*[a-zA-Z]): At least one letter (character) is required (uppercase or lowercase).
            //(?=.*[@$!%*?&]): At least one special character from the set @$!%*?& is required.
            //.{6,}: The password should have a minimum length of 6 characters.
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[@$!%*?&]).{6,}$");


    public static boolean isUserNameValid(EditText editText) {
        String userName = editText.getText().toString().trim();

        if (userName.isEmpty()) {
            editText.setError("User Name is required.");
            return false;
        } else if (userName.length() < 4) {
            editText.setError("User Name must be at least 4 characters.");
            return false;
        } else if (userName.length() > 20) {
            editText.setError("User Name can't exceed 20 characters.");
            return false;
        } else if (!USERNAME_PATTERN.matcher(userName).matches()) {
            editText.setError("User Name can only contain letters, numbers, underscore, and hyphen.");
            return false;
        }

        return true;
    }


    public static boolean isPhoneNumberValid(EditText editText) {
        String phoneNumber = editText.getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            editText.setError("Phone Number is required.");
            return false;
        } else if (phoneNumber.length() != 10) {
            editText.setError("Phone Number must be exactly 10 digits.");
            return false;
        } else if (!phoneNumber.startsWith("0")) {
            editText.setError("Phone Number should start with 0.");
            return false;
        } else if (phoneNumber.startsWith("00")) {
            editText.setError("Phone Number should not start with 00.");
            return false;
        }

        return true;
    }

public static boolean isAddressValid(EditText editText) {
        String address = editText.getText().toString().trim();

        if (address.isEmpty()) {
            editText.setError("Address is required.");
            return false;
        } else if (address.length() > 100) {
            editText.setError("Address can't exceed 100 characters.");
            return false;
        }

        return true;
    }

    public static boolean isPasswordValid(EditText editText) {
        String password = editText.getText().toString().trim();

        if (password.isEmpty()) {
            editText.setError("Password is required.");
            return false;
        } else if (password.length() < 6) {
            editText.setError("Password must be at least 6 characters.");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            editText.setError("Password must have a combination of letters, numbers, and special characters.");
            return false;
        }

        return true;
    }

    public static boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString().trim();

        if (email.isEmpty()) {
            editText.setError("Email Address is required.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText.setError("Please enter a valid email address.");
            return false;
        }

        return true;
    }

    public static boolean isSpinnerSelectionValid(Spinner spinner, String selectionType) {
        String selectedValue = spinner.getSelectedItem().toString();

        if (selectedValue.isEmpty() || selectedValue.equals("Please select " + selectionType)) {
            Toast.makeText(spinner.getContext(), "Please select " + selectionType + ".", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
