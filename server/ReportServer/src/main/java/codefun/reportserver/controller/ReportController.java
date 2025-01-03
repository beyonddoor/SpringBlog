package codefun.reportserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("report")
public class ReportController {

    @PostMapping("touch")
    public void reportTouch(HttpServletRequest request, @RequestBody String postBody) throws IOException {
        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            var name = headerNames.nextElement();
            log.info("header {}: {}", name, request.getHeader(name));
        }

        /**
         * playerId,width,height\n
         * x,y,timestamp;
         */
        String decodedString = null;
        try {
            decodedString = URLDecoder.decode(postBody, "UTF-8");
            log.info("report {}", decodedString);
        } catch (UnsupportedEncodingException e) {
            log.error("error format", e);
            throw new RuntimeException(e);
        }

        if (decodedString == null) {
            log.error("error format null");
            return;
        }

        var pos = decodedString.indexOf(",");
        if (pos <= 0) {
            log.error("error format, no comma found");
            return;
        }

        var playerId = Long.parseLong(decodedString.substring(0, pos));
        var filename = String.format("%d-%d.txt", playerId, new Date().getTime());
        Files.writeString(Path.of(filename), decodedString);
        log.info("write {} #{}", filename, decodedString.length());
    }
}
