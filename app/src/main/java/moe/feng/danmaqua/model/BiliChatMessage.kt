package moe.feng.danmaqua.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import moe.feng.danmaqua.util.JsonUtils

@Parcelize
open class BiliChatMessage(open val cmd: String, open val timestamp: Long) : Parcelable {

    companion object {

        const val CMD_SEND_GIFT = "SEND_GIFT"
        const val CMD_DANMAKU = "DANMU_MSG"
        const val CMD_ENTRY_EFFECT = "ENTRY_EFFECT"
        const val CMD_WELCOME_GUARD = "WELCOME_GUARD"

        fun from(json: String): BiliChatMessage {
            return from(JsonUtils.fromJson(json, Map::class.java))
        }

        fun from(map: Map<*, *>): BiliChatMessage {
            val command: String = map["cmd"] as? String ?: ""
            return when (command) {
                CMD_SEND_GIFT -> {
                    val data = map["data"] as? Map<*, *> ?: emptyMap<String, Any>()
                    BiliChatSendGift(
                        command,
                        data["giftName"] as? String ?: "",
                        (data["num"] as? Number)?.toInt() ?: 0,
                        data["uname"] as? String ?: "",
                        data["face"] as? String ?: "",
                        (data["uid"] as? Number)?.toLong() ?: 0L,
                        (data["timestamp"] as? Number)?.toLong() ?: 0L,
                        (data["giftId"] as? Number)?.toInt() ?: 0,
                        (data["giftType"] as? Number)?.toInt() ?: 0,
                        data["action"] as? String ?: "",
                        (data["super"] as? Number)?.toInt() ?: 0,
                        (data["super_gift_num"] as? Number)?.toInt() ?: 0,
                        (data["super_batch_gift_num"] as? Number)?.toInt() ?: 0,
                        (data["price"] as? Number)?.toInt() ?: 0,
                        data["coin_type"] as? String ?: "",
                        (data["total_coin"] as? Number)?.toInt() ?: 0
                    )
                }
                CMD_DANMAKU -> {
                    val info = map["info"] as? List<*> ?: emptyList<Any>()
                    val text = info[1] as? String ?: ""
                    val senderInfo = info[2] as? List<*> ?: emptyList<Any>()
                    val senderUid = (senderInfo[0] as? Number)?.toLong() ?: 0L
                    val senderName = senderInfo[1] as? String ?: ""
                    val tsInfo = info[9] as? Map<*, *> ?: emptyMap<String, Any>()
                    BiliChatDanmaku(
                        command,
                        text,
                        senderName,
                        senderUid,
                        (tsInfo["ts"] as? Number)?.toLong() ?: 0L
                    )
                }
                else -> BiliChatMessage(command, 0L)
            }
        }

    }

    override fun toString(): String {
        return "BiliChatMessage[cmd: $cmd, timestamp: $timestamp, (Unrecognized data)]"
    }

}