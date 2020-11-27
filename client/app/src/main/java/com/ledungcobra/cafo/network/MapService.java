package com.ledungcobra.cafo.network;

import com.ledungcobra.cafo.models.map.PositionMap;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapService {
    @GET("/")
    Call<List<PositionMap>> getPositions(@Query("search") String searchTerm);
    
}
