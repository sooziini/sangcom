package com.sangcom.capstone.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.sangcom.capstone.R
import com.sangcom.capstone.adapter.BoardAdapter
import com.sangcom.capstone.dataclass.PostList
import com.sangcom.capstone.main.MainActivity
import com.sangcom.capstone.network.MasterApplication
import kotlinx.android.synthetic.main.activity_scrap.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScrapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrap)

        // toolbar 설정
        setSupportActionBar(scrap_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)       // 기본 뒤로가기 버튼 설정
        supportActionBar?.setDisplayShowTitleEnabled(false)     // 기본 title 제거

        retrofitGetScrapPostList()
    }

    private fun retrofitGetScrapPostList() {
        (application as MasterApplication).service.getScrapPostList()
            .enqueue(object : Callback<PostList> {
                override fun onResponse(call: Call<PostList>, response: Response<PostList>) {
                    // 응답 성공 시
                    if (response.isSuccessful && response.body()!!.success == "true") {
                        val postList = response.body()!!.data

                        // 스크랩 게시판 글 목록 화면 뷰 작성
                        // item 클릭 시 board_id 넘겨줌 + detail 화면으로 전환
                        val adapter = BoardAdapter(postList, LayoutInflater.from(this@ScrapActivity)) { post ->
                            val intent = Intent(this@ScrapActivity, BoardDetailActivity::class.java)
                            intent.putExtra("board_id", post.board_id.toString())
                            intent.putExtra("activity_num", "2")
                            startActivity(intent)
                            finish()
                        }
                        scrap_recyclerview.adapter = adapter
                        scrap_recyclerview.layoutManager = LinearLayoutManager(this@ScrapActivity)
                        scrap_recyclerview.setHasFixedSize(true)
                    } else {
                        toast("스크랩 게시글 목록을 조회할 수 없습니다")
                        finish()
                    }
                }

                override fun onFailure(call: Call<PostList>, t: Throwable) {
                    toast("network error")
                    finish()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // toolbar의 뒤로가기 버튼을 눌렀을 때
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}