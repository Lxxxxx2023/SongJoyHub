package com.lx.SongJoyHub.client.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 多条件查询歌曲
 */
@Data
public class SongMultipleQueryReqDTO {

    private Long id;

    private Long committerId;

    private Long opId;

    private Integer type;

    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date minCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date maxCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date minUpdateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date maxUpdateTime;

    private Integer page;

    private Integer pageSize;
}
