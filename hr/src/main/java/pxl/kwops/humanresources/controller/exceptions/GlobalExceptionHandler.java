package pxl.kwops.humanresources.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pxl.kwops.api.models.ErrorModel;
import pxl.kwops.domain.exceptions.ContractException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ContractException.class)
    public ResponseEntity<ErrorModel> handleContractException(ContractException ex) {
        return new ResponseEntity<>(new ErrorModel(ex.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

}
