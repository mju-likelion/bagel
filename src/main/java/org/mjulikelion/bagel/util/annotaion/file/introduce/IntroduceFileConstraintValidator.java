package org.mjulikelion.bagel.util.annotaion.file.introduce;

import static org.mjulikelion.bagel.constant.FileConstant.MAX_FILE_SIZE;
import static org.mjulikelion.bagel.constant.FileConstant.ZIP_CONTENT_TYPE;
import static org.mjulikelion.bagel.constant.FileConstant.ZIP_EXTENSION;
import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_STUDENT_ID_PATTERN;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

public class IntroduceFileConstraintValidator implements ConstraintValidator<IntroduceFileConstraint, MultipartFile> {
    @Override
    public void initialize(IntroduceFileConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return !isEmptyFile(file) && (isValidFileName(file.getOriginalFilename()) && isValidFileExtension(file)
                && isValidFileSize(file));
    }

    private boolean isEmptyFile(MultipartFile file) {
        return file == null || file.isEmpty() || file.getSize() == 0 || file.getOriginalFilename() == null
                || file.getOriginalFilename().isEmpty();
    }

    private boolean isValidFileName(String fileName) {
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
        return fileNameWithoutExtension.matches(APPLICATION_STUDENT_ID_PATTERN);
    }

    private boolean isValidFileExtension(MultipartFile file) {
        return Objects.equals(file.getContentType(), ZIP_CONTENT_TYPE) && Objects.requireNonNull(
                file.getOriginalFilename()).toLowerCase().endsWith(ZIP_EXTENSION);
    }

    private boolean isValidFileSize(MultipartFile file) {
        return file.getSize() <= MAX_FILE_SIZE;
    }
}
