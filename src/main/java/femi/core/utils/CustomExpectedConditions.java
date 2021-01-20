package femi.core.utils;

import femi.core.custom_assertions.CustomAssertion;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class CustomExpectedConditions{

    public static ExpectedCondition<Boolean> elementIsNotEmpty(final WebElement element) {
        return driver -> {
            Boolean hasText = false;
            try {
                hasText = !element.getText().isEmpty();
            } catch (Exception e) {
                CustomAssertion.fail(element + "don't contains any text.");
            }
            return hasText;
        };
    }

}
