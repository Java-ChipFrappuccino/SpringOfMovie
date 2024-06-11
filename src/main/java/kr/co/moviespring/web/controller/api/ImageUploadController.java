package kr.co.moviespring.web.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class ImageUploadController {

//    @Autowired
//    private final WebApplicationContext context;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping(value = "/image-upload")
    // @RequestParam은 자바스크립트에서 설정한 이름과 반드시 같아야합니다.
    public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        try {
            // 서버에 저장할 경로
//            String uploadDirectory = context.getServletContext().getRealPath(uploadDir);
            String uploadDirectory = Paths.get(uploadDir).toAbsolutePath().toString();
            System.out.println(uploadDir);

            // 디렉토리가 존재하지 않으면 생성
            File dir = new File(uploadDirectory);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 업로드 된 파일의 이름
            String originalFileName = file.getOriginalFilename();

            // 업로드 된 파일의 확장자
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // 업로드 될 파일의 이름 재설정 (중복 방지를 위해 UUID 사용)
            String uuidFileName = UUID.randomUUID().toString() + fileExtension;

            // 위에서 설정한 서버 경로에 이미지 저장
            file.transferTo(new File(uploadDirectory, uuidFileName));

            System.out.println("************************ 업로드 컨트롤러 실행 ************************");
            System.out.println(uploadDirectory);
//            String result = "/image/upload/" +uuidFileName;
            String imageUrl = "/image/upload/" + uuidFileName;
            // Ajax에서 업로드 된 파일의 이름을 응답 받을 수 있도록 해줍니다.
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
//                        String uploadDirectory = Paths.get(uploadDir).toAbsolutePath().toString();
//            System.out.println(uploadDirectory);
            System.out.println(uploadDir);

            return ResponseEntity.badRequest().body("이미지 업로드 실패");

        }

    }
}
