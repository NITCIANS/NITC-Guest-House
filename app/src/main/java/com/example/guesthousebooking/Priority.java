package com.example.guesthousebooking;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

public class Priority {

    public static void main(String[] args)
    {

        Priority O = new Priority();
        O.priorTize(10);

    }

    public void priorTize(int capacity)
    {
        PriorityQueue<Booking> PQueue = new PriorityQueue<Booking>(capacity, new BookingComparator());
        List<Booking> List = new ArrayList<Booking>();

        for (Booking B : List)
        {
            PQueue.add(B);
        }
    }


}


class BookingComparator implements Comparator<Booking> {


    @Override
    public int compare(Booking b1, Booking b2) {

        String type1 = b1.getType();
        String type2 = b2.getType();
        String status1 = b1.getBookingStatus();
        String status2 = b2.getBookingStatus();
        String d1 = b1.getBookingDate();
        String d2 = b2.getBookingDate();
        Date date1 = new Date();
        Date date2 = new Date();


        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
            date1 = (Date) formatter.parse(d1);
            date2 = (Date) formatter.parse(d2);
        }
        catch (Exception e)
        {

        }

        if (type1.equals("Director") && date1.before(date2))
            return 1;
        if(type1.equals("Registrar") && date1.before(date2))
            return 1;
        if(type1.equals("Faculty") && date1.before(date2))
            return 1;
        if(type1.equals("Staff") && date1.before(date2))
            return 1;
        if(type1.equals("SAC Member") && date1.before(date2))
            return 1;
        else if(date1.before(date2))
            return 1;
        else
            return -1;
    }

}