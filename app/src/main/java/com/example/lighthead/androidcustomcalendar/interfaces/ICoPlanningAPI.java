package com.example.lighthead.androidcustomcalendar.interfaces;

import com.example.lighthead.androidcustomcalendar.helpers.Credentials;
import com.example.lighthead.androidcustomcalendar.helpers.CurrentUserTask;
import com.example.lighthead.androidcustomcalendar.helpers.SetTaskListParams;
import com.example.lighthead.androidcustomcalendar.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ICoPlanningAPI {

    @GET("/api/getAllUsers")
    Call<List<User>> usersList();

    @GET("/api/getUserById/{id}")
    Call<User> getUser(@Path("id") String id);

    @POST("/api/extLogin")
    Call<User> login(@Query("username") String username, @Query("password") String password);

    @POST("/api/extRegister")
    Call<User> register(@Body Credentials credentials);
	
	@POST("/api/extSetUserTaskList")
	Call<User> setUserTaskList(@Body SetTaskListParams params);

    @GET("/api/extGetUserTaskList")
    Call<User> getUserTaskList(@Query("username") String username, @Query("dateFrom") String dateFrom,
                               @Query("timeFrom") String timeFrom, @Query("dateTo") String dateTo,
                               @Query("timeTo") String timeTo);

    //@GET("/api/extGetUserUnavailableTime")


    @POST("/api/extAddTask")
    Call<String> addTask(@Body CurrentUserTask params);

    @POST("/api/extDeleteTask")
    Call<String> deleteTask(@Query("username") String username, @Query("taskId") int taskId);

    @POST("/api/extEditTask")
    Call<String> editTask(@Body CurrentUserTask params);
}
