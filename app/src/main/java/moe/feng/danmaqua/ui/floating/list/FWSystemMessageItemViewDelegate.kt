package moe.feng.danmaqua.ui.floating.list

import android.view.View
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import kotlinx.android.synthetic.main.danmaku_fw_item_view.*
import moe.feng.danmaqua.R
import moe.feng.danmaqua.ui.floating.FloatingWindowHolder
import moe.feng.danmaqua.ui.common.list.ItemBasedSimpleViewBinder
import moe.feng.danmaqua.ui.common.list.ItemBasedViewHolder
import moe.feng.danmaqua.ui.common.list.innerViewHolderCreatorOf

class FWSystemMessageItemViewDelegate(val fwHolder: FloatingWindowHolder) :
    ItemBasedSimpleViewBinder<String, FWSystemMessageItemViewDelegate.ViewHolder>(){

    override val viewHolderCreator: ViewHolderCreator<ViewHolder>
        = innerViewHolderCreatorOf(R.layout.danmaku_fw_item_view)

    inner class ViewHolder(itemView: View) : ItemBasedViewHolder<String>(itemView) {

        override fun onBind() {
            text1.gravity = fwHolder.textGravity

            text1.textSize = fwHolder.textSize.toFloat()
            val latest = adapterPosition + 1 == fwHolder.listView.layoutManager?.itemCount
            text1.text = if (latest) {
                buildSpannedString {
                    bold { append(data) }
                }
            } else {
                data
            }
        }

    }

}