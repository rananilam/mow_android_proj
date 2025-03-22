package iCode.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternHelper {

    private static PatternHelper patternHelper = null;

    public static PatternHelper getInstance()
    {
        if(patternHelper == null)
            patternHelper = new PatternHelper();

        return patternHelper;
    }

    public boolean isValidPattern(String regex, String text, boolean isCaseInsensitive)
    {
        Pattern pattern = null;

        if(isCaseInsensitive)
            pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        else
            pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
}
