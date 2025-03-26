import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;
import java.io.File;
import java.util.TimeZone;

class HTTPRequestObject {
    private String command;
    private String path;
    private String ifModifiedSince;
    private int statusCode = 200;
    private final String rootDirectory;

    public HTTPRequestObject(String requestLine, Map<String, String> headers, String rootDirectory) throws IOException {
        this.rootDirectory = rootDirectory;

        String[] parts = requestLine.split(" ");
        if (parts.length < 2) {
            statusCode = 400; //Bad request
            return;
        }

        command = parts[0];
        path = parts[1];

        if (!("GET".equalsIgnoreCase(command) || "HEAD".equalsIgnoreCase(command))) {
            statusCode = 501; // Not Implemented
        }

        ifModifiedSince = headers.getOrDefault("If-Modified-Since", null);
        //Validate If-Modified-Since:
        if (ifModifiedSince != null) {
            try {
                SimpleDateFormat HTTPDateFormat = new SimpleDateFormat("EEE MMM d hh:mm:ss zzz yyyy");
                HTTPDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date parsedDate = HTTPDateFormat.parse(ifModifiedSince);
            } catch (ParseException e) {
                statusCode = 400; //Bad request
            }
        }

        //Process path: truncate or append index.html
        try {
            File rootDir = new File(rootDirectory).getCanonicalFile();
            File requestedFile = new File(rootDir, path).getCanonicalFile();

            if (!requestedFile.getCanonicalPath().startsWith(rootDir.getCanonicalPath())) {
            statusCode = 400; //Bad request
            } else {
                if (requestedFile.isDirectory()){
                    requestedFile = new File(requestedFile, "index.html");
                }
                path = requestedFile.getPath();
            }
        } catch (IOException e) {
            statusCode = 400; //Bad request
        }
    }


    public String getCommand() {
        return command;
    }

    public String getPath() {
        return path;
    }

    public String getIfModifiedSince() {
        return ifModifiedSince;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
