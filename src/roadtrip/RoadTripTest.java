package roadtrip;

import java.io.FileNotFoundException;

/**
   Program to perform basic testing of the RoadTrip class.
   Comments include expected output produced by method calls.
*/
public class RoadTripTest
{     
   public static void main (String[] args) throws FileNotFoundException
   {
      RoadTrip roads = new RoadTrip("contiguous-usa.txt");

      System.out.println(roads.getPath("LA", "TX"));
      /*
         There is a path from LA to TX
         The shortest path involves crossing 1 borders
         Path: [LA -> TX]
      */
      
      System.out.println(roads.getPath("LA", "FL"));
      /*
         There is a path from LA to FL
         The shortest path involves crossing 3 borders
         Path: [LA -> MS -> AL -> FL]
      */
      
      System.out.println(roads.getPath("LA", "OR"));
      /*
         There is a path from LA to OR
         The shortest path involves crossing 5 borders
         Path: [LA -> TX -> NM -> AZ -> CA -> OR]
      */
    }
}
