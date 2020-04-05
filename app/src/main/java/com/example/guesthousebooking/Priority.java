package com.example.guesthousebooking;
import java.text.SimpleDateFormat;
import java.util.*;

public class Priority {

    public static void main(String[] args)
    {

        Priority O = new Priority();
        List<Booking> List = new ArrayList<Booking>();
        Scanner sc = new Scanner(System.in);

        for(int i = 0; i < 6; i++)
        {
            Booking B = new Booking();
            B.setBookingId(i);
            String type = sc.nextLine();
            B.setType(type);
            String date = sc.nextLine();
            B.setBookingDate(date);
            List.add(B);

        }
        System.out.println("Enter Sort By : ");
        int type = sc.nextInt();
        PriorityQueue<Booking> PQueue = O.priorTize(11, List, type);
        List.clear();
        while (!PQueue.isEmpty())
        {
            List.add(PQueue.poll());
        }
        for(Booking B : List)
            System.out.println(B + "\n");
    }

    public PriorityQueue<Booking> priorTize(int capacity, List<Booking> List, int type)
    {
        PriorityQueue<Booking> PQueue;
        if(type == 1)
        {
            PQueue = new PriorityQueue<Booking>(capacity, new SortByUserType());
            for (Booking B : List)
            {
                PQueue.add(B);
            }

        }
        else if(type == 2)
        {
            PQueue = new PriorityQueue<Booking>(capacity, new SortByBookingDate());
            for(Booking B: List)
                PQueue.add(B);

        }

        else
        {
            PQueue = new PriorityQueue<Booking>(capacity, new SortByPriorityAlgorithm());
            for(Booking B: List)
                PQueue.add(B);
        }
        return PQueue;

    }


}


class SortByUserType implements Comparator<Booking> {


    @Override
    public int compare(Booking b1, Booking b2) {

        String type1 = b1.getType();
        String type2 = b2.getType();
        int P1, P2;
        if(type1.equals("d"))
            P1 = 6;
        else if(type1.equals("r"))
            P1 = 5;
        else if(type1.equals("f"))
            P1 = 4;
        else if(type1.equals("n"))
            P1 = 3;
        else if(type1.equals("c"))
            P1 = 2;
        else
            P1 = 1;

        if(type2.equals("d"))
            P2 = 6;
        else if(type2.equals("r"))
            P2 = 5;
        else if(type2.equals("f"))
            P2 = 4;
        else if(type2.equals("n"))
            P2 = 3;
        else if(type2.equals("c"))
            P2 = 2;
        else
            P2 = 1;


        if(P1 < P2)
        {
            return 1;
        }
        else if(P1 > P2)
        {
            return -1;
        }

        return 0;
    }

}

class SortByBookingDate implements Comparator<Booking> {


    @Override
    public int compare(Booking b1, Booking b2) {



        String d1 = b1.getBookingDate();
        String d2 = b2.getBookingDate();
        Date date1 = new Date();
        Date date2 = new Date();

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            date1 = (Date) formatter.parse(d1);
            date2 = (Date) formatter.parse(d2);

            if(date2.before(date1))
            {
                return 1;
            }
            else if(date2.after(date1))
            {
                return -1;
            }
            return 0;


        }
        catch (Exception e)
        {
            System.out.println("Error : " + e);
        }


        return 0;
    }

}

class SortByPriorityAlgorithm implements Comparator<Booking> {
    @Override
    public int compare(Booking b1, Booking b2) {

        String type1 = b1.getType();
        String type2 = b2.getType();
        int P1 = 0, P2 = 0;

        if (type1.equals("d"))
            P1 = 6;
        else if (type1.equals("r"))
            P1 = 5;
        else if (type1.equals("f"))
            P1 = 4;
        else if (type1.equals("n"))
            P1 = 3;
        else if (type1.equals("c"))
            P1 = 2;
        else
            P1 = 1;

        if (type2.equals("d"))
            P2 = 6;
        else if (type2.equals("r"))
            P2 = 5;
        else if (type2.equals("f"))
            P2 = 4;
        else if (type2.equals("n"))
            P2 = 3;
        else if (type2.equals("c"))
            P2 = 2;
        else
            P2 = 1;


        String d1 = b1.getBookingDate();
        String d2 = b2.getBookingDate();
        Date date1 = new Date();
        Date date2 = new Date();
        int gap = 0;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
            date1 = (Date) formatter.parse(d1);
            date2 = (Date) formatter.parse(d2);

            while (date1.before(date2)) {
                gap++;
                Calendar c = Calendar.getInstance();
                c.setTime(date1);
                c.add(Calendar.DAY_OF_MONTH, 1);
                date1 = c.getTime();

            }

            if (P2 > P1) {
                if (gap > 5)
                    return -1;
                else
                    return 1;
            } else if (P2 < P1) {
                return -1;
            }


            /*else
            {
	            while (date1.before(date2)){
	            	gap++;
	                Calendar c = Calendar.getInstance();
	                c.setTime(date1);
	                c.add(Calendar.DAY_OF_MONTH, 1);
	                date1 = c.getTime();

	            }
	            if(gap > 5)
	            	return 1;
	            else
	            	return -1;
            }*/
        } catch (Exception e) {
            System.out.println(e);
        }

        /*if(P1 == 6 && P2 == 5)
        {
        	if(gap > 5)
        		return 1;
        	else
        		return -1;
        }

        if(P1 == 6 && P2 == 4)
        {
        	if(gap > 10)
        		return 1;
        	else
        		return -1;
        }

        if(P1 == 6 && P2 == 3)
        {
        	if(gap > 20)
        		return 1;
        	else
        		return -1;
        }

        if(P1 == 5 && P2 == 4)
        {
        	if(gap > 10)
        		return 1;
        	else
        		return -1;
        }

        if(P1 == 5 && P2 == 3)
        {
        	if(gap > 15)
        		return 1;
        	else
        		return -1;
        }

        if(P1 == 4 && P2 == 3)
        {
        	if(gap > 5)
        		return 1;
        	else
        		return -1;
        }

        if(P1 == 4 && P2 == 2)
        {
        	if(gap > 10)
        		return 1;
        	else
        		return -1;
        }

        if(P1 == 3 && P2 == 2)
        {
        	if(gap > 5)
        		return 1;
        	else
        		return -1;
        }

        if(P1 == 3 && P2 == 1)
        {
        	if(gap > 10)
        		return 1;
        	else
        		return -1;
        }

        if(P1 == 2 && P2 == 1)
        {

        	if(gap > 5)
        		return 1;
        	else
        		return -1;
        }*/

        return 0;

    }

}