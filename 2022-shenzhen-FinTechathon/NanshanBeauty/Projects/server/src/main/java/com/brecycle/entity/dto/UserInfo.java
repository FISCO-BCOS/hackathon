package com.brecycle.entity.dto;

import com.brecycle.entity.Resource;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author cmgun
 */
@Data
@Builder
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String token;

    private List<String> roles;

    private List<Resource> resources;
}
