import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static void DocumentParsing() throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;

        for (Element name: names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "       Погодные явления                                            Температура" +
                    " Давление    Влажность   Ветер");
            int iterationCount = printPartValues(values, index);
            System.out.println();
            index = index + iterationCount;
        }

    }

    private static Document getPage() throws IOException {
        String url = "http://pogoda.spb.ru/";
        return Jsoup.parse(new URL(url), 5000);
    }

    private static final Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString (String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()){
            return  matcher.group();
        }
        throw new Exception ("Can't extract date from string!");
    }

    private static int printPartValues(Elements values, int index){
        int iterationCount = 4;
        if (index == 0) {
            String firstEl = values.get(0).select("td").first().text();
            switch (firstEl) {
                case "Ночь":
                    iterationCount = 5;
                    break;
                case "День":
                    iterationCount = 3;
                case "Вечер":
                    iterationCount = 2;
            }
        }

        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = values.get(index + i);
            int j = 1;

            for (Element td : valueLine.select("td")) {
                if (j==2) {
                    System.out.printf("%-60.60s", td.text());
                } else
                    System.out.printf("%-12.12s", td.text());
                j++;
            }

            System.out.println();
        }

        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        DocumentParsing();
    }
}
