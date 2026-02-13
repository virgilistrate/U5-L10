package virgilistrate.U5L10.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<String> errorsMessages;

    public ValidationException(List<String> errorsMessages) {
        super("Errore/i di validazione nel payload!");
        this.errorsMessages = errorsMessages;
    }

    public List<String> getErrorsMessages() {
        return errorsMessages;
    }
}
