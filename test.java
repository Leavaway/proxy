import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String args[]) {
//        String str = "GET / HTTP/1.1";
//        String pattern = "GET (.*?) HTTP";
//
//        Pattern r = Pattern.compile(pattern);
//        Matcher m = r.matcher(str);
//        System.out.println(m.matches());
        StringBuilder checkHTML = new StringBuilder();
        checkHTML.append("a");
        checkHTML.append("a");
        checkHTML.append("a");
        checkHTML.append("a");
        checkHTML.append("b");
        checkHTML.append("a");
        checkHTML.append("a");
        checkHTML.append("a");
        System.out.println(checkHTML.indexOf("aac"));

    }

}