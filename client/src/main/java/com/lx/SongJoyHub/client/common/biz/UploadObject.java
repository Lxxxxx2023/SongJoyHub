package com.lx.SongJoyHub.client.common.biz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传后返回类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadObject {
    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 音频时长
     */
    private String duration;
}