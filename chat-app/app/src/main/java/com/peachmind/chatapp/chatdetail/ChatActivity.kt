package com.peachmind.chatapp.chatdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.peachmind.chatapp.Key.Companion.DB_CHATS
import com.peachmind.chatapp.Key.Companion.DB_CHAT_ROOMS
import com.peachmind.chatapp.Key.Companion.DB_USERS
import com.peachmind.chatapp.R
import com.peachmind.chatapp.databinding.ActivityChatBinding
import com.peachmind.chatapp.userlist.UserItem
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ChatActivity : AppCompatActivity() {

  private lateinit var binding: ActivityChatBinding
  private lateinit var chatAdapter: ChatAdapter
  private lateinit var linearLayoutManager: LinearLayoutManager

  private var chatRoomId: String = ""
  private var otherUserId: String = ""
  private var otherUserFcmToken: String = ""
  private var myUserId: String = ""
  private var myUsername: String = ""
  private val chatItemList = mutableListOf<ChatItem>()
  private var isInit = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityChatBinding.inflate(layoutInflater)
    setContentView(binding.root)

    chatRoomId = intent.getStringExtra("chatRoomId") ?: return
    otherUserId = intent.getStringExtra("otherUserId") ?: return
    myUserId = Firebase.auth.currentUser?.uid ?: ""
    chatAdapter = ChatAdapter()
    linearLayoutManager = LinearLayoutManager(applicationContext)

    Firebase.database.reference.child(DB_USERS).child(myUserId).get()
      .addOnSuccessListener {
        val myUserItem = it.getValue(UserItem::class.java)
        myUsername = myUserItem?.username ?: ""
        getOtherUserData()
      }

    binding.chatRecyclerView.apply {
      layoutManager = linearLayoutManager
      adapter = chatAdapter
    }

    chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
      override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)

        linearLayoutManager.smoothScrollToPosition(binding.chatRecyclerView, null, chatAdapter.itemCount)
      }
    })

    binding.sendButton.setOnClickListener {
      val message = binding.messageEditText.text.toString()

      if (!isInit) {
        return@setOnClickListener
      }

      if (message.isEmpty()) {
        Toast.makeText(applicationContext, "빈 메시지를 전송할 수는 없습니다.", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      val newChatItem = ChatItem(
        message = message,
        userId = myUserId
      )

      Firebase.database.reference.child(DB_CHATS).child(chatRoomId).push().apply {
        newChatItem.chatId = key
        setValue(newChatItem)
      }

      val updates: MutableMap<String, Any> = hashMapOf(
        "${DB_CHAT_ROOMS}/$myUserId/$otherUserId/lastMessage" to message,
        "${DB_CHAT_ROOMS}/$otherUserId/$myUserId/lastMessage" to message,
        "${DB_CHAT_ROOMS}/$otherUserId/$myUserId/chatRoomId" to chatRoomId,
        "${DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserId" to myUserId,
        "${DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUsername" to myUsername,
      )

      Firebase.database.reference.updateChildren(updates)

      val client = OkHttpClient()

      val root = JSONObject()
      val notification = JSONObject()
      notification.put("title", getString(R.string.app_name))
      notification.put("body", message)
      root.put("to", otherUserFcmToken)
      root.put("priority", "high")
      root.put("notification", notification)

      val requestBody = root.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

      val request = Request.Builder()
        .post(requestBody)
        .url("https://fcm.googleapis.com/fcm/send")
        .header("Authorization", "key=${getString(R.string.fcm_server_key)}")
        .build()
      client.newCall(request)
        .enqueue(object : Callback {
          override fun onFailure(call: Call, e: IOException) {
            e.stackTraceToString()
          }

          override fun onResponse(call: Call, response: Response) {
            Log.e("ChatActivity", response.toString())
          }
        })
      binding.messageEditText.text.clear()
    }
  }

  private fun getChatData() {
    Firebase.database.reference.child(DB_CHATS).child(chatRoomId)
      .addChildEventListener(object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
          val chatItem = snapshot.getValue(ChatItem::class.java)
          chatItem ?: return

          chatItemList.add(chatItem)
          chatAdapter.submitList(chatItemList.toMutableList())
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
      })
  }

  private fun getOtherUserData() {
    Firebase.database.reference.child(DB_USERS).child(otherUserId).get()
      .addOnSuccessListener {
        val otherUserItem = it.getValue(UserItem::class.java)
        otherUserFcmToken = otherUserItem?.fcmToken.orEmpty()
        chatAdapter.otherUserItem = otherUserItem

        isInit = true
        getChatData()
      }
  }

  companion object {
    const val EXTRA_CHAT_ROOM_ID = "chatRoomId"
    const val EXTRA_OTHER_USER_ID = "otherUserId"
  }
}