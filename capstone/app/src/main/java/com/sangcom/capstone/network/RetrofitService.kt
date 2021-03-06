package com.sangcom.capstone.network

import com.sangcom.capstone.dataclass.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    // 게시글 목록 조회
    @GET("board/")
    fun getPostList(
        @Query("type") type: String
    ): Call<PostList>

    // 게시글 생성
    @Multipart
    @POST("board/")
    fun createPost(
        @Query("type") type: String,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part images: ArrayList<MultipartBody.Part>
    ): Call<HashMap<String, Any>>

    // 게시글 자세히보기
    @GET("board/{boardid}/")
    fun getPostDetail(
        @Path("boardid") board_id: String
    ): Call<PostDetail>

    // 게시글 검색
    @GET("board/search/")
    fun searchPostList(
        @Query("type") type: String,
        @Query("title") title: String
    ): Call<PostList>

    // 게시글 수정
    @Multipart
    @PUT("board/{boardid}")
    fun putPostDetail(
        @Path("boardid") board_id: String,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part images: ArrayList<MultipartBody.Part>
    ): Call<HashMap<String, String>>

    // 게시글 삭제
    @DELETE("board/{boardid}/")
    fun deletePostDetail(
        @Path("boardid") board_id: String
    ): Call<HashMap<String, String>>

    // 게시글 스크랩
    @GET("board/scrap/{boardid}")
    fun scrapPost(
        @Path("boardid") board_id: String
    ): Call<HashMap<String, String>>

    // 게시글 좋아요
    @GET("board/good/{boardid}")
    fun goodPost(
        @Path("boardid") board_id: String
    ): Call<HashMap<String, String>>

    // 게시글 신고
    @POST("board/report")
    fun reportPost(
        @Body params: HashMap<String, Any>
    ): Call<HashMap<String, String>>

    // 댓글 조회
    @GET("reply/{boardid}")
    fun getReplyList(
        @Path("boardid") board_id: String
    ): Call<ReplyListList>

    // 댓글 등록
    @POST("reply/{boardid}")
    @FormUrlEncoded
    fun createReply(
        @Path("boardid") board_id: String,
        @Field("body") body: String
    ): Call<ReplyChange>

    // 대댓글 등록
    @POST("reply/{boardid}/{replyid}")
    @FormUrlEncoded
    fun createReplyReply(
        @Path("boardid") board_id: String,
        @Path("replyid") reply_id: String,
        @Field("body") body: String
    ): Call<ReplyChange>

    // 댓글 삭제
    @DELETE("reply/{boardid}/{replyid}")
    fun deleteReply(
        @Path("boardid") board_id: String,
        @Path("replyid") reply_id: String
    ): Call<HashMap<String, String>>

    // 댓글 좋아요
    @GET("reply/good/{replyid}")
    fun goodReply(
        @Path("replyid") reply_id: String
    ): Call<HashMap<String, String>>

    // 댓글 신고
    @POST("reply/report")
    fun reportReply(
        @Body params: HashMap<String, Any>
    ): Call<HashMap<String, String>>

    // 스크랩한 게시글 목록 조회
    @GET("board/scrap")
    fun getScrapPostList(): Call<PostList>

    // 로그인
    @POST("user/login")
    fun login(
        @Body params: HashMap<String, String>
    ): Call<HashMap<String, Any>>

    // 회원가입
    @POST ("user/register")
    fun signUp(
        @Body params: HashMap<String, Any>
    ): Call<HashMap<String, String>>

    // 아이디 중복 확인
    @POST("user/confirm/name")
    fun confirmId(
        @Body params: HashMap<String, String>
    ): Call<HashMap<String, String>>

    // 학번 인증
    @POST("user/auth/student/check")
    fun authStudent(
        @Body params: HashMap<String, String>
    ): Call<HashMap<String, String>>

    // 로그아웃
    @POST("user/logout")
    fun logout(): Call<HashMap<String, String>>

    // 토큰 검증 (회원 데이터 조회)
    @GET("auth/valid")
    fun authorization():Call<HashMap<String, Any>>

    // 토큰 재발급
    @FormUrlEncoded
    @POST("auth/refresh")
    fun setRefreshToken(
        @Field("token") token: String
    ): Call<HashMap<String, String>>

    // 비밀번호 찾기
    @POST("user/password/find")
    fun findPassword(
        @Body params: HashMap<String, String>
    ):Call<HashMap<String, String>>

    // 식단표 받아오기
    @GET("school/cafeteria?")
    fun loadMeal(
        @Query ("MLSV_FROM_YMD") start: String,
        @Query ("MLSV_TO_YMD") end: String
    ): Call<HashMap<String, Any>>

    // 주차별 식단표
    @GET("school/cafeteria/month?")
    fun weekMeal(
        @Query ("MLSV_FROM_YMD") start: String,
        @Query ("MLSV_TO_YMD") end: String
    ): Call<HashMap<String, Any>>

    // 비밀번호 확인
    @POST("user/password/check")
    fun checkPassword(
        @Body params: HashMap<String, String>
    ): Call<HashMap<String, String>>

    // 비밀번호 변경
    @POST("user/password/change")
    fun changePassword(
        @Body params: HashMap<String, String>
    ): Call<HashMap<String, String>>

    // 회원탈퇴
    @DELETE("user/quit")
    fun deleteUser():Call<HashMap<String, String>>

    // 본인 정보 조회
    @GET("user/info")
    fun readInfo():Call<HashMap<String, Any>>

    // 본인 정보 수정
    @PUT("user/info")
    fun updateInfo(
        @Body params: HashMap<String, String>
    ): Call<HashMap<String, Any>>

    // 프로필 사진 설정
    @Multipart
    @POST("user/profile")
    fun createUserProfile(
        @Part profile: MultipartBody.Part
    ): Call<HashMap<String, String>>

    // 프로필 사진 조회
    @GET("user/profile")
    fun getUserProfile(
        @Query("id") user_id: String
    ): Call<HashMap<String, String>>

    // 프로필 사진 삭제
    @DELETE("user/profile")
    fun deleteUserProfile(): Call<HashMap<String, String>>

    // 자신이 신고한 게시글 조회
    @GET ("board/report/me")
    fun userLoadBoardReport(): Call<HashMap<String, Any>>

    // 자신이 신고한 댓글 조회
    @GET ("reply/report/me")
    fun userLoadReplyReport(): Call<HashMap<String, Any>>

    // TodoList 날짜별 불러오기
    @GET("school/todo?")
    fun readTodo(
        @Query ("year") year: Int,
        @Query ("month") month: Int,
        @Query ("day") day: Int
    ): Call<HashMap<String, Any>>

    // TodoList 등록
    @POST("school/todo")
    fun createTodo(
        @Body params: HashMap<String, Any>
    ): Call<HashMap<String, String>>

    // TodoList 수정
    @PUT ("school/todo/{list_id}")
    fun updateTodo(
        @Path ("list_id") list_id: Int,
        @Body params: HashMap<String, String>
    ): Call<HashMap<String, String>>

    // TodoList 삭제
    @DELETE ("school/todo/{list_id}")
    fun deleteTodo(
        @Path ("list_id") list_id: Int
    ):Call<HashMap<String, String>>

    // TodoList 체크
    @GET ("school/todo/{list_id}")
    fun checkTodo(
        @Path ("list_id") list_id: Int
    ): Call<HashMap<String, String>>

    // 시간표 등록 / 수정
    @POST ("school/timetable")
    fun updateTimeTable(
        @Body params: HashMap<String, ArrayList<HashMap<String, Any?>>>
    ): Call<HashMap<String, String>>

    // 시간표 조회
    @GET ("school/timetable")
    fun readTimeTable(): Call<HashMap<String, Any>>

    // 시간표 삭제
    @HTTP (method = "DELETE", path = "school/timetable", hasBody = true)
    fun deleteTimeTable(
        @Body params: HashMap<String, ArrayList<HashMap<String, Any>>>
    ): Call<HashMap<String, String>>

    // master 학생정보 파일 등록
    @Multipart
    @POST ("user/auth/student")
    fun uploadAuthFile(
        @Part file: MultipartBody.Part
    ): Call<HashMap<String, String>>

    // master 학생조회
    @GET ("user/auth/master/student")
    fun loadStudent(): Call<HashMap<String, Any>>

    // master role 변경
    @PUT ("user/update/role")
    fun changeRole(
        @Body params: HashMap<String, String>
    ): Call<HashMap<String, String>>

    // master 게시판 신고 조회
    @GET ("board/report/{page_num}")
    fun readBoardReport(
        @Path ("page_num") page_num: Int
    ): Call<HashMap<String, Any>>

    // master 댓글 신고 조회
    @GET ("reply/report/{page_num}")
    fun readReplyReport(
        @Path ("page_num") page_num: Int
    ): Call<HashMap<String, Any>>

    // master 댓글 신고받은 횟수 조회
    @GET ("reply/report/count")
    fun readReplyReportCount(): Call<HashMap<String, Any>>

    // master 게시판 신고받은 횟수 조회
    @GET ("board/report/count")
    fun readBoardReportCount(): Call<HashMap<String, Any>>

    // master 게시판 신고내용 조회
    @GET ("board/report?")
    fun masterLoadBoardReport(
        @Query ("id") id: String
    ): Call<HashMap<String, Any>>

    // master 댓글 신고내용 조회
    @GET ("reply/report?")
    fun masterLoadReplyReport(
        @Query ("id") id: String
    ): Call<HashMap<String, Any>>

    // 알림 장치 등록
    @FormUrlEncoded
    @POST("device/")
    fun setDeviceToken(
        @Field("deviceToken") token: String
    ): Call<HashMap<String, String>>

    @DELETE("device/")
    fun deleteDeviceToken(): Call<HashMap<String, String>>

    // 알림 추가
    @POST("notice/")
    fun createNotification(
        @Body noti: HashMap<String, ArrayList<NotiPost>>
    ): Call<HashMap<String, String>>

    // 알림 조회
    @GET("notice/")
    fun getNotification(): Call<NotiList>
}