package hku.hk.EmoLM.config;

import hku.hk.EmoLM.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FileCleanupTask {

    @Autowired
    private FileService fileService;

    @Scheduled(cron = "0 0 0 * * ?") // 每天午夜执行
    public void cleanUpFiles() {
        fileService.deleteExpiredFiles();
    }
}