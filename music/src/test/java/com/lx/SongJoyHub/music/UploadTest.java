package com.lx.SongJoyHub.music;

import com.lx.SongJoyHub.music.cos.CosManager;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
public class UploadTest {
    @Resource
    private CosManager cosManager;
    @Test
    public void test() {
        File file = new File("C:\\Users\\Administrator\\Pictures\\mayi.jpg");
        cosManager.putObject("img/may.jpg",file);
    }
}
