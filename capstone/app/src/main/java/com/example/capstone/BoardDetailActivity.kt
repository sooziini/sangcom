package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.adapter.ReplyAdapter
import com.example.capstone.dataclass.PostDetail
import com.example.capstone.dataclass.Reply
import com.example.capstone.dataclass.ReplyListList
import com.example.capstone.network.MasterApplication
import kotlinx.android.synthetic.main.activity_board_detail.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class BoardDetailActivity : AppCompatActivity() {

    private lateinit var board_id: String
    private lateinit var activity_num: String
    private lateinit var board_title: String
    private lateinit var board_body: String
    var detailLike = 0
    var detailScrap = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        // toolbar 설정
        setSupportActionBar(board_detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)       // 기본 뒤로가기 버튼 설정
        supportActionBar?.setDisplayShowTitleEnabled(false)     // 기본 title 제거

        // 성공적으로 intent 전달값을 받았을 경우
        if (intent.hasExtra("board_id")) {
            board_id = intent.getStringExtra("board_id")!!
            activity_num = intent.getStringExtra("activity_num")!!

            // 받은 board_id로 게시글 detail GET
            retrofitGetPostDetail(board_id)

            // 받은 board_id로 댓글 GET
            retrofitGetReplyList(board_id)
        } else {
            // intent 실패할 경우 현재 액티비티 종료
            finish()
        }

        // 좋아요 버튼 눌렀을 경우
        board_detail_like_btn.setOnClickListener {
            retrofitGoodPostClick()
        }

        // 스크랩 버튼 눌렀을 경우
        board_detail_scrap_btn.setOnClickListener {
            retrofitScrapPostClick()
        }

        // 댓글 등록 버튼을 클릭했을 경우
        board_detail_comment_btn.setOnClickListener {
            val body = board_detail_comment.text.toString()
            val reply = HashMap<String, String>()

            if (body == "") {
                toast("댓글을 입력해주세요")
            } else {
                reply.put("body", body)
                // 댓글 작성 POST
                retrofitCreateReply(board_id, reply)
            }
        }

    }

    // 받은 board_id로 게시글 detail GET하는 함수
    private fun retrofitGetPostDetail(board_id: String) {
        (application as MasterApplication).service.getPostDetail(board_id)
            .enqueue(object : Callback<PostDetail> {
                override fun onResponse(call: Call<PostDetail>, response: Response<PostDetail>) {
                    if (response.isSuccessful && response.body()!!.success == "true") {
                        val post = response.body()!!.data[0]
                        val postImg = response.body()!!.imagepath
                        board_title = post.title
                        board_body = post.body

                        board_detail_title.setText(post.title).toString()
                        board_detail_body.setText(post.body).toString()
                        board_detail_date.setText(post.regdate.substring(0, 16)).toString()
                        // board_detail_nickname.setText(post.user_id).toString()
                        board_detail_comment_cnt.setText(post.replyCount.toString()).toString()
                        board_detail_like_cnt.setText(post.goodCount.toString()).toString()
                        board_detail_scrap_cnt.setText(post.scrapCount.toString()).toString()

                        if (post.goodCheck == "N") detailLike = 0
                        else {
                            board_detail_like_btn.setImageResource(R.drawable.detail_like_selected)
                            detailLike = 1
                        }
                        if (post.scrapCheck == "N") detailScrap = 0
                        else {
                            board_detail_scrap_btn.setImageResource(R.drawable.detail_scrap_selected)
                            detailScrap = 1
                        }

                        // 사진이 있을 경우
                        if (postImg.size > 1) {

                        }

                    } else {
                        toast("게시글 조회 실패")
                    }
                }

                // 응답 실패 시
                override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                    toast("network error")
                    finish()
                }
            })
    }

    // 받은 board_id로 댓글 GET하는 함수
    private fun retrofitGetReplyList(board_id: String) {
        (application as MasterApplication).service.getReplyList(board_id)
            .enqueue(object : Callback<ReplyListList> {
                override fun onResponse(
                    call: Call<ReplyListList>,
                    response: Response<ReplyListList>
                ) {
                    if (response.isSuccessful && response.body()!!.success == "true") {
                        val replyList = response.body()?.data
                        var reply = ArrayList<Reply>()

                        if (replyList != null && replyList.size > 0) {
                            // 새로운 replyList 생성
                            for (i in 0 until replyList.size) {
                                reply.add(replyList[i].parent)
                                for (j in 0 until replyList[i].child.size)
                                    reply.add(replyList[i].child[j])
                            }

                            val adapter = ReplyAdapter(reply, LayoutInflater.from(this@BoardDetailActivity), this@BoardDetailActivity, menuInflater)
                            reply_recyclerview.adapter = adapter
                            reply_recyclerview.layoutManager = LinearLayoutManager(this@BoardDetailActivity)
                            reply_recyclerview.setHasFixedSize(true)
                        }
                    } else {
                        toast("댓글 조회 실패")
                    }
                }

                // 응답 실패 시
                override fun onFailure(call: Call<ReplyListList>, t: Throwable) {
                    toast("network error")
                    finish()
                }
            })
    }

    // 입력받은 댓글 POST하는 함수
    private fun retrofitCreateReply(board_id: String, body: HashMap<String, String>) {
        (application as MasterApplication).service.createReply(board_id, body)
            .enqueue(object : Callback<HashMap<String, String>> {
                override fun onResponse(
                    call: Call<HashMap<String, String>>,
                    response: Response<HashMap<String, String>>
                ) {
                    if (response.isSuccessful && response.body()!!.get("success") == "true") {
                        // 댓글 작성 성공
                        // 댓글 recyclerview 갱신해야 함

                    } else {
                        toast("댓글 작성 실패")
                    }
                }

                // 응답 실패 시
                override fun onFailure(call: Call<HashMap<String, String>>, t: Throwable) {
                    toast("network error")
                    // finish()
                }
            })
    }

    // 게시글 좋아요하는 함수
    private fun retrofitGoodPostClick() {
        (application as MasterApplication).service.goodPost(board_id)
            .enqueue(object : Callback<HashMap<String, String>>{
            override fun onResponse(
                call: Call<HashMap<String, String>>,
                response: Response<HashMap<String, String>>
            ) {
                if (response.isSuccessful && response.body()!!.get("success") == "true") {
                    // 안 누름 -> 누름
                    if (response.body()!!.get("stat") == "INSERT") {
                        board_detail_like_btn.setImageResource(R.drawable.detail_like_selected)
                        detailLike = 1
                    } else if (response.body()!!.get("stat") == "DELETE") {
                        // 누름 -> 안 누름
                        board_detail_like_btn.setImageResource(R.drawable.detail_like)
                        detailLike = 0
                    }
                } else {
                    toast("게시글 좋아요 실패")
                }
            }

            override fun onFailure(call: Call<HashMap<String, String>>, t: Throwable) {
                toast("network error")
                // finish()
            }
        })
    }

    // 게시글 스크랩하는 함수
    private fun retrofitScrapPostClick() {
        (application as MasterApplication).service.scrapPost(board_id)
            .enqueue(object : Callback<HashMap<String, String>> {
            override fun onResponse(
                call: Call<HashMap<String, String>>,
                response: Response<HashMap<String, String>>
            ) {
                if (response.isSuccessful && response.body()!!.get("success") == "true") {
                    // 안 누름 -> 누름
                    if (response.body()!!.get("stat") == "INSERT") {
                        board_detail_scrap_btn.setImageResource(R.drawable.detail_scrap_selected)
                        detailLike = 1
                    } else if (response.body()!!.get("stat") == "DELETE") {
                        // 누름 -> 안 누름
                        board_detail_scrap_btn.setImageResource(R.drawable.detail_scrap)
                        detailLike = 0
                    }
                } else {
                    toast("게시글 스크랩 실패")
                }
            }

            override fun onFailure(call: Call<HashMap<String, String>>, t: Throwable) {
                toast("network error")
                // finish()
            }
        })
    }

    // menu xml에서 설정한 menu를 붙임
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.board_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            // toolbar의 뒤로가기 버튼을 눌렀을 때
            android.R.id.home -> {
                when (activity_num) {
                    "0" -> startActivity(Intent(this, BoardActivity::class.java))
                    "1" -> startActivity(Intent(this, SearchActivity::class.java))
                    "2" -> startActivity(Intent(this, ScrapActivity::class.java))
                }
                finish()
                return true
            }
            R.id.board_detail_edit -> {
                val intent = Intent(this, BoardWriteActivity::class.java)
                intent.putExtra("board_write_id", board_id)     // 글 수정의 경우 board_id 전달
                intent.putExtra("board_write_title", board_title)
                intent.putExtra("board_write_body", board_body)
                startActivity(intent)
                finish()
                return true
            }
            // 삭제하기 버튼 클릭시 dialog 뜸
            R.id.board_detail_delete -> {
                // 현재 activity가 종료되었을 경우 dialog를 설정하지 않음
                if (!this.isFinishing)
                    setDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 게시글 삭제하기 버튼 눌렀을 때 뜨는 dialog 설정하는 함수
    private fun setDialog() {
        val builder = AlertDialog.Builder(this)
            .setCancelable(false)       // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않음
            .create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_board_delete, null)
        val deleteBtn = dialogView.findViewById<Button>(R.id.dialog_board_delete_btn)
        val cancelBtn = dialogView.findViewById<Button>(R.id.dialog_board_cancel_btn)

        // 삭제 버튼 눌렀을 때
        deleteBtn.setOnClickListener {
            (application as MasterApplication).service.deletePostDetail(board_id)
                .enqueue(object : Callback<HashMap<String, String>> {
                    override fun onResponse(
                        call: Call<HashMap<String, String>>,
                        response: Response<HashMap<String, String>>
                    ) {
                        if (response.isSuccessful && response.body()!!.get("success") == "true") {
                            startActivity(Intent(this@BoardDetailActivity, BoardActivity::class.java))
                            finish()
                        } else {
                            toast("게시글 삭제 실패")
                        }
                    }

                    // 응답 실패 시
                    override fun onFailure(call: Call<HashMap<String, String>>, t: Throwable) {
                        toast("network error")
                        finish()
                    }
                })
        }
        // 취소 버튼 눌렀을 때
        cancelBtn.setOnClickListener {
            builder.dismiss()
        }
        builder.setView(dialogView)
        builder.show()
    }
}