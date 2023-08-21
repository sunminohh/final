package kr.co.mgv.support.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

@Component
public class SupportFileDownloadView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String directory = (String) model.get("directory");
        String saveName = (String) model.get("saveName");
        String originalName = (String) model.get("originalName");

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(originalName, "utf-8"));

        directory = new ClassPathResource(directory).getFile().getAbsolutePath().replace("target\\classes", "src\\main\\resources");
        InputStream in = new FileInputStream(new File(directory, saveName));
        
        OutputStream out = response.getOutputStream();

        FileCopyUtils.copy(in, out);

    }
}
