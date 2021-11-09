package de.jupiterpi.wiesen.website.pages;

import de.jupiterpi.wiesen.website.analytics.Visit;
import jupiterpi.tools.files.TextFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("")
public class Controller {
    Loader loader = new Loader();

    // ping
    @GetMapping("/ping")
    public String ping() {
        return "Server is on and responsive. ";
    }

    /* ----- pages ----- */

    @GetMapping(value = {"", "/home"})
    public String homePage() {
        return pageLink("oth", "home");
    }

    private static final String MOBILE_COOKIE_NAME = "allow_mobile";
    private static final String MOBILE_COOKIE_REDIRECT = "redirect:";
    private static final String MOBILE_COOKIE_ALLOW = "allow";

    private static final String PROFILING_ID_COOKIE_NAME = "profiling_id";

    @GetMapping(value = {"/oth/{page}", "/bw/{page}", "/mw/{page}"})
    public String categorizedPage(@PathVariable String page, HttpServletRequest request, HttpServletResponse response) throws TextFile.DoesNotExistException {
        String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        String[] parts = path.split("/");
        String category = parts[parts.length-2];

        String userAgentHeader = request.getHeader("User-Agent");
        boolean isMobile = userAgentHeader.contains("Mobile");
        if (isMobile) {
            boolean mobileAllowed = false;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(MOBILE_COOKIE_NAME) && cookie.getValue().equals(MOBILE_COOKIE_ALLOW)) mobileAllowed = true;
                }
            }
            if (!mobileAllowed) {
                Cookie cookie = new Cookie(MOBILE_COOKIE_NAME, MOBILE_COOKIE_REDIRECT + category + "/" + page);
                cookie.setPath("/");
                response.addCookie(cookie);
                return loader.loadInternalPage("mobile-warning.html");
            }
        }

        boolean isNew = false;
        String profilingId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(PROFILING_ID_COOKIE_NAME)) {
                    profilingId = cookie.getValue();
                }
            }
        }
        if (profilingId == null) {
            isNew = true;
            profilingId = UUID.randomUUID().toString();
            Cookie profilingCookie = new Cookie(PROFILING_ID_COOKIE_NAME, profilingId);
            profilingCookie.setMaxAge(356 * 24 * 60 * 60); // 356d
            profilingCookie.setPath("/");
            response.addCookie(profilingCookie);
        }
        String pageStr = category + "/" + page;
        Visit.createVisit(profilingId, pageStr, isMobile, isNew);
        System.out.printf("site access: page '%s' by %s%n%s", pageStr, profilingId, isMobile ? " FROM MOBILE ==========================================" : "");

        return page(category, page).replaceAll("\"&firstVisit\"", Boolean.toString(isNew));
    }

    @GetMapping("/allowMobile")
    public String allowMobile(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = null;
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals(MOBILE_COOKIE_NAME) && c.getValue().startsWith(MOBILE_COOKIE_REDIRECT)) cookie = c;
        }
        if (cookie != null) {
            String redirectString = cookie.getValue().split(MOBILE_COOKIE_REDIRECT.substring(MOBILE_COOKIE_REDIRECT.length() - 1))[1];
            String[] redirectStringParts = redirectString.split("/");
            String category = redirectStringParts[0];
            String page = redirectStringParts[1];
            Cookie newCookie = new Cookie(MOBILE_COOKIE_NAME, MOBILE_COOKIE_ALLOW);
            newCookie.setPath("/");
            newCookie.setMaxAge(2 * 24*60*60);
            response.addCookie(newCookie);
            return pageLink(category, page);
        }
        return "Something went very horribly wrong! Please consult the technical support at wiesen.jupiterpi.de/oth/kontakt or try again later :)";
    }

    private String page(String category, String pageName) {
        return loader.loadPage(category, pageName);
    }
    private String pageLink(String category, String pageName) {
        return "<script> window.location.href = '/" + category + "/" + pageName + "'; </script>";
    }

    /* ----- resources ----- */

    // text
    @GetMapping("/res/{filename}")
    public String getResource(@PathVariable String filename) {
        return loader.getResource(filename);
    }

    // pic redirects
    @GetMapping("/pic/{filename}")
    public RedirectView getPicRedirect(@PathVariable String filename) {
        return new RedirectView("https://wiesen-website.s3.eu-central-1.amazonaws.com/pic/" + filename);
    }

    // postcard pics redirects
    @GetMapping("/postcards/{filename}")
    public RedirectView getPostcardsPicRedirect(@PathVariable String filename) {
        return new RedirectView("https://wiesen-website.s3.eu-central-1.amazonaws.com/postcards/" + filename);
    }

    // header-pic
    @GetMapping("/header-pic/{name}")
    public RedirectView getHeaderPicRedirect(@PathVariable String name) {
        return new RedirectView("https://wiesen-website.s3.eu-central-1.amazonaws.com/header-pic/" + name);
    }
}
