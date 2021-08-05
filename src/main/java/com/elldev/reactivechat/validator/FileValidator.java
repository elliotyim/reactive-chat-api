package com.elldev.reactivechat.validator;

import com.elldev.reactivechat.constant.ErrorCode;
import com.elldev.reactivechat.constant.FileSize;
import com.elldev.reactivechat.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

public class FileValidator {
    public static void checkProfileImg(MultipartFile profileImgFile) throws BadRequestException {
        if (profileImgFile != null || !profileImgFile.isEmpty()) {
            if (!profileImgFile.getContentType().contains("image")) {
                throw new BadRequestException(ErrorCode.INVALID_PROFILE_FILE_PROVIDED, "Check the type of profile");
            }
            if (profileImgFile.getSize() > 10 * FileSize.MEGABYTE) {
                throw new BadRequestException(ErrorCode.IMAGE_SIZE_TOO_BIG, "Profile exceeded 10 MB");
            }
        }
    }
}
