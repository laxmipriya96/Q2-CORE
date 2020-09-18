/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.contoller;

import com.vm.qsmart2api.dtos.BadRequestException;
import com.vm.qsmart2api.dtos.SResponse;
import java.util.Date;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author SOMANADH PHANI
 */
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     *
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handelAllException(Exception ex,
            WebRequest request) {
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        //SResponse excResp = new SResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(excResp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handelDataNotFoundException(BadRequestException ex, 
            WebRequest request){
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        return new ResponseEntity(excResp, HttpStatus.BAD_REQUEST);
    }
    
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        return new ResponseEntity(excResp, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        return new ResponseEntity(excResp, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        return new ResponseEntity(excResp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        return new ResponseEntity(excResp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        return new ResponseEntity(excResp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        return new ResponseEntity(excResp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        return new ResponseEntity(excResp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        return new ResponseEntity(excResp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SResponse excResp = new SResponse(false, ex.getMessage(), null, new Date());
        return new ResponseEntity(excResp, HttpStatus.BAD_REQUEST);
    }

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        SResponse excResp = new SResponse(false, "Validation Failed", null, new Date());
//        return new ResponseEntity(excResp, HttpStatus.BAD_REQUEST);
//    }
//    
}
