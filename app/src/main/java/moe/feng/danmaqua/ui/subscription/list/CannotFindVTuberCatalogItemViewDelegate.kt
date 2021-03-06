package moe.feng.danmaqua.ui.subscription.list

import android.view.View
import androidx.core.net.toUri
import com.google.androidbrowserhelper.trusted.TwaLauncher
import moe.feng.danmaqua.R
import moe.feng.danmaqua.ui.common.list.BaseViewHolder
import moe.feng.danmaqua.ui.common.list.SimpleViewBinder
import moe.feng.danmaqua.ui.common.list.viewHolderCreatorOf
import moe.feng.danmaqua.ui.subscription.list.CannotFindVTuberCatalogItemViewDelegate.Item
import moe.feng.danmaqua.ui.subscription.list.CannotFindVTuberCatalogItemViewDelegate.ViewHolder

class CannotFindVTuberCatalogItemViewDelegate : SimpleViewBinder<Item, ViewHolder>() {

    override val viewHolderCreator: ViewHolderCreator<ViewHolder> =
        viewHolderCreatorOf(R.layout.cannot_find_vtuber_catalog_item)

    object Item

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        override fun onItemClick() {
            val uri = context.getString(R.string.cannot_find_vtuber_in_catalog_url).toUri()
            TwaLauncher(context).launch(uri)
        }

    }

}