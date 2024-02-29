package org.mjulikelion.bagel.util.annotaion.file.introduce;

import static org.mjulikelion.bagel.constant.FileConstant.MAX_FILE_SIZE;
import static org.mjulikelion.bagel.constant.FileConstant.X_ZIP_CONTENT_TYPE;
import static org.mjulikelion.bagel.constant.FileConstant.ZIP_CONTENT_TYPE;
import static org.mjulikelion.bagel.constant.FileConstant.ZIP_EXTENSION;
import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_STUDENT_ID_PATTERN;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class IntroduceFileConstraintValidator implements ConstraintValidator<IntroduceFileConstraint, MultipartFile> {
    @Override
    public void initialize(IntroduceFileConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        log.info("Start validate file");
        return !isEmptyFile(file) && (isValidFileName(file.getOriginalFilename()) && isValidFileExtension(file)
                && isValidFileSize(file));
    }

    private boolean isEmptyFile(MultipartFile file) {
        log.info("Is file empty? {}",
                file == null || file.isEmpty() || file.getSize() == 0 || file.getOriginalFilename() == null
                        || file.getOriginalFilename().isEmpty());
        return file == null || file.isEmpty() || file.getSize() == 0 || file.getOriginalFilename() == null
                || file.getOriginalFilename().isEmpty();
    }

    private boolean isValidFileName(String fileName) {
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
        log.info("Is file name valid? {}", fileNameWithoutExtension.matches(APPLICATION_STUDENT_ID_PATTERN));
        return fileNameWithoutExtension.matches(APPLICATION_STUDENT_ID_PATTERN);
    }

    private boolean isValidFileExtension(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        boolean isValidContentType =
                Objects.equals(contentType, ZIP_CONTENT_TYPE) || Objects.equals(contentType, X_ZIP_CONTENT_TYPE);
        boolean isValidFileExtension = filename != null && filename.toLowerCase().endsWith(ZIP_EXTENSION);

        log.info("Is file extension valid? {}", isValidContentType && isValidFileExtension);

        return isValidContentType && isValidFileExtension;
    }


    private boolean isValidFileSize(MultipartFile file) {
        log.info("Is file size valid? {}", file.getSize() <= MAX_FILE_SIZE);
        return file.getSize() <= MAX_FILE_SIZE;
    }
}
