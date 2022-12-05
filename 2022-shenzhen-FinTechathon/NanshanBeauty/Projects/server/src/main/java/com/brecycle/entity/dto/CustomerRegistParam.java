package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cmgun
 */
@Data
public class CustomerRegistParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;

    private String name;

    private String phone;

    private String idno;
}
