package co.edu.iudigital.sendelapa.exception;

public class InternalServerErrorException extends RestException {

    private static final long serialVersionUID = 1L;
    private String codeError;

    public InternalServerErrorException(String msg, String codeError, Exception ex) {
        super(msg, ex);
        this.codeError = codeError;
    }

    public InternalServerErrorException(String msg, Exception ex) {
        super(msg, ex);
    }

    public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(ErrorDto errorDto) {
        super(errorDto);
    }

    public String getCodeError() {
        return codeError;
    }
}
