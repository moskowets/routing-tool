package p.moskovets;

import p.moskovets.routing.dataimport.ICHotelImporter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        new ICHotelImporter().run();
        System.out.println( "End of Application" );
    }
}
