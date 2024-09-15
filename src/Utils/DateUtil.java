package Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {

    public DateUtil() {
    }

    public static List<LocalDate> dateListRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateListRange = new ArrayList<>();
        for (LocalDate dateTest = startDate; !dateTest.isAfter(endDate); dateTest = dateTest.plusDays(1)) {
            dateListRange.add(dateTest);
//            System.out.println("Dates dans la plage : " + dateListRange);
        }
        return dateListRange;
    }

    public static boolean isDateAvailable(LocalDate startCons, LocalDate endCons, LocalDate startPeriod, LocalDate endPeriod) {
        return !startCons.isAfter(endPeriod) && !endCons.isBefore(startPeriod);
    }
}

//    public static boolean isDateAvailable(LocalDate startDate, LocalDate endDate, List<LocalDate> dates) {
//        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
//            if (!dates.contains(date)) {
//                return false;
//            }
//        }
//        return true;
//    }
