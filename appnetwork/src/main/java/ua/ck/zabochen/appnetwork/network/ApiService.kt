package ua.ck.zabochen.appnetwork.network

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*
import ua.ck.zabochen.appnetwork.network.model.Note
import ua.ck.zabochen.appnetwork.network.model.User

interface ApiService {

    // Register user
    @POST("notes/user/register")
    fun registerUser(@Field("device_id") deviseId: String): Single<User>

    // Create note
    @POST("notes/new")
    fun createNote(@Field("note") note: String): Single<Note>

    // Get all notes
    @GET("notes/all")
    fun getAllNotes(): Single<List<Note>>

    // Update note
    @PUT("notes/{id}")
    fun updateNote(@Path("id") noteId: Int, @Field("note") note: String): Completable

    // Delete note
    @DELETE("notes/{id}")
    fun deleteNote(@Path("id") noteId: Int)

}