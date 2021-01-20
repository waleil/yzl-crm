package cn.net.yzl.crm.controller.store;

import cn.net.yzl.crm.utils.FastdfsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/20 10:07
 */
@Controller
@Api(value = "仓储中心-下载", tags = {"仓储中心-下载"})
@RequestMapping("down")
public class DownImageInController {

    @Autowired
    private FastdfsUtils fastdfsUtils;

    @ApiOperation("下载图片")
    @GetMapping("v1/downImage")
    public void downImage(@RequestParam("imageUrl") String imageUrl, HttpServletResponse httpServletResponse) throws IOException {
        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = fastdfsUtils.download(imageUrl, null);
            String[] split = imageUrl.split("[.]");
            String[] splitPath = split[0].split("/");
            httpServletResponse.setContentType("image/" + split[split.length - 1]);
            httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" + splitPath[splitPath.length - 1]+"."+split[split.length - 1]);
            outputStream = httpServletResponse.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } finally {
            if (outputStream != null)
                outputStream.close();
            if (inputStream != null)
                inputStream.close();
        }
    }

}
