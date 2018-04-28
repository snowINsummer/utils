package qa.utils;

import javax.validation.*;
import java.util.Iterator;
import java.util.Set;

public class ValidationUtil {

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public ValidationUtil() {
    }

    public static <T> void validate(T t) throws ValidationException {
        ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = vFactory.getValidator();
        Set<ConstraintViolation<T>> set = validator.validate(t, new Class[0]);
        if (set.size() > 0) {
            StringBuilder validateError = new StringBuilder();
            Iterator var5 = set.iterator();

            while(var5.hasNext()) {
                ConstraintViolation<T> val = (ConstraintViolation)var5.next();
                validateError.append(val.getPropertyPath().toString()).append(":").append(val.getMessage()).append(",");
            }

            String validateErrorMsg = validateError.toString();
            throw new ValidationException(validateErrorMsg.substring(0, validateErrorMsg.length() - 1));
        }
    }
}
