import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Main {

    private static final Map<String, String> periodMap = Map.of(
            "PM", "PRE_MATCH",
            "H1", "FIRST_HALF",
            "H2", "SECOND_HALF",
            "HT", "HALF_TIME",
            "FT", "FULL_TIME");

    public static void main(String[] args) {
        try {
            List<String> stringList = Files.readAllLines(Paths.get(args[0]));
            for (String line: stringList) {
                convert(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void convert(String line){
        String periodAbbreviation = line.substring(1,3);
        String period = periodMap.get(periodAbbreviation);
        int minuteSeparator;
        if (period != null && line.length() > 4) {
            minuteSeparator = line.indexOf(':');
            String minute = line.substring(5, minuteSeparator);
            String second = line.substring(minuteSeparator + 1);

            int minuteValue;
            double secondValue;

            if (second.startsWith("0")){
                second = second.substring(1);
            }

            secondValue = Math.round(Double.parseDouble(second));
            minuteValue = Integer.parseInt(minute);

            int roundedSecond = (int) secondValue;

            second = String.valueOf(roundedSecond);

            if (secondValue == 60) {
                minuteValue += 1;
                second = "00";
            }

            if (secondValue < 10) {
                second = "0" + roundedSecond;
            }

            minute = String.valueOf(minuteValue);

            if (minuteValue < 10) {
                minute = "0" + minuteValue;
            }

            if (minuteValue < 0) {
                System.out.println("INVALID");
                return;
            }

            if (periodAbbreviation.equals("H1") && minuteValue >= 45) {
                addExtraTime(45, minuteValue, roundedSecond, period);
                return;
            }

            if (periodAbbreviation.equals("H2") && minuteValue >= 90) {
                addExtraTime(90, minuteValue, roundedSecond, period);
                return;
            }

            System.out.println(minute + ":" + second + " - " + period);

        } else {
            System.out.println("INVALID");
        }
    }

    private static void addExtraTime(int maxMinute, int minuteValue, int roundedSecondValue, String period) {
        if (minuteValue >= maxMinute) {
            int extraMinutesValue = minuteValue - maxMinute;
            String roundedSecond = String.valueOf(roundedSecondValue);
            String extraMinutes = String.valueOf(extraMinutesValue);
            if (extraMinutesValue < 10) {
                extraMinutes = "0" + extraMinutesValue;
            }
            if (roundedSecondValue < 10) {
                roundedSecond = "0" + roundedSecondValue;
            }
            System.out.println(maxMinute + ":" + "00 +" + extraMinutes + ":" + roundedSecond + " - " + period);
        }
    }

}