package com.peachmind.chatapp.chatlist

data class ChatRoomItem(
  val chatRoomId: String? = null,
  val lastMessage: String? = null,
  val otherUsername: String? = null,
  val otherUserId: String? = null,
)