package com.lx.SongJoyHub.client.controller;

import cn.hutool.core.io.FileUtil;
import com.lx.SongJoyHub.client.common.biz.UploadObject;
import com.lx.SongJoyHub.client.common.constant.FileConstant;
import com.lx.SongJoyHub.client.common.cos.CosManager;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

/**
 * 文件传输层
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {

    private final CosManager cosManager;

    private static final long max_memory = 1024 * 1024 * 16;

    /**
     * 上传歌曲
     */
    @PostMapping("/upload")
    public Result<UploadObject> uploadFileSong(@RequestPart("file") MultipartFile multipartFile, HttpServletRequest request) {
        validFile(multipartFile);
        String uuid = RandomStringUtils.randomAlphabetic(8);
        String fileName = uuid + "_" + multipartFile.getOriginalFilename();
        String filePath = String.format("/%s", fileName);
        File file = null;
        try {
            file = File.createTempFile(filePath, null);
            multipartFile.transferTo(file);
            String duration = null;
            if (Objects.equals(FileUtil.getSuffix(multipartFile.getOriginalFilename()), "mp3")) {
                // 如果是音频文件还需解析音频时长
                duration = analysisMusicDuration(file);
            }
            cosManager.putObject(filePath, file);
            return Results.success(UploadObject.builder().filePath(FileConstant.COS_HOST + filePath)
                    .duration(duration)
                    .build());
        } catch (Exception e) {
            log.error("文件上传失败 filePath = {}", filePath, e);
            throw new ServiceException("文件上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filePath);
                }
            }
        }
    }

    private void validFile(MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size > max_memory) {
            throw new ServiceException("上传大小不能超过 16M");
        }
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        if (!Arrays.asList("mp3", "ogg", "lrc").contains(suffix)) {
            throw new ServiceException("文件类型错误");
        }
    }

    public String analysisMusicDuration(File file) {
        int minutes = 0, seconds = 0;
        try (InputStream input = new FileInputStream(file)) {
            // Mp3Parser for MP3 files
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            Mp3Parser mp3Parser = new Mp3Parser();
            mp3Parser.parse(input, handler, metadata, new ParseContext());

            // 获取时长并转换为秒
            String durationStr = metadata.get("xmpDM:duration");
            double durationInSeconds = Double.parseDouble(durationStr);

            // 转换成分钟:秒的格式
            minutes = (int) (durationInSeconds / 60);
            seconds = (int) (durationInSeconds % 60);

        } catch (Exception e) {
            System.err.println("音频解析失败 " + e.getMessage());
        }
        return minutes + ":" + seconds;
    }
}
