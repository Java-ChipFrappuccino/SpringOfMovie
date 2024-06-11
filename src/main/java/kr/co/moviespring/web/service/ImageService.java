//package kr.co.moviespring.web.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartRequest;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.UUID;
//
//@Service
//public class ImageService {
//
//    private String localLocation = "C:\\Users\\Joon\\Desktop\\teamprj\\";
//
//    public String imageUpload(MultipartRequest request) throws IOException {
//
//        MultipartFile file = request.getFile("upload");
//
//        String fileName = file.getOriginalFilename();
//        String ext = fileName.substring(fileName.indexOf("."));
//
//        String uuidFileName = UUID.randomUUID() + ext;
//        String localPath = localLocation + uuidFileName;
//
//        File localFile = new File(localPath);
//        file.transferTo(localFile);
//
//
////        localFile.delete();
//
//        return "redirect:/index";
//
//    }
//
//    public String saveImage(MultipartFile file) throws IOException {
//        String fileName = file.getOriginalFilename();
//        String ext = fileName.substring(fileName.lastIndexOf("."));
//        String uuidFileName = UUID.randomUUID().toString() + ext;
//        String localPath = localLocation + uuidFileName;
//        File localFile = new File(localPath);
//        file.transferTo(localFile);
//        // 이미지 URL을 반환
//        return "http://localhost/image/" + uuidFileName;
//    }
//}