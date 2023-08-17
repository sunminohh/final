package kr.co.mgv.web.view;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

@Component("downloadFileView")
public class DownloadView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = (String) model.get("url");
        String filename = (String) model.get("filename");

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));

        InputStream in = new URL(url).openStream();
        OutputStream out = response.getOutputStream();

        FileCopyUtils.copy(in, out);

    }
}
