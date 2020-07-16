package top.youmunan.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.youmunan.community.dto.FileDTO;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Controller
@ResponseBody
public class FileController {
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");

        if (file == null || file.isEmpty()) {
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("文件不存在");
            return fileDTO;
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String savePath = "src/main/resources/static/images/userImg/";
        String visitPath = "/images/userImg/" + fileName;


        File fileDir = new File(savePath);


        if (!fileDir.isDirectory()) {
            fileDir.mkdirs();
        }

        System.out.println(fileDir.getAbsolutePath());

        File saveFile = new File(fileDir.getAbsolutePath() + File.separator + fileName);

        try {
            file.transferTo(saveFile);
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setMessage("上传成功");
            fileDTO.setUrl(visitPath);
            return fileDTO;
        } catch (IOException e) {
            e.printStackTrace();
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("文件上传失败，请稍后再试");
            return fileDTO;
        }


    }
}
