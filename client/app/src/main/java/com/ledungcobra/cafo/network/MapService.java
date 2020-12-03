package com.ledungcobra.cafo.network;

import com.ledungcobra.cafo.models.routing.Routing;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//https://api.geoapify.com/v1/routing?waypoints=11.2240687,106.6674681|11.3397343,106.75436033775114&mode=drive&apiKey=e39be72fa4a1417db83845d0c9e238ce
public interface MapService {
   @GET("v1/routing")
    Call<Routing> getRoute(@Query(value = "waypoints",encoded = true) String wayPoints, @Query("mode") String mode, @Query("apiKey") String apiKey) ;
    
}
